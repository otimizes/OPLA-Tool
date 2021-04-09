package br.otimizes.oplatool.core.jmetal4.core;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;
import br.otimizes.oplatool.core.learning.ArchitecturalElementType;
import br.otimizes.oplatool.core.learning.DistributeUserEvaluation;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import br.otimizes.oplatool.domain.entity.Info;
import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.LogLog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.ArrayUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * OPLA extension of solution set, implement all new methods here
 */
public class OPLASolutionSet {

    @JsonIgnore
    private SolutionSet solutionSet;

    public OPLASolutionSet(SolutionSet solutionSet) {
        this.solutionSet = solutionSet;
    }

    public SolutionSet getSolutionSet() {
        return solutionSet;
    }

    public void setSolutionSet(SolutionSet solutionSet) {
        this.solutionSet = solutionSet;
    }

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAgragations, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return matrix containing the objectives
     */
    public double[][] writeObjectivesAndElementsNumberToMatrix() {
        double[][] doubles = solutionSet.writeObjectivesToMatrix();
        for (int i = 0; i < doubles.length; i++) {
            int length = doubles[i].length;
            double[] doublesObj = new double[length + 4 + 3];
            if (doubles[i].length >= 0) System.arraycopy(doubles[i], 0, doublesObj, 0, doubles[i].length);
            doublesObj[length] = solutionSet.get(i).getAlternativeArchitecture().getAllClasses().size();
            doublesObj[length + 1] = solutionSet.get(i).getAlternativeArchitecture().getAllConcerns().size();
            doublesObj[length + 2] = solutionSet.get(i).getAlternativeArchitecture().getAllInterfaces().size();
            doublesObj[length + 3] = solutionSet.get(i).getAlternativeArchitecture().getAllPackages().size();

            doublesObj[length + 4] = solutionSet.get(i).getAlternativeArchitecture().getAllVariationPoints().size();
            doublesObj[length + 5] = solutionSet.get(i).getAlternativeArchitecture().getAllVariants().size();
            doublesObj[length + 6] = solutionSet.get(i).getAlternativeArchitecture().getAllVariabilities().size();
            doubles[i] = doublesObj;
        }
        return doubles;
    } // writeObjectivesAndElementsNumberToMatrix

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAgragations, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return matrix containing the objectives
     */
    public double[][] writeObjectivesAndArchitecturalElementsNumberToMatrix() {
        double[][] doubles = reduceThreeDimensionalArray(getSolutionsWithArchitecturalEvaluations().stream()
                .map(this::writeObjectiveWithAllElementsFromSolution).toArray(double[][][]::new));
        return doubles;
    }

    /**
     * Copies the objectives and All Elements of a specific set to a matrix
     *
     * @param solution specific solution
     * @return Matrix with values
     */
    private double[][] writeObjectiveWithAllElementsFromSolution(Solution solution) {
        double[] objectives = solution.getObjectives();
        double[][] values = writeAllElementsFromSolution(solution);
        double[][] newValues = new double[values.length][];
        int i = 0;
        for (double[] value : values) {
            double[] newArray = new double[objectives.length + value.length];
            System.arraycopy(objectives, 0, newArray, 0, objectives.length);
            System.arraycopy(value, 0, newArray, objectives.length, value.length);
            newValues[i] = newArray;
            i++;
        }
        return newValues;
    }

    /**
     * Generate the Solution from elements and get the objective values
     *
     * @param element  specific element to add in solution
     * @param solution specific solution
     * @return list of objectives
     * @throws ClassNotFoundException Exception on create the new solution by param
     */
    public double[] writeObjectiveFromElementsAndObjectives(Element element, Solution solution) {
        Solution newSolution = null;
        try {
            newSolution = new Solution(solution.getProblem());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        OPLA.getLOGGER().setLevel(org.apache.log4j.Level.OFF);
        newSolution.getAlternativeArchitecture().getLOGGER().setLevel(org.apache.log4j.Level.OFF);
        Architecture architecture = new Architecture("agm");
        architecture.addElement(element);
        newSolution.setDecisionVariables(new Architecture[]{architecture});
        ((OPLA) newSolution.getProblem()).evaluate(newSolution);
        try {
            newSolution.getProblem().evaluateConstraints(newSolution);
        } catch (JMException e) {
            e.printStackTrace();
        }
        OPLA.getLOGGER().setLevel(org.apache.log4j.Level.ALL);
        newSolution.getAlternativeArchitecture().getLOGGER().setLevel(org.apache.log4j.Level.ALL);
        return newSolution.getObjectives();
    }

    /**
     * Copies the objectives and Elements Number of the solution set to a matrix
     * Objectives, nrClasses, nrConcerns, nrInterfaces, nrPackages, nrVariationPoints, nrVariants, nrVariabilities, nrConcerns,
     * nrAbstractions, nrAgragations, nrAssociations, nrCompositions, nrDependencies, nrGeneralizations, nrRealizations, nrUsage
     *
     * @return A matrix containing the objectives
     */
    public double[] writeArchitecturalEvaluationsToMatrix() {
        double[][] doubles = getSolutionsWithArchitecturalEvaluations().stream().map(solution -> {
            List<Element> allElementsFromSolution = getAllElementsFromSolution(solution);
            double[] objects = allElementsFromSolution.stream().mapToDouble(element -> element.isFreezeByDM() ? 1.0 : 0.0).toArray();
            return objects;
        }).toArray(double[][]::new);
        return reduceBiDimensionalArray(doubles);
    }

    /**
     * Reduce one dimensional in three dimensional array
     *
     * @param treeDimensionalArray array of objectives
     * @return bi-dimensional array of objectives
     */
    public double[][] reduceThreeDimensionalArray(double[][][] treeDimensionalArray) {
        if (treeDimensionalArray.length <= 0) return new double[][]{};
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
        if (biDimensionalArray.length <= 0) return new double[]{};
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
        double[][] elements = allElementsFromSolution.stream().map(s -> this.writeCharacteristicsFromElement(s, solution)).toArray(double[][]::new);
        return elements;
    }

    /**
     * Get characteristics from element in a solution (number id, element type, nr of classes, interfaces, attrs and methods, objectives, user evaluation)
     *
     * @param element  specific in a solution
     * @param solution specific solution
     * @return array of characteristics
     */
    public double[] writeCharacteristicsFromElement(Element element, Solution solution) {
        double[] elm = new double[6];
        elm[0] = element.getNumberId();
        elm[1] = ArchitecturalElementType.getTypeId(element.getTypeElement());
        elm[2] = element instanceof Package ? (double) ((Package) element).getAllClasses().size() : 0;
        elm[3] = element instanceof Package ? (double) ((Package) element).getAllInterfaces().size() : 0;
        elm[4] = element instanceof Class ? (double) ((Class) element).getAllAttributes().size() : 0;
        elm[5] = element instanceof Class ? (double) ((Class) element).getAllMethods().size() :
                element instanceof Interface ? (double) ((Interface) element).getMethods().size() : 0;
        double[] doubles = writeObjectiveFromElementsAndObjectives(element, solution);
        elm = ArrayUtils.addAll(elm, doubles);
        elm = ArrayUtils.addAll(elm, new double[]{
                solution.containsArchitecturalEvaluation() ? 1 : 0
        });

        return elm;
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
        return Arrays.stream(solutionSet.writeObjectivesToMatrix()).map(p -> Arrays.asList(ArrayUtils.toObject(p)).toString().replace("]", interaction + "," + interaction + "\n").replace(",", "|").replace("[", interaction + "," + interaction + ",").replaceAll("\\.0", "").replaceAll(" ", "")).collect(Collectors.joining());
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
            doubles[i][doubles[i].length - 1] = solutionSet.getSolutionSet().get(i).getEvaluation();
        }
        return doubles;
    } // writeObjectivesAndElementsNumberToMatrix

    /**
     * Get user evaluations list
     *
     * @return array of user evaluations
     */
    public double[] writeUserEvaluationsToMatrix() {
        double[] doubles = new double[solutionSet.solutionsList_.size()];
        for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
            doubles[i] = solutionSet.solutionsList_.get(i).getEvaluation();
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
            if (aDouble > 0) return true;
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
        for (Solution solution : solutionSet.solutionsList_) {
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
        if (values == null) return 0;
        values = values.stream().filter(v -> v > 0).collect(Collectors.toSet());
        if (values.size() == 0) return 0;
        if (values.size() == 1) return values.stream().findFirst().get();
        int soma = 0;
        for (Integer valore : values) {
            soma += valore;
        }
        return soma / values.size();
    }

    /**
     * Get solutions that have architectural evaluations
     *
     * @return solutions with architectural evaluations
     */
    public List<Solution> getSolutionsWithArchitecturalEvaluations() {
        return solutionSet.getSolutionSet().stream().filter(Solution::containsArchitecturalEvaluation).collect(Collectors.toList());
    }

    /**
     * Get architectural elements evaluated in a cluster
     *
     * @param clusterId cluster id
     * @return list of elements
     */
    public List<Element> getArchitecturalElementsEvaluatedByClusterId(Double clusterId) {
        List<Element> elements = new ArrayList<>();
        List<List<Element>> collect = getSolutionsWithArchitecturalEvaluations().stream().filter(solution -> clusterId.equals(solution.getClusterId()))
                .map(solution -> solution.getAlternativeArchitecture().getElementsWithPackages().stream().filter(Element::isFreezeByDM).collect(Collectors.toList())).collect(Collectors.toList());
        for (List<Element> elementList : collect) {
            elements.addAll(elementList);
        }
        return elements;
    }

    /**
     * Get solutions with architectural elements evaluated in a cluster
     *
     * @param clusterId cluster id
     * @return list of solutions with architectural elements
     */
    public List<Solution> getSolutionWithArchitecturalElementsEvaluatedByClusterId(Double clusterId) {
        return getSolutionsWithArchitecturalEvaluations().stream().filter(solution -> clusterId.equals(solution.getClusterId())).collect(Collectors.toList());
    }

    /**
     * Generalize the evaluatios in a cluster (see approaches in Bindewald, 2020)
     *
     * @param distributeUserEvaluation Approach
     */
    public void distributeUserEvaluation(DistributeUserEvaluation distributeUserEvaluation) {
        if (DistributeUserEvaluation.NONE.equals(distributeUserEvaluation)) return;
        Map<Double, Set<Integer>> clusterIds = getClusterIds();
        if (hasUserEvaluation() && clusterIds.size() > 0) {
            List<Solution> solutionsList_ = solutionSet.solutionsList_;
            if (DistributeUserEvaluation.MIDDLE.equals(distributeUserEvaluation))
                solutionsList_ = solutionsList_.subList(0, Math.abs(solutionsList_.size() / 2));
            for (int i = 0; i < solutionsList_.size(); i++) {
                Solution solution = solutionsList_.get(i);
                if (solution.getEvaluation() == 0) {
                    int media = Math.abs(getMedia(clusterIds.get(solution.getClusterId())));
                    solution.setEvaluation(media);
                }
            }
        }
    }

    /**
     * freeze solutions according the clusters in a solution
     *
     * @param solution specific solution
     * @return solution with elements
     */
    private Solution freezeArchitecturalElementsAccordingCluster(Solution solution) {
        if (!solution.containsArchitecturalEvaluation()) {
            List<Solution> solutionWithArchitecturalElementsEvaluatedByClusterId = getSolutionWithArchitecturalElementsEvaluatedByClusterId(solution.getClusterId());
            if (solutionWithArchitecturalElementsEvaluatedByClusterId.size() > 0) {
                solution = solutionWithArchitecturalElementsEvaluatedByClusterId.get(0);
            }
        }
        return solution;
    }

    /**
     * Freeze the architectural elements according the solution
     *
     * @param solution solution with elements
     */
    private void freezeArchitecturalElementsAccordingSolution(Solution solution) {
        List<Element> evaluatedElements = solution.getAlternativeArchitecture().getFreezedElements();
        if (evaluatedElements.size() > 0) {
            for (Solution sol : solutionSet.getSolutionSet()) {
                List<Element> collect = sol.getAlternativeArchitecture().getElementsWithPackages().stream()
                        .filter(e -> evaluatedElements.stream().anyMatch(ee -> ee.totalyEquals(e))).collect(Collectors.toList());
                if (collect.size() > 0) {
                    for (Element element : collect) {
                        element.setFreezedByCluster();
                    }
                }
            }
        }
    }

    /**
     * Find elements with a id
     *
     * @param id hash id
     * @return filtered elements
     */
    public List<Element> findElementWithNumberId(Double id) {
        List<List<Element>> collect = solutionSet.getSolutionSet().stream().map(s -> s.getAlternativeArchitecture().findElementByNumberId(id)).collect(Collectors.toList());
        List<Element> objects = new ArrayList<>();
        for (List<Element> elements : collect) {
            objects.addAll(elements);
        }
        return objects;
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
    } // printObjectivesToFile

    private void printObjectivesWithHypervolumeToFile(String pathNorm) {
        String path = pathNorm.replace("txt", "hypervolume");
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            String executionId = solutionSet.get(0).getExecutionId();
            for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
                bw.write(Arrays.toString(getNormalizedSolution(i)).trim().replaceAll("]", "").replaceAll("\\[", "").replaceAll(", ", "\t")); // returns something
                bw.newLine();
                if (executionId != null && !executionId.equals(solutionSet.get(i).getExecutionId())) {
                    executionId = solutionSet.get(i).getExecutionId();
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
            String executionId = solutionSet.get(0).getExecutionId();
            for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
                bw.write(Arrays.toString(getNormalizedSolution(i)).trim().replaceAll("]", "").replaceAll("\\[", "").replaceAll(", ", "\t")); // returns something
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
            QualityIndicator qualityIndicator = new QualityIndicator(solutionSet.get(0).getProblem(), pathInd.replace("txt", "normalized"));
            bw.write("HV:" + qualityIndicator.getHypervolume(solutionSet));
            bw.newLine();
            bw.write("EPSILON:" + qualityIndicator.getEpsilon(solutionSet));
            bw.newLine();
            bw.write("IGD:" + qualityIndicator.getIGD(solutionSet));
            bw.newLine();
            bw.write("SPREAD:" + qualityIndicator.getSpread(solutionSet));
            bw.newLine();
            bw.write("GD:" + qualityIndicator.getGD(solutionSet));
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

            for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
                bw.write(solutionSet.solutionsList_.get(i).toString().trim().replaceAll(" ", ", ")); // returns something
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

            int numberOfVariables = solutionSet.solutionsList_.get(0).getDecisionVariables().length;
            for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    bw.write(((Architecture) solutionSet.solutionsList_.get(i).getDecisionVariables()[j]).getName());
                }
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printVariablesToFile

    /**
     * Save variables in a file
     *
     * @param path       path
     * @param funResults information of each solution
     * @param logger     logger
     * @param generate   log without save
     */
    public void saveVariablesToFile(String path, List<Info> funResults, LogLog logger, boolean generate) {
        int numberOfVariables = solutionSet.solutionsList_.get(0).getDecisionVariables().length;

        SaveStringToFile.getInstance().createLogDir();
        String logPath = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + "logs" + FileConstants.FILE_SEPARATOR + "link_fitness.txt";

        if (logger != null)
            logger.putLog("Number of solutions: " + solutionSet.solutionsList_.size(), Level.INFO);
        for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutionSet.solutionsList_.get(i).getDecisionVariables()[j];
                String pathToSave = path;
                String originalName = ((OPLA) solutionSet.solutionsList_.get(i).getProblem()).getArchitecture_().getName();
                funResults.get(i).setName(pathToSave + originalName);
                if (generate) {
                    arch.save(arch, pathToSave, "-" + funResults.get(i).getId());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n" + pathToSave + arch.getName() + funResults.get(i).getId() + "\t" + solutionSet.solutionsList_.get(i).toString());
                }
            }
        }
    }

    public void saveVariablesToFile(String path) {

        int numberOfVariables = solutionSet.solutionsList_.get(0).getDecisionVariables().length;
        System.out.println("Number of solutions: " + solutionSet.solutionsList_.size());
        for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutionSet.solutionsList_.get(i).getDecisionVariables()[j];
                String pathToSave = path;
                arch.save(arch, pathToSave, String.valueOf(i));
            }
        }
    }

    public double[] getNormalizedSolution(int i) {
        Solution solution = solutionSet.get(i);
        Solution max = getMax();
        Solution min = getMin();
        double[] doubles = new double[solution.getObjectives().length];
        if (solutionSet.size() == 1) return doubles;
        for (int j = 0; j < solution.getObjectives().length; j++) {
            doubles[j] = (max.getObjective(j) - min.getObjective(j)) == 0 ? 0 :
                    (solution.getObjective(j) - min.getObjective(j)) / (max.getObjective(j) - min.getObjective(j));
            if (doubles[j] == -0.0) doubles[j] = 0.0;
        }
        return doubles;
    }

    public Solution getMin() {
        Solution solution = solutionSet.get(0);
        for (int i = 0; i < solution.getObjectives().length; i++) {
            for (Solution otherSolution : solutionSet.getSolutionSet()) {
                if (otherSolution.getObjective(i) <= solution.getObjective(i)) {
                    solution = otherSolution;
                }
            }
        }
        return solution;
    }

    public Solution getMax() {
        Solution solution = solutionSet.get(0);
        for (int i = 0; i < solution.getObjectives().length; i++) {
            for (Solution otherSolution : solutionSet.getSolutionSet()) {
                if (otherSolution.getObjective(i) >= solution.getObjective(i)) {
                    solution = otherSolution;
                }
            }
        }
        return solution;
    }

    public Solution get(int i) {
        return this.getSolutionSet().get(i);
    }

    public List<Solution> getSolutions() {
        return getSolutionSet().solutionsList_;
    }

    public void setSolutions(List<Solution> solutions) {
        solutionSet.setCapacity(solutions.size());
        solutionSet.setSolutionSet(solutions);
    }
}
