package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII_jm6;

import java.util.List;

import org.apache.log4j.Logger;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;

/*
 * @author Lucas Wolschick
 */
public class NSGAIIIAlgorithmAdapter extends Algorithm {
    private static final Logger LOGGER = Logger.getLogger(NSGAIIIAlgorithmAdapter.class);
    
    public NSGAIIIAlgorithmAdapter(Problem problem) {
        super(problem);
    }

    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        LOGGER.info("Initializing execution");
        
        int populationSize = (Integer) getInputParameter("populationSize");
        int maxEvaluations = (Integer) getInputParameter("maxEvaluations");
        //int maxDivisions =  (Integer) this.getInputParameter("div1");

        Operator mutationOperator = operators_.get("mutation");
        Operator crossoverOperator = operators_.get("crossover");
        Operator selectionOperator = operators_.get("selection");

        NSGAIIIBuilder builder = new NSGAIIIBuilder(problem_);
        builder.setCrossoverOperator(crossoverOperator);
        builder.setMutationOperator(mutationOperator);
        builder.setSelectionOperator(selectionOperator);
        builder.setMaxEvaluations(maxEvaluations);
        builder.setPopulationSize(populationSize);
        //builder.setNumberOfDivisions(maxDivisions);

        NSGAIII algorithm = builder.build();
        try {
            algorithm.run();
        } catch (JMException | ClassNotFoundException e) {
            throw e;
        } catch (Exception e) {
            // :(
            throw new JMException(e);
        }

        List<Solution> result = algorithm.result();
        SolutionSet set = new SolutionSet(result.size());
        for (Solution s : result) {
            set.add(s);
        }
        return set;
    }
    
}
