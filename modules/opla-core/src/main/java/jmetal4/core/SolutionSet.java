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

package jmetal4.core;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.LogLog;
import jmetal4.metrics.MetricsEvaluation;
import jmetal4.util.Configuration;
import results.FunResults;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * Class representing a SolutionSet (a set of solutions)
 */
public class SolutionSet implements Serializable {
	
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
     * Constructor. Creates an unbounded solution set.
     */
    public SolutionSet() {
        solutionsList_ = new ArrayList<Solution>();
    } // SolutionSet

    /**
     * Creates a empty solutionSet with a maximum capacity.
     *
     * @param maximumSize Maximum size.
     */
    public SolutionSet(int maximumSize) {
        solutionsList_ = new ArrayList<Solution>();
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

    /**
     * Returns the ith solution in the set.
     *
     * @param i Position of the solution to obtain.
     * @return The <code>Solution</code> at the position i.
     * @throws IndexOutOfBoundsException.
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
     * Writes the objective function values of the <code>Solution</code> objects
     * into the set in a file.
     *
     * @param path The output file name
     */
    public void printObjectivesToFile(String path) {
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
        /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            for (int i = 0; i < solutionsList_.size(); i++) {
                // if (this.vector[i].getFitness()<1.0) {
                bw.write(solutionsList_.get(i).toString().trim().replaceAll(" ", ", ")); // returns something
                // like this: 744.0
                // 6.142857142857143
                bw.newLine();

                // }
            }

	    /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printObjectivesToFile

    public void printObjectivesTempToFile(String path) {
        try {
        /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            for (int i = 0; i < solutionsList_.size(); i++) {
                // if (this.vector[i].getFitness()<1.0) {
                bw.write(solutionsList_.get(i).toStringObjectivesTemp());
                bw.newLine();
                // }
            }

	    /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printObjectivesToFile

    /**
     * Writes the decision variable values of the <code>Solution</code>
     * solutions objects into the set in a file.
     *
     * @param path The output file name
     */
    public void printVariablesToFile(String path) {
        try {
	    /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
            for (int i = 0; i < solutionsList_.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    bw.write(((Architecture) solutionsList_.get(i).getDecisionVariables()[j]).getName());
                    // bw.write(((Architecture)
                    // solutionsList_.get(i).getDecisionVariables()[j]).getNumber());
                    // bw.write(solutionsList_.get(i).getDecisionVariables()[j].toString()
                    // + " ");
                }
                bw.newLine();
            }

	    /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printVariablesToFile

    // added by Thelma october/2012
    public void saveVariablesToFile(String path, List<FunResults> funResults, LogLog logger, boolean generate) {
        int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;

        if (logger != null)
            logger.putLog("Number of solutions: " + solutionsList_.size(), Level.INFO);
        for (int i = 0; i < solutionsList_.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutionsList_.get(i).getDecisionVariables()[j];
                String pathToSave = path;
                funResults.get(i).setSolution_name(pathToSave + arch.getName() + "-" + funResults.get(i).getId());
                if (generate)
                    arch.save(arch, pathToSave, "-" + funResults.get(i).getId());
            }
        }
    }

    public void printInformationToFile(String path) {
        try {
	    /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Number of solutions: " + solutionsList_.size());
            int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
            for (int i = 0; i < solutionsList_.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    bw.newLine();
                    bw.write("--------  NEW SOLUTION ------------------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Solution: " + i);
                    bw.newLine();
                    Architecture arch = (Architecture) solutionsList_.get(i).getDecisionVariables()[j];

                    for (Concern concern : arch.getAllConcerns()) {
                        bw.newLine();
                        bw.write("Concern: " + concern.getName());
                    }

                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of Packages: " + arch.getAllPackages().size());
                    bw.newLine();

                    bw.newLine();
                    bw.write("Number of variabilities: " + arch.getAllVariabilities().size());

                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of interfaces: " + arch.getAllInterfaces().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of classes: " + arch.getAllClasses().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of DependencyInterElementRelationships: "
                            + arch.getRelationshipHolder().getAllDependencies().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of AbstractionInterElementRelationships: "
                            + arch.getRelationshipHolder().getAllAbstractions().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of GeneralizationsRelationships: "
                            + arch.getRelationshipHolder().getAllGeneralizations().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of AssociationsRelationships: "
                            + arch.getRelationshipHolder().getAllAssociationsRelationships().size());
                    bw.newLine();
                    bw.write("Number of AssociationClasses: "
                            + arch.getRelationshipHolder().getAllAssociationsClass().size());
                    bw.newLine();
                }
            }

	    /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printInformationToFile

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

    /**
     * Copies the objectives of the solution set to a matrix
     *
     * @return A matrix containing the objectives
     */
    public double[][] writeObjectivesToMatrix() {
    	LOGGER.info("writeObjectivesToMatrix()");
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
    } // writeObjectivesMatrix

    public void printTimeToFile(String path, int run, long time[], String pla) {
        try {
	    /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("--------  " + pla + " ------------------");
            bw.newLine();
            bw.newLine();
            for (int i = 0; i < run; i++) {
                bw.write("" + time[i]);
                bw.newLine();
            }

	    /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printTimeToFile

    public void printMetricsToFile(String path) {
        MetricsEvaluation metrics = new MetricsEvaluation();

        try {
	    /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Number of solutions: " + solutionsList_.size());
            int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;

            for (int i = 0; i < solutionsList_.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    bw.newLine();
                    bw.write("-----------------------------------  NEW SOLUTION -----------------------------------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Solution: " + i);
                    bw.newLine();
                    Architecture arch = (Architecture) solutionsList_.get(i).getDecisionVariables()[j];
                    bw.newLine();
                    bw.newLine();

                    bw.write("--------  ELEGANCE ------------------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("ELEGANCE: " + metrics.evaluateElegance(arch));
                    bw.newLine();
                    bw.write("NAC - ELEGANCE: " + metrics.evaluateNACElegance(arch));
                    bw.newLine();
                    bw.write("ATMR - ELEGANCE: " + metrics.evaluateATMRElegance(arch));
                    bw.newLine();
                    bw.write("EC - ELEGANCE: " + metrics.evaluateECElegance(arch));
                    bw.newLine();
                    bw.newLine();

                    bw.write("--------  PLA EXTENSIBILITY -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("PLA EXTENSIBILITY: " + metrics.evaluatePLAExtensibility(arch));
                    bw.newLine();

                    bw.write("-----  FEATURE-DRIVEN METRICS ----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("MSI AGGREGATION: " + metrics.evaluateMSIFitness(arch));
                    bw.newLine();
                    bw.write("CDAC: " + metrics.evaluateCDAC(arch));
                    bw.newLine();
                    bw.write("CDAI: " + metrics.evaluateCDAI(arch));
                    bw.newLine();
                    bw.write("CDAO: " + metrics.evaluateCDAO(arch));
                    bw.newLine();
                    bw.write("CIBC: " + metrics.evaluateCIBC(arch));
                    bw.newLine();
                    bw.write("IIBC: " + metrics.evaluateIIBC(arch));
                    bw.newLine();
                    bw.write("OOBC: " + metrics.evaluateOOBC(arch));
                    bw.newLine();
                    bw.write("LCC: " + metrics.evaluateLCC(arch));
                    bw.newLine();
                    bw.write("LCCClass: " + metrics.evaluateLCCClass(arch));
                    bw.newLine();
                    bw.write("CDAClass: " + metrics.evaluateCDAClass(arch));
                    bw.newLine();
                    bw.write("CIBClass: " + metrics.evaluateCIBClass(arch));
                    bw.newLine();
                    bw.newLine();

                    bw.write("------  CONVENTIONAL METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("MAC AGGREGATION: " + metrics.evaluateMACFitness(arch));
                    bw.newLine();
                    bw.write("Cohesion: " + metrics.evaluateCohesion(arch));
                    bw.newLine();
                    bw.write("Mean Dep Comps: " + metrics.evaluateMeanDepComps(arch));
                    bw.newLine();
                    bw.write("Mean Num Ops: " + metrics.evaluateMeanNumOps(arch));
                    bw.newLine();
                    bw.write("Sum Classes DepIn: " + metrics.evaluateSumClassesDepIn(arch));
                    bw.newLine();
                    bw.write("Sum Classes DepOut: " + metrics.evaluateSumClassesDepOut(arch));
                    bw.newLine();
                    bw.write("Sum DepIn: " + metrics.evaluateSumDepIn(arch));
                    bw.newLine();
                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
                    bw.newLine();
                    bw.newLine();

                    bw.write("------  ACOMP METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Sum DepIn: "
                            + metrics.evaluateSumDepIn(arch));
                    bw.newLine();
                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
                    bw.newLine();
                    bw.newLine();


                    bw.write("------  ACLASS METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Sum DepIn: "
                            + metrics.evaluateSumDepIn(arch));
                    bw.newLine();
                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
                    bw.newLine();
                    bw.newLine();


                    bw.write("------  TAM METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Mean Num OPs: " + metrics.evaluateMeanNumOps(arch));
                    bw.newLine();
                    bw.newLine();


                    bw.write("------  COE METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Relational Cohesion: " + metrics.evaluateCohesion(arch));
                    bw.newLine();
                    bw.write("Lcc: " + metrics.evaluateLCC(arch));
                    bw.newLine();
                    bw.newLine();


                    bw.write("------  DC METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("CDAC: " + metrics.evaluateCDAC(arch));
                    bw.newLine();
                    bw.write("CDAI: " + metrics.evaluateCDAI(arch));
                    bw.newLine();
                    bw.write("CDAO: " + metrics.evaluateCDAO(arch));
                    bw.newLine();

                    bw.newLine();

                    bw.write("------  EC METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("CIBC: " + metrics.evaluateCIBC(arch));
                    bw.newLine();
                    bw.write("IIBC: " + metrics.evaluateIIBC(arch));
                    bw.newLine();
                    bw.write("OOBC: " + metrics.evaluateOOBC(arch));
                    bw.newLine();

                    bw.newLine();
                }
            }

	    /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printMetricsToFile

    public void printAllMetricsToFile(String path) {
        try {
	    /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
            bw.write("MSI    MAC     Extensibility       Elegance");
            bw.newLine();
            MetricsEvaluation metrics = new MetricsEvaluation();

            for (int i = 0; i < solutionsList_.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    Architecture arch = (Architecture) solutionsList_.get(i).getDecisionVariables()[j];
                    bw.write(metrics.evaluateMSIFitness(arch) + "  " + metrics.evaluateMACFitness(arch) + "  "
                            + metrics.evaluatePLAExtensibility(arch) + "  " + metrics.evaluateElegance(arch));
                    // bw.write(solutionsList_.get(i).toString());
                    bw.newLine();
                }
            }

	    /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printAllMetricsToFile

    public void printDiscardedSolutionsToFile(int[] discardedSolutions, String path) {
        try {
	    /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.newLine();
            bw.write("Number of Discarded Solutions");

            int runs = discardedSolutions.length;
            for (int i = 0; i < runs; i++) {
                bw.newLine();
                bw.write("Run " + i + ":  " + discardedSolutions[i]);
                bw.newLine();
            }
	    /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printDiscardedSolutionsToFile

    public List<Solution> getSolutionSet() {
        return this.solutionsList_;
    }

} // SolutionSet

