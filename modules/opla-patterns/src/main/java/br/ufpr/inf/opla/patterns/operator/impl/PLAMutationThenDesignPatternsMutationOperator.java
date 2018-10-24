/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.operator.impl;

import br.ufpr.inf.opla.patterns.operator.AbstractMutationOperator;
import br.ufpr.inf.opla.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import jmetal4.core.Solution;

import java.util.Map;

/**
 * @author giovaniguizzo
 */
public class PLAMutationThenDesignPatternsMutationOperator extends AbstractMutationOperator {

    private static final long serialVersionUID = 1L;

    private final PLAMutation pLAMutation;
    private final DesignPatternMutationOperator designPatternMutationOperator;

    public PLAMutationThenDesignPatternsMutationOperator(Map<String, Object> parameters, ScopeSelectionStrategy scopeSelectionStrategy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        super(parameters);
        pLAMutation = new PLAMutation(parameters);
        designPatternMutationOperator = new DesignPatternMutationOperator(parameters, scopeSelectionStrategy, designPatternSelectionStrategy);
    }

    @Override
    protected boolean hookMutation(Solution solution, Double probability) throws Exception {
        if (pLAMutation.hookMutation(solution, probability)) {
            return designPatternMutationOperator.hookMutation(solution, 2.0D);
        }
        return false;
    }

}
