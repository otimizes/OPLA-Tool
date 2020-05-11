package br.ufpr.dinf.gres.core.jmetal4.core;

import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;
import br.ufpr.dinf.gres.common.Configuration;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import br.ufpr.dinf.gres.core.learning.ArchitecturalElementType;
import br.ufpr.dinf.gres.core.learning.DistributeUserEvaluation;
import br.ufpr.dinf.gres.domain.entity.Info;
import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.LogLog;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class OPLASolutionSet {

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
     * @return A matrix containing the objectives
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
     * @return A matrix containing the objectives
     */
    public double[][] writeObjectivesAndArchitecturalElementsNumberToMatrix() {
        double[][] doubles = reduceTreeDimensionalArray(getArchitecturalSolutionsEvaluated().stream()
                .map(this::writeObjectiveWithAllElementsFromSolution).toArray(double[][][]::new));
        return doubles;
    }

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

    public double[] generateSolutionFromElementsAndGetDoubles(Element element, Solution solution) throws ClassNotFoundException {
        Solution newSolution = new Solution(solution.getProblem());
        ((OPLA) newSolution.getProblem()).getLOGGER().setLevel(org.apache.log4j.Level.OFF);
        newSolution.getAlternativeArchitecture().getLOGGER().setLevel(org.apache.log4j.Level.OFF);
        Architecture architecture = new Architecture("agm");
        architecture.addElement(element);
        newSolution.setDecisionVariables(new Architecture[]{architecture});
        ((OPLA) newSolution.getProblem()).evaluate(newSolution);
        try {
            ((OPLA) newSolution.getProblem()).evaluateConstraints(newSolution);
        } catch (JMException e) {
            e.printStackTrace();
        }
        ((OPLA) newSolution.getProblem()).getLOGGER().setLevel(org.apache.log4j.Level.ALL);
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
        double[][] doubles = getArchitecturalSolutionsEvaluated().stream().map(solution -> {
            List<Element> allElementsFromSolution = getAllElementsFromSolution(solution);
            double[] objects = allElementsFromSolution.stream().mapToDouble(element -> element.isFreezeByDM() ? 1.0 : 0.0).toArray();
            return objects;
        }).toArray(double[][]::new);
        return reduceBiDimensionalArray(doubles);
    }

    public double[][] reduceTreeDimensionalArray(double[][][] treeDimensionalArray) {
        if (treeDimensionalArray.length <= 0) return new double[][]{};
        double[][] twoDimensionalArray = treeDimensionalArray[0];
        for (int i = 1; i < treeDimensionalArray.length; i++) {
            twoDimensionalArray = (double[][]) ArrayUtils.addAll(twoDimensionalArray, treeDimensionalArray[i]);
        }
        return twoDimensionalArray;
    }

    public double[] reduceBiDimensionalArray(double[][] biDimensionalArray) {
        if (biDimensionalArray.length <= 0) return new double[]{};
        double[] oneDimensionalArray = biDimensionalArray[0];
        for (int i = 1; i < biDimensionalArray.length; i++) {
            oneDimensionalArray = (double[]) ArrayUtils.addAll(oneDimensionalArray, biDimensionalArray[i]);
        }
        return oneDimensionalArray;
    }

    public double[][] writeAllElementsFromSolution(Solution solution) {
        List<Element> allElementsFromSolution = getAllElementsFromSolution(solution);
        double[][] elements = allElementsFromSolution.stream().map(s -> this.writeCharacteristicsFromElement(s, solution)).toArray(double[][]::new);
        return elements;
    }

    public double[] writeCharacteristicsFromElement(Element element, Solution solution) {
        double[] elm = new double[6];
        elm[0] = element.getNumberId();
        elm[1] = ArchitecturalElementType.getTypeId(element.getTypeElement());
        elm[2] = element instanceof Package ? (double) ((Package) element).getAllClasses().size() : 0;
        elm[3] = element instanceof Package ? (double) ((Package) element).getAllInterfaces().size() : 0;
        elm[4] = element instanceof Class ? (double) ((Class) element).getAllAttributes().size() : 0;
        elm[5] = element instanceof Class ? (double) ((Class) element).getAllMethods().size() :
                element instanceof Interface ? (double) ((Interface) element).getMethods().size() : 0;
        try {
            double[] doubles = generateSolutionFromElementsAndGetDoubles(element, solution);
            elm = ArrayUtils.addAll(elm, doubles);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        elm = ArrayUtils.addAll(elm, new double[]{
                solution.containsArchitecturalEvaluation() ? 1 : 0
        });

        return elm;
    }

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
     * Create Create a list from objectives. Used to create CSV Files
     *
     * @param interaction Interaction Number
     * @return List of objectives
     */
    public String toStringObjectives(String interaction) {
        return Arrays.stream(solutionSet.writeObjectivesToMatrix()).map(p -> Arrays.asList(ArrayUtils.toObject(p)).toString().replace("]", interaction + "," + interaction + "\n").replace(",", "|").replace("[", interaction + "," + interaction + ",").replaceAll("\\.0", "").replaceAll(" ", "")).collect(Collectors.joining());
    }

    public double[][] writeObjectivesAndElementsNumberEvaluationToMatrix() {
        double[][] doubles = writeObjectivesAndElementsNumberToMatrix();
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Arrays.copyOf(doubles[i], doubles[i].length + 1);
            doubles[i][doubles[i].length - 1] = solutionSet.getSolutionSet().get(i).getEvaluation();
        }
        return doubles;
    } // writeObjectivesAndElementsNumberToMatrix

    public double[] writeUserEvaluationsToMatrix() {
        double[] doubles = new double[solutionSet.solutionsList_.size()];
        for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
            doubles[i] = (double) solutionSet.solutionsList_.get(i).getEvaluation();
        }
        return doubles;
    }

    public boolean hasUserEvaluation() {
        double[] doubles = writeUserEvaluationsToMatrix();
        for (double aDouble : doubles) {
            if (aDouble > 0) return true;
        }
        return false;
    }

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

    public int getMedia(Set<Integer> valores) {
        if (valores == null) return 0;
        valores = valores.stream().filter(v -> v > 0).collect(Collectors.toSet());
        if (valores.size() == 0) return 0;
        if (valores.size() == 1) return valores.stream().findFirst().get();
        int soma = 0;
        for (Integer valore : valores) {
            soma += valore;
        }
        return soma / valores.size();
    }


    public List<Solution> getArchitecturalSolutionsEvaluated() {
        return solutionSet.getSolutionSet().stream().filter(Solution::containsArchitecturalEvaluation).collect(Collectors.toList());
    }

    public List<Element> getArchitecturalElementsEvaluatedByClusterId(Double clusterId) {
        List<Element> elements = new ArrayList<>();
        List<List<Element>> collect = getArchitecturalSolutionsEvaluated().stream().filter(solution -> clusterId.equals(solution.getClusterId()))
                .map(solution -> solution.getAlternativeArchitecture().getElementsWithPackages().stream().filter(Element::isFreezeByDM).collect(Collectors.toList())).collect(Collectors.toList());
        for (List<Element> elementList : collect) {
            elements.addAll(elementList);
        }
        return elements;
    }

    public List<Solution> getSolutionWithArchitecturalElementsEvaluatedByClusterId(Double clusterId) {
        return getArchitecturalSolutionsEvaluated().stream().filter(solution -> clusterId.equals(solution.getClusterId())).collect(Collectors.toList());
    }

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

    private Solution freezeArchitecturalElementsAccordingCluster(Solution solution) {
        if (!solution.containsArchitecturalEvaluation()) {
            List<Solution> solutionWithArchitecturalElementsEvaluatedByClusterId = getSolutionWithArchitecturalElementsEvaluatedByClusterId(solution.getClusterId());
            if (solutionWithArchitecturalElementsEvaluatedByClusterId.size() > 0) {
                solution = solutionWithArchitecturalElementsEvaluatedByClusterId.get(0);
            }
        }
        return solution;
    }

    private void freezeArchitecturalElementsAccordingSolution(Solution solution) {
        List<Element> evaluatedElements = solution.getAlternativeArchitecture().getFreezedElements();
        if (evaluatedElements.size() > 0) {
            for (Solution sol : solutionSet.getSolutionSet()) {
                List<Element> collect = sol.getAlternativeArchitecture().getElementsWithPackages().stream()
                        .filter(e -> evaluatedElements.stream().anyMatch(ee -> ee.totalyEquals(e))).collect(Collectors.toList());
                if (collect.size() > 0) {
                    for (Element element : collect) {
                        System.out.println("Freeze Architectural Element By Cluster: " + element.getName() + ":" + element.getTypeElement());
                        element.setFreezedByCluster();
                    }
                }
            }
        }
    }

    public List<Element> findElementWithNumberId(Double id) {
        List<List<Element>> collect = solutionSet.getSolutionSet().stream().map(s -> s.getAlternativeArchitecture().findElementByNumberId(id)).collect(Collectors.toList());
        List<Element> objects = new ArrayList<>();
        for (List<Element> elements : collect) {
            objects.addAll(elements);
        }
        return objects;
    }

    public void saveVariableToFile(Solution solution, String path, Logger logger, boolean generate) {
        int numberOfVariables = solution.getDecisionVariables().length;

        for (int j = 0; j < numberOfVariables; j++) {
            Architecture arch = (Architecture) solution.getDecisionVariables()[j];
            arch.setName(solution.getAlternativeArchitecture().getName());
            if (generate)
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
    } // printObjectivesToFile

    public void printObjectivesTempToFile(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            for (int i = 0; i < solutionSet.solutionsList_.size(); i++) {
                bw.write(solutionSet.solutionsList_.get(i).toStringObjectivesTemp());
                bw.newLine();
            }

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

    public void saveVariablesToFile(String path, List<Info> funResults, LogLog logger, boolean generate) {
        int numberOfVariables = solutionSet.solutionsList_.get(0).getDecisionVariables().length;

        SaveStringToFile.getInstance().createLogDir();
        String logPath = ReaderConfig.getDirExportTarget() + "/Logs/linkHypervolume.txt";

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
                    SaveStringToFile.getInstance().appendStrToFile(logPath,"\n" + pathToSave +arch.getName() + funResults.get(i).getId() + "\t" + solutionSet.solutionsList_.get(i).toString());
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
}
