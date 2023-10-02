package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.util.archive.Archive;
import br.otimizes.oplatool.core.jmetal4.util.comparators.DominanceComparator;
import br.otimizes.oplatool.core.jmetal4.util.comparators.EqualSolutions;

/**
 * This class implements an archive containing non-dominated solutions
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
public class NonDominatedSolutionListArchive {

    private List<Solution> solutionList;
    private Comparator<Solution> dominanceComparator;
    private Comparator<Solution> equalSolutions = new EqualSolutions();

    /**
     * Constructor
     */
    public NonDominatedSolutionListArchive() {
        this(new DominanceComparator());
    }

    /**
     * Constructor
     */
    public NonDominatedSolutionListArchive(Comparator<Solution> comparator) {
        dominanceComparator = comparator;

        solutionList = new ArrayList<>();
    }

    /**
     * Inserts a solution in the list
     *
     * @param solution The solution to be inserted.
     * @return true if the operation success, and false if the solution is dominated
     *         or if an
     *         identical individual exists. The decision variables can be null if
     *         the solution is read from a
     *         file; in that case, the domination tests are omitted
     */
    public boolean add(Solution solution) {
        boolean isSolutionInserted = false;
        if (solutionList.isEmpty()) {
            solutionList.add(solution);
            isSolutionInserted = true;
        } else {
            isSolutionInserted = insertSolutionIfNonDominatedAndIsNotInTheArchive(solution,
                    isSolutionInserted);
        }

        return isSolutionInserted;
    }

    private boolean insertSolutionIfNonDominatedAndIsNotInTheArchive(Solution solution,
            boolean solutionInserted) {
        boolean isDominated = false;
        boolean isContained = false;
        Iterator<Solution> iterator = solutionList.iterator();
        while (((!isDominated) && (!isContained)) && (iterator.hasNext())) {
            Solution listIndividual = iterator.next();
            int flag = dominanceComparator.compare(solution, listIndividual);
            if (flag == -1) {
                iterator.remove();
            } else if (flag == 1) {
                isDominated = true; // dominated by one in the list
            } else if (equalSolutions.compare(solution, listIndividual) == 0) {// solutions are equals
                isContained = true;
            }
        }

        if (!isDominated && !isContained) {
            solutionList.add(solution);
            solutionInserted = true;
        }
        return solutionInserted;
    }

    public NonDominatedSolutionListArchive join(Archive archive) {
        return this.addAll(archive.getSolutionSet());
    }

    public NonDominatedSolutionListArchive addAll(List<Solution> list) {
        for (Solution solution : list) {
            this.add(solution);
        }

        return this;
    }

    public List<Solution> solutions() {
        return solutionList;
    }

    public int size() {
        return solutionList.size();
    }

    public Solution get(int index) {
        return solutionList.get(index);
    }
}
