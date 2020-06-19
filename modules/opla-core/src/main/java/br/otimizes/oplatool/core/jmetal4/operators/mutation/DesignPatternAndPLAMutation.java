package br.otimizes.oplatool.core.jmetal4.operators.mutation;

import br.otimizes.oplatool.core.jmetal4.operators.pattern.impl.DesignPatternsMutationOperator;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.Map;

/**
 * Mutation operator that uses the PLA mutation and Design pattern
 */
public class DesignPatternAndPLAMutation extends Mutation {

    private static final long serialVersionUID = 3896434361251147825L;
    private DesignPatternsMutationOperator dpm;
    private PLAMutationOperator pf;

    public DesignPatternAndPLAMutation(Map<String, Object> parameter, DesignPatternsMutationOperator dpm, PLAMutationOperator pf) {
        super(parameter);
        this.dpm = dpm;
        this.pf = pf;
    }

    @Override
    public Object execute(Object object) throws Exception {
        int mutationQuantity = this.pf.getOperators().size();
        int r = PseudoRandom.randInt(0, mutationQuantity);
        if (r == 0) {
            return this.dpm.execute(object);
        } else {
            return this.pf.execute(object);
        }
    }
}
