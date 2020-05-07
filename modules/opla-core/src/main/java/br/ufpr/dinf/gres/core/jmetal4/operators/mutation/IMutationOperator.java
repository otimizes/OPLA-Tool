package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.core.jmetal4.core.Solution;

public interface IMutationOperator {

    void execute(double probability, Solution solution, String scope);
}
