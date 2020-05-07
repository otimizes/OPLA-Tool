package br.ufpr.dinf.gres.core.jmetal4.main.patterns;

import br.ufpr.dinf.gres.core.jmetal4.operators.pattern.AbstractMutationOperator;
import br.ufpr.dinf.gres.core.jmetal4.operators.pattern.impl.DesignPatternMutationOperator;
import br.ufpr.dinf.gres.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;

import java.util.Map;

public class DesignPatternsAndPLAMutationOperator extends AbstractMutationOperator {

    private static final long serialVersionUID = 1L;

    private final PLAMutation pLAMutation;
    private final DesignPatternMutationOperator designPatternMutationOperator;

    public DesignPatternsAndPLAMutationOperator(Map<String, Object> parameters, ScopeSelectionStrategy scopeSelectionStrategy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        super(parameters);
        pLAMutation = new PLAMutation(parameters);
        designPatternMutationOperator = new DesignPatternMutationOperator(parameters, scopeSelectionStrategy, designPatternSelectionStrategy);
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
