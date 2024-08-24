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

import br.otimizes.isearchai.learning.ml.MLSolutionSet;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import br.otimizes.oplatool.domain.entity.Info;
import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.LogLog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing a SolutionSet (a set of solutions)
 */
public class SolutionSet extends MLSolutionSet<Solution, Element> implements Serializable, Iterable<Solution> {

    private static final Logger LOGGER = Logger.getLogger(SolutionSet.class);

    /**
     *
     */
    private static final long serialVersionUID = 2100295237257916377L;

    /**
     * Maximum size of the solution set
     */
    private int capacity_ = 0;

    /**
     * Stores a Solution with the maximum values for each objective.
     */
    private double[] objectiveMax;

    /**
     * Constructor. Creates an unbounded solution set.
     */
    public SolutionSet() {
        solutions = new ArrayList<>();
    }

    // SolutionSet

    /**
     * Creates a empty solutionSet with a maximum capacity.
     *
     * @param maximumSize Maximum size.
     */
    public SolutionSet(int maximumSize) {
        solutions = new ArrayList<>();
        capacity_ = maximumSize;
    }

    // SolutionSet
    public SolutionSet(SolutionSet resultFront) {
        this.capacity_ = resultFront.capacity_;
        this.solutions = resultFront.solutions;
    }

    /**
     * Inserts a new solution into the SolutionSet.
     *
     * @param solution The <code>Solution</code> to store
     * @return True If the <code>Solution</code> has been inserted, false
     * otherwise.
     */
    public boolean add(Solution solution) {
        if (solutions.size() == capacity_) {
            Configuration.logger_.severe("The population is full");
            Configuration.logger_.severe("Capacity is : " + capacity_);
            Configuration.logger_.severe("\t Size is: " + this.size());
            throw new RuntimeException();
            //            return false;
        }
        // if
        solutions.add(solution);
        return true;
    }

    // add
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
        if (i >= solutions.size()) {
            LOGGER.warn("Index out of Bound " + i);
            throw new IndexOutOfBoundsException("Index out of Bound " + i);
        }
        return solutions.get(i);
    }

    // get

    /**
     * Returns the maximum capacity of the solution set
     *
     * @return The maximum capacity of the solution set
     */
    public int getMaxSize() {
        return capacity_;
    }

    // getMaxSize

    /**
     * Sorts a SolutionSet using a <code>Comparator</code>.
     *
     * @param comparator <code>Comparator</code> used to sort.
     */
    public void sort(Comparator<Solution> comparator) {
        if (comparator == null) {
            Configuration.logger_.severe("No criterium for compare exist");
            return;
        }
        // if
        Collections.sort(solutions, comparator);
    }

    // sort

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
        if ((solutions == null) || (this.solutions.isEmpty())) {
            return -1;
        }
        int index = 0;
        Solution bestKnown = solutions.get(0), candidateSolution;
        int flag;
        for (int i = 1; i < solutions.size(); i++) {
            candidateSolution = solutions.get(i);
            flag = comparator.compare(bestKnown, candidateSolution);
            if (flag == -1) {
                index = i;
                bestKnown = candidateSolution;
            }
        }
        return index;
    }

    // indexBest

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
            return solutions.get(indexBest);
        }
    }

    // best

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
        if ((solutions == null) || (this.solutions.isEmpty())) {
            return -1;
        }
        int index = 0;
        Solution worstKnown = solutions.get(0), candidateSolution;
        int flag;
        for (int i = 1; i < solutions.size(); i++) {
            candidateSolution = solutions.get(i);
            flag = comparator.compare(worstKnown, candidateSolution);
            if (flag == -1) {
                index = i;
                worstKnown = candidateSolution;
            }
        }
        return index;
    }

    // indexWorst

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
            return solutions.get(index);
        }
    }

    // worst

    /**
     * Returns the number of solutions in the SolutionSet.
     *
     * @return The size of the SolutionSet.
     */
    public int size() {
        return solutions.size();
    }

    // size

    /**
     * Empties the SolutionSet
     */
    public void clear() {
        solutions.clear();
    }

    // clear

    /**
     * Deletes the <code>Solution</code> at position i in the set.
     *
     * @param i The position of the solution to remove.
     */
    public void remove(int i) {
        if (i > solutions.size() - 1) {
            Configuration.logger_.severe("Size is: " + this.size());
        }
        // if
        solutions.remove(i);
    }

    // remove

    /**
     * Returns an <code>Iterator</code> to access to the solution set list.
     *
     * @return the <code>Iterator</code>.
     */
    public Iterator<Solution> iterator() {
        return solutions.iterator();
    }

    // iterator

    /**
     * Returns a new <code>SolutionSet</code> which is the result of the union
     * between the current solution set and the one passed as a parameter.
     *
     * @param solutionSet SolutionSet to join with the current solutions.
     * @return The result of the union operation.
     */
    public SolutionSet union(SolutionSet solutionSet) {
        // Check the correct size. In development
        int newSize = this.size() + solutions.size();
        if (newSize < capacity_)
            newSize = capacity_;
        // Create a new population
        SolutionSet union = new SolutionSet(newSize);
        for (int i = 0; i < this.size(); i++) {
            union.add(this.get(i));
        }
        // for
        for (int i = this.size(); i < (this.size() + solutions.size()); i++) {
            union.add(solutions.get(i - this.size()));
        }
        // for
        return union;
    }

    // union

    /**
     * Replaces a solution by a new one
     *
     * @param position The position of the solution to replace
     * @param solution The new solution
     */
    public void replace(int position, Solution solution) {
        if (position > this.solutions.size()) {
            solutions.add(solution);
        }
        // if
        solutions.remove(position);
        solutions.add(position, solution);
    }

    // replace
    public List<Solution> getSolutionSet() {
        return this.solutions;
    }

    public void setSolutionSet(List<Solution> solutions) {
        this.solutions = solutions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SolutionSet that = (SolutionSet) o;
        return capacity_ == that.capacity_ && Objects.equals(solutions, that.solutions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solutions, capacity_);
    }

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAggregation, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return matrix containing the objectives
     */
    public double[][] writeObjectivesAndElementsNumberToMatrix() {
        double[][] doubles = writeObjectivesToMatrix();
        for (int i = 0; i < doubles.length; i++) {
            int length = doubles[i].length;
            double[] doublesObj = new double[length + 4 + 3];
            System.arraycopy(doubles[i], 0, doublesObj, 0, doubles[i].length);
            doublesObj[length] = solutions.get(i).getAlternativeArchitecture().getAllClasses().size();
            doublesObj[length + 1] = solutions.get(i).getAlternativeArchitecture().getAllConcerns().size();
            doublesObj[length + 2] = solutions.get(i).getAlternativeArchitecture().getAllInterfaces().size();
            doublesObj[length + 3] = solutions.get(i).getAlternativeArchitecture().getAllPackages().size();
            doublesObj[length + 4] = solutions.get(i).getAlternativeArchitecture().getAllVariationPoints().size();
            doublesObj[length + 5] = solutions.get(i).getAlternativeArchitecture().getAllVariants().size();
            doublesObj[length + 6] = solutions.get(i).getAlternativeArchitecture().getAllVariabilities().size();
            doubles[i] = doublesObj;
        }
        return doubles;
    }

    /**
     * Reduce one dimensional in three dimensional array
     *
     * @param treeDimensionalArray array of objectives
     * @return bi-dimensional array of objectives
     */
    public double[][] reduceThreeDimensionalArray(double[][][] treeDimensionalArray) {
        if (treeDimensionalArray.length <= 0)
            return new double[][]{};
        double[][] twoDimensionalArray = treeDimensionalArray[0];
        for (int i = 1; i < treeDimensionalArray.length; i++) {
            twoDimensionalArray = (double[][]) ArrayUtils.addAll(twoDimensionalArray, treeDimensionalArray[i]);
        }
        return twoDimensionalArray;
    }

    /**
     * Reduce one dimension from bi dimensional array
     *
     * @param biDimensionalArray array of objectives
     * @return one-dimensional array of objectives
     */
    public double[] reduceBiDimensionalArray(double[][] biDimensionalArray) {
        if (biDimensionalArray.length <= 0)
            return new double[]{};
        double[] oneDimensionalArray = biDimensionalArray[0];
        for (int i = 1; i < biDimensionalArray.length; i++) {
            oneDimensionalArray = ArrayUtils.addAll(oneDimensionalArray, biDimensionalArray[i]);
        }
        return oneDimensionalArray;
    }

    /**
     * Get all elements from solutions
     *
     * @param solution specific solution
     * @return array of elements
     */
    public double[][] writeAllElementsFromSolution(Solution solution) {
        List<Element> allElementsFromSolution = getAllElementsFromSolution(solution);
        return allElementsFromSolution.stream().map(s -> this.writeCharacteristicsFromElement(s, solution)).toArray(double[][]::new);
    }

    /**
     * Get characteristics from element in a solution (number id, element type, nr of classes, interfaces, attrs and methods, objectives, user evaluation)
     *
     * @param element  specific in a solution
     * @param solution specific solution
     * @return array of characteristics
     */
    public double[] writeCharacteristicsFromElement(Element element, Solution solution) {
        double[] elementProperties = new double[6];
        elementProperties[0] = element.getNumberId();
        elementProperties[1] = ArchitecturalElementType.getTypeId(element.getTypeElement());
        elementProperties[2] = element instanceof Package ? (double) ((Package) element).getAllClasses().size() : 0;
        elementProperties[3] = element instanceof Package ? (double) ((Package) element).getAllInterfaces().size() : 0;
        elementProperties[4] = element instanceof Class ? (double) ((Class) element).getAllAttributes().size() : 0;
        elementProperties[5] = element instanceof Class ? (double) ((Class) element).getAllMethods().size() : element instanceof Interface ? (double) ((Interface) element).getMethods().size() : 0;
        double[] doubles = writeObjectivesFromElements(element, solution);
        elementProperties = ArrayUtils.addAll(elementProperties, doubles);
        elementProperties = ArrayUtils.addAll(elementProperties, new double[]{solution.containsElementsEvaluation() ? 1 : 0});
        return elementProperties;
    }

    /**
     * Get all elements from solution
     *
     * @param solution specific solution
     * @return list of elements
     */
    public List<Element> getAllElementsFromSolution(Solution solution) {
        List<Element> elements = new ArrayList<>();
        elements.addAll(solution.getAlternativeArchitecture().getAllPackages());
        elements.addAll(solution.getAlternativeArchitecture().getAllClasses());
        elements.addAll(solution.getAlternativeArchitecture().getAllInterfaces());
        return elements;
    }

    /**
     * Method to get a string of objectives and elements number. Used to create CSV files
     *
     * @param startFrom Number of objectives
     * @return List of elements. If startFrom > 0, then specify the objectives number
     */
    public String toStringObjectivesAndElementsNumber(int startFrom) {
        return Arrays.stream(writeObjectivesAndElementsNumberToMatrix()).map(p -> Arrays.asList(ArrayUtils.toObject(Arrays.copyOfRange(p, startFrom, p.length))).toString().replace("]", "\n").replace("[", "").replaceAll("\\.0", "").replaceAll(" ", "")).collect(Collectors.joining());
    }

    /**
     * Create a list from objectives. Used to create CSV Files
     *
     * @param interaction interaction Number
     * @return list of objectives
     */
    public String toStringObjectives(String interaction) {
        return Arrays.stream(writeObjectivesToMatrix()).map(p -> Arrays.asList(ArrayUtils.toObject(p)).toString().replace("]", interaction + "," + interaction + "\n").replace(",", "|").replace("[", interaction + "," + interaction + ",").replaceAll("\\.0", "").replaceAll(" ", "")).collect(Collectors.joining());
    }

    /**
     * Get objectives and elements number with evaluation
     *
     * @return array of objectives with elements and user evaluation
     */
    public double[][] writeObjectivesAndElementsNumberEvaluationToMatrix() {
        double[][] doubles = writeObjectivesAndElementsNumberToMatrix();
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Arrays.copyOf(doubles[i], doubles[i].length + 1);
            doubles[i][doubles[i].length - 1] = solutions.get(i).getEvaluation();
        }
        return doubles;
    }

    /**
     * Get user evaluations list
     *
     * @return array of user evaluations
     */
    public double[] writeUserEvaluationsToMatrix() {
        double[] doubles = new double[solutions.size()];
        for (int i = 0; i < solutions.size(); i++) {
            doubles[i] = solutions.get(i).getEvaluation();
        }
        return doubles;
    }

    /**
     * Verify if has user evaluation
     *
     * @return true if has. false if has'nt
     */
    public boolean hasUserEvaluation() {
        double[] doubles = writeUserEvaluationsToMatrix();
        for (double aDouble : doubles) {
            if (aDouble > 0)
                return true;
        }
        return false;
    }

    /**
     * Get list of cluster ids
     *
     * @return array of cluster ids
     */
    public Map<Double, Set<Integer>> getClusterIds() {
        Map<Double, Set<Integer>> clusters = new HashMap<>();
        for (Solution solution : solutions) {
            if (solution.getClusterId() != null) {
                Set<Integer> clusterId = clusters.getOrDefault(solution.getClusterId(), new HashSet<>());
                clusterId.add(solution.getEvaluation());
                clusters.put(solution.getClusterId(), clusterId);
            }
        }
        return clusters;
    }

    /**
     * Get average of values
     *
     * @param values values
     * @return average of values
     */
    public int getMedia(Set<Integer> values) {
        if (values == null)
            return 0;
        values = values.stream().filter(v -> v > 0).collect(Collectors.toSet());
        if (values.size() == 0)
            return 0;
        if (values.size() == 1)
            return values.stream().findFirst().get();
        int soma = 0;
        for (Integer value : values) {
            soma += value;
        }
        return soma / values.size();
    }

    /**
     * Save solution in a file
     *
     * @param solution specific solution
     * @param path     path
     */
    public void saveVariableToFile(Solution solution, String path) {
        int numberOfVariables = solution.getDecisionVariables().length;
        for (int j = 0; j < numberOfVariables; j++) {
            Architecture arch = (Architecture) solution.getDecisionVariables()[j];
            arch.setName(solution.getAlternativeArchitecture().getName());
            arch.save(arch, path, "");
        }
    }

    /**
     * Writes the objective function values of the <code>Solution</code> objects
     * into the set in a file.
     *
     * @param path The output file name
     */
    public void printObjectivesToFile(String path) {
        printObjectivesWithoutNormalizeToFile(path);
        printObjectivesWithHypervolumeToFile(path);
        printObjectivesNormalizedToFile(path);
        printQualityIndicators(path);
    }

    private void printObjectivesWithHypervolumeToFile(String pathNorm) {
        String path = pathNorm.replace("txt", "hypervolume");
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            String executionId = solutions.get(0).getExecutionId();
            for (int i = 0; i < solutions.size(); i++) {
                bw.write(Arrays.toString(getNormalizedSolution(i)).trim().replaceAll("]", "").replaceAll("\\[", "").replaceAll(", ", "\t"));
                bw.newLine();
                if (executionId != null && !executionId.equals(solutions.get(i).getExecutionId())) {
                    executionId = solutions.get(i).getExecutionId();
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    private void printObjectivesNormalizedToFile(String pathNorm) {
        String path = pathNorm.replace("txt", "normalized");
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            String executionId = solutions.get(0).getExecutionId();
            for (int i = 0; i < solutions.size(); i++) {
                // returns something
                bw.write(Arrays.toString(getNormalizedSolution(i)).trim().replaceAll("]", "").replaceAll("\\[", "").replaceAll(", ", "\t"));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    private void printQualityIndicators(String pathInd) {
        String path = pathInd.replace("txt", "indicators");
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            QualityIndicator qualityIndicator = new QualityIndicator(solutions.get(0).getProblem(), pathInd.replace("txt", "normalized"));
            bw.write("HV:" + qualityIndicator.getHypervolume(this));
            bw.newLine();
            bw.write("EPSILON:" + qualityIndicator.getEpsilon(this));
            bw.newLine();
            bw.write("IGD:" + qualityIndicator.getIGD(this));
            bw.newLine();
            bw.write("SPREAD:" + qualityIndicator.getSpread(this));
            bw.newLine();
            bw.write("GD:" + qualityIndicator.getGD(this));
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    private void printObjectivesWithoutNormalizeToFile(String path) {
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (int i = 0; i < solutions.size(); i++) {
                // returns something
                bw.write(solutions.get(i).toString().trim().replaceAll(" ", ", "));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    /**
     * Writes the decision variable values of the <code>Solution</code>
     * solutions objects into the set in a file.
     *
     * @param path The output file name
     */
    public void printVariablesToFile(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            int numberOfVariables = solutions.get(0).getDecisionVariables().length;
            for (int i = 0; i < solutions.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    bw.write(((Architecture) solutions.get(i).getDecisionVariables()[j]).getName());
                }
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    // printVariablesToFile

    /**
     * Save variables in a file
     *
     * @param path       path
     * @param funResults information of each solution
     * @param logger     logger
     * @param generate   log without save
     */
    public void saveVariablesToFile(String path, List<Info> funResults, LogLog logger, boolean generate) {
        int numberOfVariables = solutions.get(0).getDecisionVariables().length;
        SaveStringToFile.getInstance().createLogDir();
        String logPath = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + "logs" + FileConstants.FILE_SEPARATOR + "link_fitness.txt";
        if (logger != null)
            logger.putLog("Number of solutions: " + solutions.size(), Level.INFO);
        for (int i = 0; i < solutions.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutions.get(i).getDecisionVariables()[j];
                String pathToSave = path;
                String originalName = ((OPLA) solutions.get(i).getProblem()).getArchitecture_().getName();
                funResults.get(i).setName(pathToSave + originalName);
                if (generate) {
                    if (funResults.get(i).getId() == null)
                        funResults.get(i).setId(funResults.get(i).getObjectives().replace("|", "-") + "-" + RandomStringUtils.randomNumeric(3));
                    arch.save(arch, pathToSave, "-" + funResults.get(i).getId());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n" + pathToSave + arch.getName() + funResults.get(i).getId() + "\t" + solutions.get(i).toString());
                }
            }
        }
    }

    public void saveVariablesToFile(String path) {
        int numberOfVariables = solutions.get(0).getDecisionVariables().length;
        System.out.println("Number of solutions: " + solutions.size());
        for (int i = 0; i < solutions.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutions.get(i).getDecisionVariables()[j];
                String pathToSave = path;
                arch.save(arch, pathToSave, String.valueOf(i));
            }
        }
    }

    public double[] getNormalizedSolution(int i) {
        Solution solution = solutions.get(i);
        Solution max = getMax();
        Solution min = getMin();
        double[] doubles = new double[solution.getObjectives().length];
        if (solutions.size() == 1)
            return doubles;
        for (int j = 0; j < solution.getObjectives().length; j++) {
            doubles[j] = (max.getObjective(j) - min.getObjective(j)) == 0 ? 0 : (solution.getObjective(j) - min.getObjective(j)) / (max.getObjective(j) - min.getObjective(j));
            if (doubles[j] == -0.0)
                doubles[j] = 0.0;
        }
        return doubles;
    }

    @JsonIgnore
    public Solution getMin() {
        Solution solution = solutions.get(0);
        for (int i = 0; i < solution.getObjectives().length; i++) {
            for (Solution otherSolution : solutions) {
                if (otherSolution.getObjective(i) <= solution.getObjective(i)) {
                    solution = otherSolution;
                }
            }
        }
        return solution;
    }

    @JsonIgnore
    public Solution getMax() {
        Solution solution = solutions.get(0);
        for (int i = 0; i < solution.getObjectives().length; i++) {
            for (Solution otherSolution : solutions) {
                if (otherSolution.getObjective(i) >= solution.getObjective(i)) {
                    solution = otherSolution;
                }
            }
        }
        return solution;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    @JsonIgnore
    public List<Solution> getSolutionsWithElementsEvaluations() {
        return super.getSolutionsWithElementsEvaluations();
    }

    /**
     * Generate the Solution from elements and get the objective values
     *
     * @param element  specific element to add in solution
     * @param solution specific solution
     * @return list of objectives
     */
    @Override
    public double[] writeObjectivesFromElements(Element element, Solution solution) {
        Solution newSolution = null;
        try {
            newSolution = new Solution((Problem) solution.getProblem());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        OPLA.getLOGGER().setLevel(org.apache.log4j.Level.OFF);
        Architecture.getLOGGER().setLevel(org.apache.log4j.Level.OFF);
        Architecture architecture = new Architecture("pla");
        architecture.addElement(element);
        if (newSolution != null) {
            newSolution.setDecisionVariables(new Architecture[]{architecture});
            ((OPLA) newSolution.getProblem()).evaluate(newSolution);
            try {
                newSolution.getProblem().evaluateConstraints(newSolution);
            } catch (JMException e) {
                e.printStackTrace();
            }
            OPLA.getLOGGER().setLevel(org.apache.log4j.Level.ALL);
            Architecture.getLOGGER().setLevel(org.apache.log4j.Level.ALL);
            return newSolution.getObjectives();
        }
        return null;
    }
}
