//  SolutionSet.Java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package br.otimizes.oplatool.core.jmetal4.core;

import br.otimizes.oplatool.common.Configuration;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * Class representing a SolutionSet (a set of solutions)
 */
public class SolutionSet implements Serializable, Iterable<Solution> {

    private static final Logger LOGGER = Logger.getLogger(SolutionSet.class);

    /**
     *
     */
    private static final long serialVersionUID = 2100295237257916377L;

    /**
     * Stores a list of <code>solution</code> objects.
     */
    protected List<Solution> solutionsList_;

    /**
     * Maximum size of the solution set
     */
    private int capacity_ = 0;

    /**
     * Stores a Solution with the maximum values for each objective.
     */
    private double [] objectiveMax;

    /**
     * Constructor. Creates an unbounded solution set.
     */
    public SolutionSet() {
        solutionsList_ = new ArrayList<>();
    } // SolutionSet

    /**
     * Creates a empty solutionSet with a maximum capacity.
     *
     * @param maximumSize Maximum size.
     */
    public SolutionSet(int maximumSize) {
        solutionsList_ = new ArrayList<>();
        capacity_ = maximumSize;
    } // SolutionSet

    /**
     * Inserts a new solution into the SolutionSet.
     *
     * @param solution The <code>Solution</code> to store
     * @return True If the <code>Solution</code> has been inserted, false
     * otherwise.
     */
    public boolean add(Solution solution) {
        if (solutionsList_.size() == capacity_) {
            Configuration.logger_.severe("The population is full");
            Configuration.logger_.severe("Capacity is : " + capacity_);
            Configuration.logger_.severe("\t Size is: " + this.size());
            return false;
        } // if

        solutionsList_.add(solution);

        return true;
    } // add

    public int getCapacity() {
        return capacity_;
    }

    public void setCapacity(int capacity_) {
        this.capacity_ = capacity_;
    }

    /**
     * Returns the ith solution in the set.
     *
     * @param i Position of the solution to obtain.
     * @return The <code>Solution</code> at the position i.
     * @throws IndexOutOfBoundsException
     */
    public Solution get(int i) {
        if (i >= solutionsList_.size()) {
            LOGGER.warn("Index out of Bound " + i);
            throw new IndexOutOfBoundsException("Index out of Bound " + i);
        }
        return solutionsList_.get(i);
    } // get

    /**
     * Returns the maximum capacity of the solution set
     *
     * @return The maximum capacity of the solution set
     */
    public int getMaxSize() {
        return capacity_;
    } // getMaxSize

    /**
     * Sorts a SolutionSet using a <code>Comparator</code>.
     *
     * @param comparator <code>Comparator</code> used to sort.
     */
    public void sort(Comparator<Solution> comparator) {
        if (comparator == null) {
            Configuration.logger_.severe("No criterium for compare exist");
            return;
        } // if
        Collections.sort(solutionsList_, comparator);
    } // sort

    /**
     * Returns the index of the best Solution using a <code>Comparator</code>.
     * If there are more than one occurrences, only the index of the first one
     * is returned
     *
     * @param comparator <code>Comparator</code> used to compare solutions.
     * @return The index of the best Solution attending to the comparator or
     * <code>-1<code> if the SolutionSet is empty
     */
    public int indexBest(Comparator<Solution> comparator) {

        if ((solutionsList_ == null) || (this.solutionsList_.isEmpty())) {
            return -1;
        }

        int index = 0;
        Solution bestKnown = solutionsList_.get(0), candidateSolution;
        int flag;
        for (int i = 1; i < solutionsList_.size(); i++) {
            candidateSolution = solutionsList_.get(i);
            flag = comparator.compare(bestKnown, candidateSolution);
            if (flag == -1) {
                index = i;
                bestKnown = candidateSolution;
            }
        }

        return index;

    } // indexBest

    /**
     * Returns the best Solution using a <code>Comparator</code>. If there are
     * more than one occurrences, only the first one is returned
     *
     * @param comparator <code>Comparator</code> used to compare solutions.
     * @return The best Solution attending to the comparator or <code>null<code>
     * if the SolutionSet is empty
     */
    public Solution best(Comparator<Solution> comparator) {
        int indexBest = indexBest(comparator);
        if (indexBest < 0) {
            return null;
        } else {
            return solutionsList_.get(indexBest);
        }

    } // best

    /**
     * Returns the index of the worst Solution using a <code>Comparator</code>.
     * If there are more than one occurrences, only the index of the first one
     * is returned
     *
     * @param comparator <code>Comparator</code> used to compare solutions.
     * @return The index of the worst Solution attending to the comparator or
     * <code>-1<code> if the SolutionSet is empty
     */
    public int indexWorst(Comparator<Solution> comparator) {

        if ((solutionsList_ == null) || (this.solutionsList_.isEmpty())) {
            return -1;
        }

        int index = 0;
        Solution worstKnown = solutionsList_.get(0), candidateSolution;
        int flag;
        for (int i = 1; i < solutionsList_.size(); i++) {
            candidateSolution = solutionsList_.get(i);
            flag = comparator.compare(worstKnown, candidateSolution);
            if (flag == -1) {
                index = i;
                worstKnown = candidateSolution;
            }
        }

        return index;

    } // indexWorst

    /**
     * Returns the worst Solution using a <code>Comparator</code>. If there are
     * more than one occurrences, only the first one is returned
     *
     * @param comparator <code>Comparator</code> used to compare solutions.
     * @return The worst Solution attending to the comparator or
     * <code>null<code>
     * if the SolutionSet is empty
     */
    public Solution worst(Comparator<Solution> comparator) {

        int index = indexWorst(comparator);
        if (index < 0) {
            return null;
        } else {
            return solutionsList_.get(index);
        }

    } // worst

    /**
     * Returns the number of solutions in the SolutionSet.
     *
     * @return The size of the SolutionSet.
     */
    public int size() {
        return solutionsList_.size();
    } // size

    /**
     * Empties the SolutionSet
     */
    public void clear() {
        solutionsList_.clear();
    } // clear

    /**
     * Deletes the <code>Solution</code> at position i in the set.
     *
     * @param i The position of the solution to remove.
     */
    public void remove(int i) {
        if (i > solutionsList_.size() - 1) {
            Configuration.logger_.severe("Size is: " + this.size());
        } // if
        solutionsList_.remove(i);
    } // remove

    /**
     * Returns an <code>Iterator</code> to access to the solution set list.
     *
     * @return the <code>Iterator</code>.
     */
    public Iterator<Solution> iterator() {
        return solutionsList_.iterator();
    } // iterator

    /**
     * Returns a new <code>SolutionSet</code> which is the result of the union
     * between the current solution set and the one passed as a parameter.
     *
     * @param solutionSet SolutionSet to join with the current solutionSet.
     * @return The result of the union operation.
     */
    public SolutionSet union(SolutionSet solutionSet) {
        // Check the correct size. In development
        int newSize = this.size() + solutionSet.size();
        if (newSize < capacity_)
            newSize = capacity_;

        // Create a new population
        SolutionSet union = new SolutionSet(newSize);
        for (int i = 0; i < this.size(); i++) {
            union.add(this.get(i));
        } // for

        for (int i = this.size(); i < (this.size() + solutionSet.size()); i++) {
            union.add(solutionSet.get(i - this.size()));
        } // for

        return union;
    } // union

    /**
     * Replaces a solution by a new one
     *
     * @param position The position of the solution to replace
     * @param solution The new solution
     */
    public void replace(int position, Solution solution) {
        if (position > this.solutionsList_.size()) {
            solutionsList_.add(solution);
        } // if
        solutionsList_.remove(position);
        solutionsList_.add(position, solution);
    } // replace

    public List<Solution> getSolutionSet() {
        return this.solutionsList_;
    }

    public void setSolutionSet(List<Solution> solutionsList_) {
        this.solutionsList_ = solutionsList_;
    }

    /**
     * Copies the objectives of the solution set to a matrix
     *
     * @return A matrix containing the objectives
     */
    public double[][] writeObjectivesToMatrix() {
        if (this.size() == 0) {
            return null;
        }
        double[][] objectives;
        objectives = new double[size()][get(0).numberOfObjectives()];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < get(0).numberOfObjectives(); j++) {
                objectives[i][j] = get(i).getObjective(j);
            }
        }
        return objectives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolutionSet that = (SolutionSet) o;
        return capacity_ == that.capacity_ && Objects.equals(solutionsList_, that.solutionsList_);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solutionsList_, capacity_);
    }
}
