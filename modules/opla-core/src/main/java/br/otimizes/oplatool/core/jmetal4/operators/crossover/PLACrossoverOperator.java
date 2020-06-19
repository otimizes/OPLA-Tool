package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.ArchitectureSolutionType;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.operators.CrossoverOperators;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * PLA Crossover operator that call another crossover operators
 */
public class PLACrossoverOperator extends Crossover {

    private static final long serialVersionUID = -51015356906090226L;
    private static List VALID_TYPES = Arrays.asList(ArchitectureSolutionType.class);

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    private List<String> operators;


    public PLACrossoverOperator(Map<String, Object> parameters, List<String> operators) {
        super(parameters);
        this.operators = operators;
    }

    PLACrossoverOperator(Map<String, Object> parameters) {
        super(parameters);
    }

    public Object execute(Object object) throws JMException {
        Solution[] parents = (Solution[]) object;
        if (!(VALID_TYPES.contains(parents[0].getType().getClass()) && VALID_TYPES.contains(parents[1].getType().getClass()))) {
            Configuration.logger_.severe("PLACrossover.execute: the solutions " + "are not of the right type. The type should be 'Permutation', but " + parents[0].getType() + " and " + parents[1].getType() + " are obtained");
        }
        if (parents.length < 2) {
            Configuration.logger_.severe("PLACrossover.execute: operator needs two parents");
            java.lang.Class<String> cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        int r = PseudoRandom.randInt(0, this.operators.size() - 1);
        if (r != 0 && this.operators.size() == 1) r = 0;
        else if (this.operators.size() <= 0) return object;
        CrossoverOperators selectedOperator = CrossoverOperators.valueOf(this.operators.get(r));
        return selectedOperator.getOperator().execute(parameters_, parents, "allLevels");
    }


}