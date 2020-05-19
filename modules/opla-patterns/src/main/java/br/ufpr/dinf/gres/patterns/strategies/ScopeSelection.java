package br.ufpr.dinf.gres.patterns.strategies;

import br.ufpr.dinf.gres.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.defaultstrategy.RandomScopeSelection;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.impl.WholeArchitectureScopeSelection;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.impl.WholeArchitectureWithoutPackageScopeSelection;

/**
 * The Enum ScopeSelection.
 */
public enum ScopeSelection implements IScopeSelection {
    
    /** The random. */
    RANDOM {
        @Override
        public ScopeSelectionStrategy get() {
            return new RandomScopeSelection();
        }
    },
    
    /** The whole. */
    WHOLE {
        @Override
        public ScopeSelectionStrategy get() {
            return new WholeArchitectureScopeSelection();
        }
    },
    
    /** The whole without package. */
    WHOLE_WITHOUT_PACKAGE {
        @Override
        public ScopeSelectionStrategy get() {
            return new WholeArchitectureWithoutPackageScopeSelection();
        }
    },
}

interface IScopeSelection {
    ScopeSelectionStrategy get();
}