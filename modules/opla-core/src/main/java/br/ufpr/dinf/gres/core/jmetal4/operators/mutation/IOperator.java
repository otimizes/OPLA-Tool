package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import java.util.Map;

/**
 * Interface that has the mutation operator execution method
 */
public interface IOperator<T extends Object> {

    T execute(Map<String, Object> parameters, T solution, String scope);
}
