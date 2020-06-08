/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.core.jmetal4.main.patterns;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.operators.pattern.AbstractMutationOperator;
import br.otimizes.oplatool.core.jmetal4.operators.pattern.impl.DesignPatternsMutationOperator;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;
import br.otimizes.oplatool.patterns.strategies.scopeselection.ScopeSelectionStrategy;

import java.util.Map;

/**
 * @author giovaniguizzo
 */
public class PLAMutationThenDesignPatternsMutationOperator extends AbstractMutationOperator {

    private static final long serialVersionUID = 1L;

    private final PLAMutation pLAMutation;
    private final DesignPatternsMutationOperator designPatternMutationOperator;

    public PLAMutationThenDesignPatternsMutationOperator(Map<String, Object> parameters, ScopeSelectionStrategy scopeSelectionStrategy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        super(parameters);
        pLAMutation = new PLAMutation(parameters);
        designPatternMutationOperator = new DesignPatternsMutationOperator(parameters, scopeSelectionStrategy, designPatternSelectionStrategy);
    }

    @Override
    protected boolean hookMutation(Solution solution, Double probability) throws Exception {
        if (pLAMutation.hookMutation(solution, probability)) {
            return designPatternMutationOperator.hookMutation(solution, 2.0D);
        }
        return false;
    }
}
