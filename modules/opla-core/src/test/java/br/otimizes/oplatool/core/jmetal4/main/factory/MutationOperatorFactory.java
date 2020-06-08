/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.core.jmetal4.main.factory;

import br.otimizes.oplatool.core.jmetal4.main.patterns.DesignPatternsAndPLAMutationOperator;
import br.otimizes.oplatool.core.jmetal4.main.patterns.PLAMutationThenDesignPatternsMutationOperator;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.core.jmetal4.operators.pattern.impl.DesignPatternsMutationOperator;
import br.otimizes.oplatool.core.jmetal4.main.patterns.PLAMutation;

import java.util.Map;

/**
 * @author giovaniguizzo
 */
public class MutationOperatorFactory {

    public static Mutation create(String operator, Map<String, Object> parameters) {
        switch (operator) {
            case "DesignPatternsMutationOperator":
                return new DesignPatternsMutationOperator(parameters, null, null);
            case "DesignPatternsAndPLAMutationOperator":
                return new DesignPatternsAndPLAMutationOperator(parameters, null, null);
            case "PLAMutation":
                return new PLAMutation(parameters);
            case "PLAMutationThenDesignPatternsMutationOperator":
                return new PLAMutationThenDesignPatternsMutationOperator(parameters, null, null);
            default:
                return null;
        }
    }
}
