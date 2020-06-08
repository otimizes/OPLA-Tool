package br.otimizes.oplatool.core.jmetal4.operators;

/**
 * Interface of method that get mutation operator, implemented by MutationOperators and CrossoverOperators enum
 */
public interface IOperators {
    IOperator getOperator();
}
