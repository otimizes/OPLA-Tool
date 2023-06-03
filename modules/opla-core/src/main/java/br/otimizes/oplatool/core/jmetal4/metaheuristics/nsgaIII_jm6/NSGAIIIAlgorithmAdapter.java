package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII_jm6;

import java.util.List;

import org.apache.log4j.Logger;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveFunction;
import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveHandler;
import br.otimizes.oplatool.core.learning.SubjectiveAnalyzeAlgorithm;

/*
 * @author Lucas Wolschick
 */
public class NSGAIIIAlgorithmAdapter extends Algorithm {
    private static final Logger LOGGER = Logger.getLogger(NSGAIIIAlgorithmAdapter.class);
    private InteractiveHandler handler;

    public NSGAIIIAlgorithmAdapter(Problem problem) {
        super(problem);
        handler = new InteractiveHandler();
    }

    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        LOGGER.info("Initializing execution");

        int populationSize = (Integer) getInputParameter("populationSize");
        int maxEvaluations = (Integer) getInputParameter("maxEvaluations");

        // interatividade
        boolean interactive = (Boolean) getInputParameter("interactive");
        if (interactive) {
            InteractiveHandler.InteractiveConfig options = new InteractiveHandler.InteractiveConfig();
            options.setMaxInteractions((Integer) getInputParameter("maxInteractions"));
            options.setFirstInteraction((Integer) getInputParameter("firstInteraction"));
            options.setIntervalInteraction((Integer) getInputParameter("intervalInteraction"));
            options.setInteractiveFunction((InteractiveFunction) getInputParameter("interactiveFunction"));
            handler.setInteractiveConfig(options);
            handler.resetInteractionData();
        }

        Operator mutationOperator = operators_.get("mutation");
        Operator crossoverOperator = operators_.get("crossover");
        Operator selectionOperator = operators_.get("selection");

        NSGAIII algorithm = new NSGAIIIBuilder(problem_)
                .setCrossoverOperator(crossoverOperator)
                .setMutationOperator(mutationOperator)
                .setSelectionOperator(selectionOperator)
                .setMaxEvaluations(maxEvaluations)
                .setPopulationSize(populationSize)
                .setInteractive(interactive ? handler : null)
                .build();

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
