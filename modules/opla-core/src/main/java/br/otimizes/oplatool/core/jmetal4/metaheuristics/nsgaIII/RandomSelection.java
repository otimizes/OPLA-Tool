package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.operators.selection.Selection;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.Map;

public class RandomSelection extends Selection {
    public RandomSelection(Map<String, Object> parameters) {
        super(parameters);
    }

    /**
     * Performs the operation
     *
     * @param object Object representing a SolutionSet.
     * @return an object representing an array with the selected parents
     */
    public Object execute(Object object) {
        SolutionSet population = (SolutionSet) object;
        int pos1, pos2;
        pos1 = PseudoRandom.randInt(0, population.size() - 1);
        pos2 = PseudoRandom.randInt(0, population.size() - 1);
        while ((pos1 == pos2) && (population.size() > 1)) {
            pos2 = PseudoRandom.randInt(0, population.size() - 1);
        }

        Solution[] parents = new Solution[2];
        parents[0] = population.get(pos1);
        parents[1] = population.get(pos2);

        return parents;
    } // Execute
} // RandomSelection
