package br.ufpr.dinf.gres.patterns.strategies.scopeselection.defaultstrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Patterns;
import br.ufpr.dinf.gres.patterns.models.Scope;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.ScopeSelectionStrategy;

/**
 * The Class RandomScopeSelection.
 */
public class RandomScopeSelection implements ScopeSelectionStrategy {

    /**
     * Select scope.
     *
     * @param architecture the architecture
     * @param pattern the pattern
     * @return the scope
     */
    @Override
    public Scope selectScope(Architecture architecture, Patterns pattern) {
        Scope scope = new Scope();
        int architectureSize = architecture.getElements().size();
        if (architectureSize >= 1) {
            Random random = new Random();
            int howManyToSelect = random.nextInt(architectureSize) + 1;
            List<Integer> selectedIndexes = new ArrayList<>();
            for (int i = 0; i < howManyToSelect; i++) {
                int randomIndex;
                do {
                    randomIndex = random.nextInt(architectureSize);
                } while (selectedIndexes.contains(randomIndex));
                selectedIndexes.add(randomIndex);
                scope.getElements().add(architecture.getElements().get(randomIndex));
            }
        }
        return scope;
    }

}
