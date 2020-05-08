package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.common.Configuration;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.operators.FeatureMutationOperators;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PLAFeatureMutation extends Mutation {

    private static final long serialVersionUID = 9039316729379302747L;
    static Logger LOGGER = LogManager.getLogger(PLAFeatureMutation.class.getName());

    private Double mutationProbability = null;
    private List<String> mutationOperators;

    public PLAFeatureMutation(HashMap<String, Object> parameters, List<String> mutationOperators) {
        super(parameters);
        this.mutationOperators = mutationOperators;

        if (parameters.get("probability") != null) {
            mutationProbability = (Double) parameters.get("probability");
        }
    }

    public PLAFeatureMutation(Map<String, Object> parameters) {
        super(parameters);
        if (parameters.get("probability") != null) {
            mutationProbability = (Double) parameters.get("probability");
        }
    }

    public void doMutation(double probability, Solution solution) throws Exception {
        String scope = "sameComponent"; //allLevels

        int r = PseudoRandom.randInt(0, this.mutationOperators.size() - 1);
        HashMap<Integer, String> operatorMap = new HashMap<>();
        for (int i = 0; i < this.mutationOperators.size(); i++)
            operatorMap.put(i, this.mutationOperators.get(i));
        FeatureMutationOperators selectedOperator = FeatureMutationOperators.valueOf(operatorMap.get(r));
        selectedOperator.getOperator().execute(probability, solution, scope);
    }


    public Object execute(Object object) throws Exception {
        Solution solution = (Solution) object;
        Double probability = (Double) getParameter("probability");

        if (probability == null) {
            Configuration.logger_.severe("FeatureMutation.execute: probability not specified");
            java.lang.Class<String> cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }
        this.doMutation(mutationProbability, solution);

        if (!MutationUtils.isValidSolution(((Architecture) solution.getDecisionVariables()[0]))) {
            Architecture clone;
            clone = ((Architecture) solution.getDecisionVariables()[0]).deepClone();
            solution.getDecisionVariables()[0] = clone;
            OPLA.contDiscardedSolutions_++;
        }

        return solution;
    }

    public List<String> getMutationOperators() {
        return mutationOperators;
    }
}