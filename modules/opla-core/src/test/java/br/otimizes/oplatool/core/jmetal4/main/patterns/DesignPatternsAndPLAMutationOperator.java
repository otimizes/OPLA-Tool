package br.otimizes.oplatool.core.jmetal4.main.patterns;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.operators.pattern.AbstractMutationOperator;
import br.otimizes.oplatool.core.jmetal4.operators.pattern.impl.DesignPatternsMutationOperator;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;
import br.otimizes.oplatool.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.Map;

public class DesignPatternsAndPLAMutationOperator extends AbstractMutationOperator {

    private static final long serialVersionUID = 1L;

    private final PLAMutation pLAMutation;
    private final DesignPatternsMutationOperator designPatternMutationOperator;

    public DesignPatternsAndPLAMutationOperator(Map<String, Object> parameters, ScopeSelectionStrategy scopeSelectionStrategy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        super(parameters);
        pLAMutation = new PLAMutation(parameters);
        designPatternMutationOperator = new DesignPatternsMutationOperator(parameters, scopeSelectionStrategy, designPatternSelectionStrategy);
    }

    @Override
    protected boolean hookMutation(Solution solution, Double probability) throws Exception {
        int random = PseudoRandom.randInt(0, 6);
        if (random == 0) {
            return designPatternMutationOperator.hookMutation(solution, probability);
        } else {
            return pLAMutation.hookMutation(solution, probability);
        }
    }

}
