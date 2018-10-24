/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.factory;

import br.ufpr.inf.opla.patterns.operator.impl.DesignPatternMutationOperator;
import br.ufpr.inf.opla.patterns.operator.impl.DesignPatternsAndPLAMutationOperator;
import br.ufpr.inf.opla.patterns.operator.impl.PLAMutation;
import br.ufpr.inf.opla.patterns.operator.impl.PLAMutationThenDesignPatternsMutationOperator;
import jmetal4.operators.mutation.Mutation;

import java.util.Map;

/**
 * @author giovaniguizzo
 */
public class MutationOperatorFactory {

    public static Mutation create(String operator, Map<String, Object> parameters) {
        switch (operator) {
            case "DesignPatternsMutationOperator":
                return new DesignPatternMutationOperator(parameters, null, null);
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
