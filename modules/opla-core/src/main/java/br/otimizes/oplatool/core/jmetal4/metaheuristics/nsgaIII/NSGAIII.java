package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import br.otimizes.oplatool.architecture.io.OPLALogs;
import br.otimizes.oplatool.architecture.io.OptimizationInfo;
import br.otimizes.oplatool.architecture.io.OptimizationInfoStatus;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveHandler;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII.util.EnvironmentalSelection;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII.util.ReferencePoint;
import br.otimizes.oplatool.core.jmetal4.operators.CrossoverOperators;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverUtils;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.PLACrossoverOperator;
import br.otimizes.oplatool.domain.OPLAThreadScope;

// Código baseado em/obtido de: https://github.com/jMetal/jMetal (versão 6)
// Acessado em 2023-05-31 22:54 BRT
// ~ Lucas Wolschick

/**
 * Created by ajnebro on 30/10/14.
 * Modified by Juanjo on 13/11/14
 *
 * This implementation is based on the code of Tsung-Che Chiang
 * http://web.ntnu.edu.tw/~tcchiang/publications/nsga3cpp/nsga3cpp.htm
 */
public class NSGAIII {
    protected int evaluations;
    protected int maxEvaluations;

    protected int numberOfDivisions;
    protected List<ReferencePoint> referencePoints = new Vector<>();

    protected InteractiveHandler interactive;

    /** Constructor */
    public NSGAIII(NSGAIIIBuilder builder) { // can be created from the NSGAIIIBuilder within the same package
        // super(builder.getProblem());
        setProblem(builder.getProblem());

        maxEvaluations = builder.getMaxEvaluations();

        crossoverOperator = builder.getCrossoverOperator();
        mutationOperator = builder.getMutationOperator();
        selectionOperator = builder.getSelectionOperator();

        /// NSGAIII
        numberOfDivisions = builder.getNumberOfDivisions();

        (new ReferencePoint()).generateReferencePoints(referencePoints, getProblem().getNumberOfObjectives(),
                numberOfDivisions);

        int populationSize = referencePoints.size();
        while (populationSize % 4 > 0) {
            populationSize++;
        }

        setMaxPopulationSize(populationSize);

        /// Interactivity
        interactive = builder.getInteractive(); // can be null
    }

    protected void initProgress() {
        evaluations = 1;
    }

    protected void updateProgress() {
        evaluations++;
    }

    protected boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    protected List<Solution> evaluatePopulation(List<Solution> population, ArrayList<Integer> originalArchElementCount)
            throws JMException {
        List<Solution> newPopulation = new ArrayList<>();

        for (Solution s : population) {
            problem.evaluateConstraints(s);
            problem.evaluate(s);
            ArrayList<Integer> newArchElementCount = CrossoverUtils.getElementAmount(s);
            if (IsValidArchElements(originalArchElementCount, newArchElementCount)) {
                newPopulation.add(s);
            }
        }

        return population;
    }

    protected List<Solution> selectionReproduction(List<Solution> population) throws Exception {
        List<Solution> offspringPopulation = new ArrayList<>(getMaxPopulationSize());
        SolutionSet populationSet = toSet(population);
        for (int i = 0; i < getMaxPopulationSize(); i += 2) {
            if (!isStoppingConditionReached()) {
                Solution[] parents = new Solution[2];
                if (((PLACrossoverOperator) crossoverOperator).getOperators()
                        .contains(CrossoverOperators.PLA_COMPLEMENTARY_CROSSOVER.name())) {
                    parents = CrossoverUtils.selectionComplementary(populationSet);
                } else {
                    parents[0] = (Solution) selectionOperator.execute(populationSet);
                    parents[1] = (Solution) selectionOperator.execute(populationSet);
                }

                Solution[] offspring = (Solution[]) crossoverOperator.execute(parents);
                for (Solution child : offspring) {
                    problem.evaluateConstraints(child);
                    mutationOperator.execute(child);
                    offspringPopulation.add(child);
                    updateProgress();
                }
            }
        }
        return offspringPopulation;
    }

    private List<ReferencePoint> getReferencePointsCopy() {
        List<ReferencePoint> copy = new ArrayList<>();
        for (ReferencePoint r : this.referencePoints) {
            copy.add(new ReferencePoint(r));
        }
        return copy;
    }

    protected List<Solution> replacement(List<Solution> population, List<Solution> offspringPopulation)
            throws JMException {

        List<Solution> jointPopulation = new ArrayList<>();
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);

        FastNonDominatedSortRanking ranking = computeRanking(jointPopulation);

        // List<Solution> pop = crowdingDistanceSelection(ranking);
        List<Solution> last = new ArrayList<>();
        List<Solution> pop = new ArrayList<>();
        List<List<Solution>> fronts = new ArrayList<>();
        int rankingIndex = 0;
        int candidateSolutions = 0;
        while (candidateSolutions < getMaxPopulationSize()) {
            last = ranking.getSubFront(rankingIndex);
            fronts.add(last);
            candidateSolutions += last.size();
            if ((pop.size() + last.size()) <= getMaxPopulationSize())
                pop.addAll(last);
            rankingIndex++;
        }

        if (pop.size() == this.getMaxPopulationSize())
            return pop;

        // A copy of the reference list should be used as parameter of the environmental
        // selection
        EnvironmentalSelection selection = new EnvironmentalSelection(fronts,
                getMaxPopulationSize() - pop.size(),
                getReferencePointsCopy(),
                getProblem().getNumberOfObjectives());

        List<Solution> choosen = selection.execute(last);
        pop.addAll(choosen);

        return pop;
    }

    public List<Solution> result() {
        return getNonDominatedSolutions(getPopulation());
    }

    protected FastNonDominatedSortRanking computeRanking(List<Solution> solutionList) {
        FastNonDominatedSortRanking ranking = new FastNonDominatedSortRanking(this.interactive);
        ranking.compute(solutionList);

        return ranking;
    }

    protected List<Solution> getNonDominatedSolutions(List<Solution> solutionList) {
        return SolutionListUtils.getNonDominatedSolutions(solutionList);
    }

    /// AbstractGeneticAlgorithm.java
    protected int maxPopulationSize;
    protected Operator selectionOperator;
    protected Operator crossoverOperator;
    protected Operator mutationOperator;

    /* Setters and getters */
    public void setMaxPopulationSize(int maxPopulationSize) {
        this.maxPopulationSize = maxPopulationSize;
    }

    public int getMaxPopulationSize() {
        return maxPopulationSize;
    }

    public Operator getSelectionOperator() {
        return selectionOperator;
    }

    public Operator getCrossoverOperator() {
        return crossoverOperator;
    }

    public Operator getMutationOperator() {
        return mutationOperator;
    }

    protected Solution createBaseSolution() throws Exception {
        Solution solutionBase = new Solution(problem);
        problem.evaluateConstraints(solutionBase);
        problem.evaluate(solutionBase);
        return solutionBase;
    }

    protected List<Solution> createInitialPopulation() throws Exception {
        List<Solution> population = new ArrayList<>();
        for (int i = 0; i < getMaxPopulationSize(); i++) {
            population.add(newRandomSolution(mutationOperator));
            updateProgress();
        }

        return population;
    }

    // AbstractEvolutionaryAlgorithm.java
    protected List<Solution> population;
    protected Problem problem;

    public List<Solution> getPopulation() {
        return population;
    }

    public void setPopulation(List<Solution> population) {
        this.population = population;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Problem getProblem() {
        return problem;
    }

    public void run() throws Exception {
        List<Solution> offspringPopulation;

        Solution baseSolution = createBaseSolution();
        ArrayList<Integer> originalArchElementCount = CrossoverUtils.getElementAmount(baseSolution);
        population = createInitialPopulation();
        population = evaluatePopulation(population, originalArchElementCount);
        initProgress();
        while (!isStoppingConditionReached()) {
            offspringPopulation = selectionReproduction(population);
            offspringPopulation = evaluatePopulation(offspringPopulation, originalArchElementCount);
            population = replacement(population, offspringPopulation);

            int generation = evaluations / getMaxPopulationSize();
            OPLAThreadScope.currentGeneration.set(generation);
            OPLALogs.add(new OptimizationInfo(Thread.currentThread().getId(), "Generation " + generation,
                    OptimizationInfoStatus.RUNNING));

            if (interactive != null) {
                interactive.checkAndInteract(generation, toSet(offspringPopulation));

                for (Solution solution : offspringPopulation) {

                    for (int i = 0; i < population.size(); i++) {
                        if (population.get(i).getEvaluation() == 0 && solution.getIdOrigem() !=0 && solution.getIdOrigem() == population.get(i).getIdOrigem()) {
                            population.get(i).setEvaluation(solution.getEvaluation());
                            population.get(i).setEvaluatedByUser(solution.getEvaluatedByUser());
                            population.get(i).setEvaluatedByUser3(solution.getEvaluatedByUser3());
                        }
                    }
                }

                for (int i = 0; i < population.size(); i++) {
                    if (population.get(i).getEvaluation() == 1) {
                        population.set(i, newRandomSolution(mutationOperator));
                        population.get(i).setId(i);
                        population.get(i).setIdOrigem(i+1);
                    }
                }
//                // eliminate badly-ranked solutions
//                for (int i = 0; i < population.size(); i++) {
//                    if (population.get(i).getEvaluation() == 1) {
//                        population.set(i, newRandomSolution(mutationOperator));
//                    }
//                }
            }
        }

        // evaluate final solution set
        if (interactive != null) {
            try {
                interactive.subjectiveAnalyzeSolutionSet(new SolutionSet(toSet(result())));
            } catch (Exception e) {
                e.printStackTrace();
                throw new JMException(e);
            }
        }
    }

    // NSGAII.java
    private Solution newRandomSolution(Operator mutationOperator)
            throws ClassNotFoundException, JMException, Exception {
        Solution newSolution = new Solution(problem);
        mutationOperator.execute(newSolution);
        problem.evaluate(newSolution);
        problem.evaluateConstraints(newSolution);
        return newSolution;
    }

    public static boolean IsValidArchElements(ArrayList<Integer> lst1, ArrayList<Integer> lst2) {
        if (!("" + lst1.get(0)).equals("" + lst2.get(0))) {
            return false;
        }
        if (!("" + lst1.get(1)).equals("" + lst2.get(1))) {
            return false;
        }
        return ("" + lst1.get(2)).equals("" + lst2.get(2));
    }

    // Helper
    private static SolutionSet toSet(List<Solution> solutions) {
        SolutionSet set = new SolutionSet(solutions.size());
        for (Solution s : solutions) {
            set.add(s);
        }
        return set;
    }
}
