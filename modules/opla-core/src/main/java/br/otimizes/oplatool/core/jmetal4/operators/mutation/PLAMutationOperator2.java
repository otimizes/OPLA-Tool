package br.otimizes.oplatool.core.jmetal4.operators.mutation;

import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolution2;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.operators.MutationOperators;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.uma.jmetal.operator.mutation.MutationOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PLA feature mutation operator that call another mutation operators included in FeatureMutationOperators enum
 */
public class PLAMutationOperator2 implements MutationOperator<OPLASolution2> {

    private static final long serialVersionUID = 9039316729379302747L;
    static Logger LOGGER = LogManager.getLogger(PLAMutationOperator2.class.getName());
    private int threshold;
    private int thresholdLc;
    protected Map<String, Object> parameters_;

    private Double probability;
    private List<String> operators;

    public PLAMutationOperator2(List<String> operators, Double probability) {
        this.operators = operators;
        this.probability = probability;

    }

    public PLAMutationOperator2(Double probability) {
        this.probability = probability;
    }

    public void doMutation(double probability, Solution solution) throws Exception {
        String scope = "sameComponent"; //allLevels

        int r = PseudoRandom.randInt(0, this.operators.size() - 1);
        HashMap<Integer, String> operatorMap = new HashMap<>();
        for (int i = 0; i < this.operators.size(); i++)
            operatorMap.put(i, this.operators.get(i));
        MutationOperators selectedOperator = MutationOperators.valueOf(operatorMap.get(r));
        selectedOperator.getOperator().execute(parameters_, solution, scope);
    }

    public List<String> getOperators() {
        return operators;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getThresholdLc() {
        return thresholdLc;
    }

    public void setThresholdLc(int thresholdLc) {
        this.thresholdLc = thresholdLc;
    }

    @Override
    public double getMutationProbability() {
        return this.probability;
    }

    @Override
    public OPLASolution2 execute(OPLASolution2 oplaSolution2) {
        if (probability == null) {
            Configuration.logger_.severe("FeatureMutation.execute: probability not specified");
        }
//        this.doMutation(this.probability, oplaSolution2.getVariable(0));

        return oplaSolution2;
    }
}