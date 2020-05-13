package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.core.jmetal4.core.Solution;

/**
 * Interface that has the mutation operator execution method
 */
public interface IMutationOperator {

    void execute(double probability, Solution solution, String scope);
}
