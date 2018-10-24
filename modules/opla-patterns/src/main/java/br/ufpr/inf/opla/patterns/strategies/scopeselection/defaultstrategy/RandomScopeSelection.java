package br.ufpr.inf.opla.patterns.strategies.scopeselection.defaultstrategy;

import arquitetura.representation.Architecture;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.ScopeSelectionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomScopeSelection implements ScopeSelectionStrategy {

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
