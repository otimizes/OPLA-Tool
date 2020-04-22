package br.ufpr.dinf.gres.patterns.strategies;

import br.ufpr.dinf.gres.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.defaultstrategy.RandomScopeSelection;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.impl.WholeArchitectureScopeSelection;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.impl.WholeArchitectureWithoutPackageScopeSelection;

public enum ScopeSelection implements IScopeSelection {
    RANDOM {
        @Override
        public ScopeSelectionStrategy get() {
            return new RandomScopeSelection();
        }
    },
    WHOLE {
        @Override
        public ScopeSelectionStrategy get() {
            return new WholeArchitectureScopeSelection();
        }
    },
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