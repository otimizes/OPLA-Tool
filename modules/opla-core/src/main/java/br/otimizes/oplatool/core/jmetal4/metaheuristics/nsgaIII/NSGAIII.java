package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;

import java.util.ArrayList;
import java.util.HashSet;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.interactive.InteractWithDM;
import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveFunction;
import br.otimizes.oplatool.core.jmetal4.operators.CrossoverOperators;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverUtils;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.PLACrossoverOperator;
import br.otimizes.oplatool.core.learning.SubjectiveAnalyzeAlgorithm;

import org.apache.log4j.Logger;

public class NSGAIII extends Algorithm {

    private static final Logger LOGGER = Logger.getLogger(NSGAIII.class);

    private int populationSize_;

    private int div1_;
    private int div2_;

    private SolutionSet population_;
    SolutionSet offspringPopulation_;
    SolutionSet union_;

    int generations_;

    Operator crossover_;
    Operator mutation_;
    Operator selection_;

    double[][] lambda_; // reference points

    boolean normalize_; // do normalization or not

    private InteractWithDM interaction;

    public NSGAIII(Problem problem) {
        super(problem);
        interaction = new InteractWithDM();
    } // NSGAII

    public SolutionSet execute() throws JMException {
        generations_ = 0;

        int maxGenerations_ = (Integer) this.getInputParameter("maxGenerations");
        int maxInteractions = (Integer) getInputParameter("maxInteractions");
        int firstInteraction = (Integer) getInputParameter("firstInteraction");
        int intervalInteraction = (Integer) getInputParameter("intervalInteraction");
        boolean interactive = (Boolean) getInputParameter("interactive");
        InteractiveFunction interactiveFunction = ((InteractiveFunction) getInputParameter("interactiveFunction"));
        int currentInteraction = 0;
        HashSet<Solution> bestOfUserEvaluation = new HashSet<>();

        ArrayList<Integer> originalArchElementCount;
        ArrayList<Integer> newArchElementCount;

        try {
            Solution solution_base = new Solution(problem_);
            problem_.evaluateConstraints(solution_base);
            problem_.evaluate(solution_base);
            originalArchElementCount = CrossoverUtils.getElementAmount(solution_base);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JMException(e.getMessage());
        }

        div1_ = ((Integer) this.getInputParameter("div1")).intValue();

        div2_ = ((Integer) this.getInputParameter("div2")).intValue();


        normalize_ = ((Boolean) this.getInputParameter("normalize")).booleanValue();

        VectorGenerator vg = new TwoLevelWeightVectorGenerator(div1_, div2_,
                problem_.getNumberOfObjectives());
        lambda_ = vg.getVectors();

        populationSize_ = vg.getVectors().length;
        if (populationSize_ % 2 != 0)
            populationSize_ += 1;


        mutation_ = operators_.get("mutation");
        crossover_ = operators_.get("crossover");
        selection_ = operators_.get("selection");

        initPopulation();

        while (generations_ < maxGenerations_) {
            offspringPopulation_ = new SolutionSet(populationSize_);
            Solution[] parents = new Solution[2];
            for (int i = 0; i < (populationSize_/2); i++) { //MAN
                if (generations_ < maxGenerations_) {
                    // obtain parents

                    try {
                        if (((PLACrossoverOperator) crossover_).getOperators().contains(CrossoverOperators.PLA_COMPLEMENTARY_CROSSOVER.name())) {
                            parents = CrossoverUtils.selectionComplementary(population_);
                        } else {
                            parents[0] = (Solution) selection_.execute(population_);
                            parents[1] = (Solution) selection_.execute(population_);
                        }

                        Solution[] offSpring = (Solution[]) crossover_.execute(parents);
                        for (Solution child : offSpring) {
                            problem_.evaluateConstraints(child);
                            mutation_.execute(child);
                            problem_.evaluateConstraints(child);
                            problem_.evaluate(child);
                            newArchElementCount = CrossoverUtils.getElementAmount(child);
                            if (IsValidArchElements(originalArchElementCount, newArchElementCount)) {
                                offspringPopulation_.add(child);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } // if
            } // for


            union_ = ((SolutionSet) population_).union(offspringPopulation_);

            // Ranking the union
            Ranking ranking = new NondominatedRanking(union_);

            int remain = populationSize_;
            int index = 0;
            SolutionSet front = null;
            population_.clear();

            // Obtain the next front
            front = ranking.getSubfront(index);

            while ((remain > 0) && (remain >= front.size())) {

                for (int k = 0; k < front.size(); k++) {
                    population_.add(front.get(k));
                } // for

                // Decrement remain
                remain = remain - front.size();

                // Obtain the next front
                index++;
                if (remain > 0) {
                    front = ranking.getSubfront(index);
                } // if
            }

            if (remain > 0) { // front contains individuals to insert

                new Niching(population_, front, lambda_, remain, normalize_)
                        .execute();
                remain = 0;
            }

            generations_++;

            // verify if we need to interact after this generation
            if (interactive) {
                try {
                    // assign scores to population
                    currentInteraction = interaction.interactWithDM(
                        generations_, offspringPopulation_, maxInteractions,
                        firstInteraction, intervalInteraction, interactiveFunction,
                        currentInteraction, bestOfUserEvaluation
                    );
                    // solutions ranked 1 are replaced (erased) by random solutions
                    for (int i = 0; i < population_.getSolutionSet().size(); i++) {
                        if (population_.get(i).getEvaluation() == 1) {
                            population_.getSolutionSet().set(i, newRandomSolution(mutation_));
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e);
                    e.printStackTrace();
                    throw new JMException(e);
                }
            }

        }

        Ranking ranking = new NondominatedRanking(population_);
        SolutionSet winningFront = ranking.getSubfront(0);

        // add back solutions rated 5 by the user
        for (Solution s: bestOfUserEvaluation) {
            if (!winningFront.getSolutionSet().contains(s)) {
                winningFront.add(s);
            }
        }

        // rate the final solutions if our model is trained
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = interaction.getSubjectiveAnalyzeAlgorithm();
        if (interactive && subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained()) {
            try {
                subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(new OPLASolutionSet(winningFront), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return winningFront;

    }

    public void initPopulation() {

        population_ = new SolutionSet(populationSize_);

        for (int i = 0; i < populationSize_; i++) {
            Solution newSolution = null;
            try {
                newSolution = new Solution(problem_);

                problem_.evaluate(newSolution);
                problem_.evaluateConstraints(newSolution);
            } catch (ClassNotFoundException | JMException e) {
                e.printStackTrace();
            }

            population_.add(newSolution);
        } // for
    } // initPopulation

    private Solution newRandomSolution(Operator mutationOperator) throws Exception {
        Solution newSolution;
        newSolution = new Solution(problem_);
        mutationOperator.execute(newSolution);
        problem_.evaluate(newSolution);
        problem_.evaluateConstraints(newSolution);
        return newSolution;
    }

    // NB: this only exists so SubjectiveAnalyzeAlgorithmTest can be executed
    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return interaction.getSubjectiveAnalyzeAlgorithm();
    }

    public boolean IsValidArchElements(ArrayList<Integer> lst1, ArrayList<Integer> lst2) {
        if (!("" + lst1.get(0)).equals("" + lst2.get(0))) {
            return false;
        }
        if (!("" + lst1.get(1)).equals("" + lst2.get(1))) {
            return false;
        }
        return ("" + lst1.get(2)).equals("" + lst2.get(2));
    }
} // NSGA-III