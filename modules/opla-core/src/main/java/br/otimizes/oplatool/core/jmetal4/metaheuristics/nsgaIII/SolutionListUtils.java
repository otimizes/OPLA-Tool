package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;

import java.util.List;

import br.otimizes.oplatool.core.jmetal4.core.Solution;

/**
 * @author Antonio J. Nebro
 */
public class SolutionListUtils {
    public static List<Solution> getNonDominatedSolutions(List<Solution> solutionList) {
        NonDominatedSolutionListArchive nonDominatedSolutionArchive = new NonDominatedSolutionListArchive();
        solutionList.forEach(nonDominatedSolutionArchive::add);

        return nonDominatedSolutionArchive.solutions();
    }
}
