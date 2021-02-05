package br.otimizes.oplatool.core.learning;

import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Subjective analyze algorithm by Freire and Bindewald (2020)
 */
public class SubjectiveAnalyzeAlgorithm {
    //https://stackoverflow.com/questions/28694971/using-neural-network-class-in-weka-in-java-code
    private static final long serialVersionUID = 1L;
    public static final Logger LOGGER = Logger.getLogger(SubjectiveAnalyzeAlgorithm.class);

    private OPLASolutionSet resultFront;
    private ClassifierAlgorithm algorithm;
    private int numObjectives;
    private ArffExecution subjectiveArffExecution;
    private MultilayerPerceptron subjectiveMLP;
    private ArffExecution architecturalArffExecution;
    private MultilayerPerceptron architecturalMLP;
    private int trainingTime = 2500;
    private DistributeUserEvaluation distributeUserEvaluation = DistributeUserEvaluation.ALL;
    private EvaluationModels evaluationModel = EvaluationModels.CROSS_VALIDATION;
    private double momentum = 0.2;
    private double learningRate = 0.3;
    private String hiddenLayers;
    private List<Element> evaluatedElements;
    private List<OPLASolutionSet> interactions = new ArrayList<>();
    private boolean trained = false;
    public static int currentEvaluation = 0;
    Evaluation subjectiveEval;
    Evaluation architectureEval;
    List<Element> freezedElements = new ArrayList<>();
    List<Element> notFreezedElements = new ArrayList<>();

    public SubjectiveAnalyzeAlgorithm() {
    }

    public SubjectiveAnalyzeAlgorithm(OPLASolutionSet resultFront, ClassifierAlgorithm algorithm, DistributeUserEvaluation distributeUserEvaluation) {
        this.distributeUserEvaluation = distributeUserEvaluation;
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.subjectiveArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(), resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(), resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
        this.numObjectives = this.resultFront.getSolutionSet().get(0).numberOfObjectives();
    }

    public SubjectiveAnalyzeAlgorithm(OPLASolutionSet resultFront, ClassifierAlgorithm algorithm) {
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.subjectiveArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(), resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(), resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
        this.numObjectives = this.resultFront.getSolutionSet().get(0).numberOfObjectives();
    }

    private void distributeUserEvaluations(OPLASolutionSet resultFront) {
        LOGGER.info("distributeUserEvaluations");
        if (ClassifierAlgorithm.CLUSTERING_MLP.equals(this.algorithm)) {
            resultFront.distributeUserEvaluation(distributeUserEvaluation);
        }
    }

    /**
     * Execution Method
     *
     * @return Solution Set - Best performing cluster with another solutions (filteredSolutions)
     * @throws Exception Default Exception
     */
    public SolutionSet run(OPLASolutionSet solutionSet, boolean middle) {
        try {
            return MLP(solutionSet, middle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() throws Exception {
        run(null, false);
        for (OPLASolutionSet interaction : interactions) {
            run(interaction, false);
        }
    }

    /**
     * MLP Execution Method
     *
     * @return Solution Set
     * @throws Exception Default Exception
     */
    public SolutionSet MLP(OPLASolutionSet solutionSet, boolean inOnmiddle) {
        currentEvaluation++;
        LOGGER.info("MLP()");
        long startsIn = new Date().getTime();
        if (subjectiveMLP == null) {
            LOGGER.info("MLP() Start objects");
            subjectiveMLP = newSubjectivelyMLP();
            architecturalMLP = newArchitecturalMLP();
        }

        if (solutionSet != null) {
            joinSolutionSet(solutionSet, inOnmiddle);
        }
        setArffExecutionClassIndex(subjectiveArffExecution);
        setArffExecutionClassIndex(architecturalArffExecution);
        startAlgorithms(inOnmiddle);

        LOGGER.info("::: Time: " + ((new Date().getTime() - startsIn) / 1000));
        return resultFront.getSolutionSet();
    }


    private MultilayerPerceptron newSubjectivelyMLP() {
        MultilayerPerceptron subjectiveMLP = new MultilayerPerceptron();
        subjectiveMLP.setHiddenLayers(getHiddenLayers());
        subjectiveMLP.setTrainingTime(getTrainingTime());
        subjectiveMLP.setLearningRate(getLearningRate());
        subjectiveMLP.setMomentum(getMomentum());
        return subjectiveMLP;
    }

    private MultilayerPerceptron newArchitecturalMLP() {
        MultilayerPerceptron architecturalMLP = new MultilayerPerceptron();
        architecturalMLP.setHiddenLayers(getArchitecturalMLPHiddenLayers());
        architecturalMLP.setTrainingTime(getTrainingTime());
        architecturalMLP.setLearningRate(getLearningRate());
        architecturalMLP.setMomentum(getMomentum());
        return architecturalMLP;
    }

    private void joinSolutionSet(OPLASolutionSet solutionSet, boolean inOnmiddle) {
        if (!solutionSet.hasUserEvaluation()) {
            LOGGER.info("MLP() donthasUserEvaluation");
            evaluateSolutionSetSubjectiveMLP(solutionSet);
        } else {
            LOGGER.info("MLP() hasUserEvaluation");
            distributeUserEvaluations(solutionSet);
        }
        if (!inOnmiddle) {
            ArffExecution newArffArchitectureMLP = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(), resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
            if (newArffArchitectureMLP.getData() != null) {
                newArffArchitectureMLP.getData().setClassIndex(newArffArchitectureMLP.getAttrIndices());
                if (architecturalArffExecution.getData() == null) architecturalArffExecution = newArffArchitectureMLP;
                else architecturalArffExecution.getData().addAll(newArffArchitectureMLP.getData());
            }
        }
        ArffExecution newArffSubjectiveMLP = new ArffExecution(solutionSet.writeObjectivesAndElementsNumberToMatrix(), solutionSet.writeUserEvaluationsToMatrix(), null);
        newArffSubjectiveMLP.getData().setClassIndex(newArffSubjectiveMLP.getAttrIndices());
        resultFront.getSolutionSet().getSolutionSet().addAll(solutionSet.getSolutionSet().getSolutionSet());
        subjectiveArffExecution.getData().addAll(newArffSubjectiveMLP.getData());
    }

    private void setArffExecutionClassIndex(ArffExecution subjectiveArffExecution) {
        if (subjectiveArffExecution.getData() != null)
            subjectiveArffExecution.getData().setClassIndex(subjectiveArffExecution.getAttrIndices());
    }


    private void startAlgorithms(boolean inOnMiddle) {
        Thread threadSubjectiveMLP = new Thread(this::buildSubjectiveMLP);
        threadSubjectiveMLP.start();
        Thread threadArchitecturalMLP = null;
        if (!inOnMiddle) {
            threadArchitecturalMLP = new Thread(this::buildArchitecturalMLP);
            threadArchitecturalMLP.start();
        }

        try {
            threadSubjectiveMLP.join();
            if (threadArchitecturalMLP != null) {
                threadArchitecturalMLP.join();
                logMachineLearningModel();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void logMachineLearningModel() {
        if (architecturalArffExecution.getData() == null) return;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + "LogMachineLearningModel_" + currentEvaluation + ".arff");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(architecturalArffExecution.getData().toString());
        printWriter.close();
    }

    public void evaluateSolutionSetSubjectiveMLP(OPLASolutionSet solutionSet) {
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            try {
                solutionSet.getSolutionSet().get(i).setEvaluation((int) subjectiveMLP.classifyInstance(new DenseInstance(1.0, solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i])));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Evaluate subjectively using Likert score and architectural elements evaluation
     *
     * @param solutionSet solution set
     * @param subjective  assign or not the Likert score
     * @throws Exception default exception
     */
    public void evaluateSolutionSetSubjectiveAndArchitecturalMLP(OPLASolutionSet solutionSet, boolean subjective) throws Exception {
        LOGGER.info("evaluateSolutionSetSubjectiveAndArchitecturalMLP()");
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            Solution solution = solutionSet.getSolutionSet().get(i);
            double[] solutionMatrix = solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i];
            if (subjective) {
                solution.setEvaluation((int) subjectiveMLP.classifyInstance(new DenseInstance(1.0, solutionMatrix)));
            }
            for (Element element : solution.getAlternativeArchitecture().getElementsWithPackages()) {
                double[] data = getDataSet(solutionSet, solution, element);
                if (architecturalArffExecution.getData() != null) {
                    DenseInstance denseInstance = new DenseInstance(1.0, data);
                    denseInstance.setDataset(architecturalArffExecution.getData());
                    element.setFreezeFromDM(architecturalMLP.classifyInstance(denseInstance));
                    LOGGER.info(element.getName() + ":" + element.getTypeElement() + " was " + (element.isFreezeByDM() ? " FREEZED" : " NOT FREEZED"));
                    if (element.isFreezeByDM()) {
                        LOGGER.info("::: Freezed " + element.getName());
                        this.freezedElements.add(element);
                    } else {
                        this.notFreezedElements.add(element);
                    }
                }
            }
        }
    }

    private double[] getDataSet(OPLASolutionSet solutionSet, Solution solution, Element element) {
        double[] data = solution.getObjectives();
        double[] objectives = solutionSet.writeObjectiveFromElementsAndObjectives(element, solution);
        double[] characteristics = solutionSet.writeCharacteristicsFromElement(element, solution);

        data = ArrayUtils.addAll(data, characteristics);
        data = ArrayUtils.addAll(data, objectives);
        data = ArrayUtils.addAll(data, new double[]{
                solution.containsArchitecturalEvaluation() ? 1 : 0,
                element.isFreezeByDM() ? 1 : 0
        });
        return data;
    }

    private void buildArchitecturalMLP() {
        if (architecturalArffExecution.getData() == null) return;
        LOGGER.info("MLP() buildArchitecturalMLP()");
        try {
            Instances data = architecturalArffExecution.getData();
            int trainSize = Math.toIntExact(Math.round(data.numInstances() * getRatioTest()));
            int testSize = data.numInstances() - trainSize;
            Instances train = new Instances(data, 0, trainSize);
            Instances test = new Instances(data, trainSize, testSize);

            architecturalMLP.buildClassifier(train);
            LOGGER.info("MLP() Evaluation Architectural");
            architectureEval = new Evaluation(test);
            switch (evaluationModel) {
                case TRAINING_SET:
                    architectureEval.evaluateModel(architecturalMLP, test);
                    break;
                case CROSS_VALIDATION:
                    architectureEval.crossValidateModel(architecturalMLP, test, 10, new Random(1));
                    break;
            }
            logArchitectureModelEvaluation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Double getRatioTest() {
        return 0.66;
    }

    private void logArchitectureModelEvaluation() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("::: Architectural Evaluation ::: \n");
        stringBuilder.append("Architecture Error: ").append(architectureEval.errorRate()).append("\n");
        stringBuilder.append("Architecture Summary: ").append(architectureEval.toSummaryString()).append("\n");
        LOGGER.info(stringBuilder.toString());

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + "LogMachineLearningModelEvaluation_" + currentEvaluation + ".arff");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(stringBuilder);
        printWriter.close();
    }

    /**
     * Build the MLP
     */
    private void buildSubjectiveMLP() {
        if (subjectiveArffExecution.getData() == null) return;
        LOGGER.info("MLP() buildSubjectiveMLP()");
        try {
            subjectiveMLP.buildClassifier(subjectiveArffExecution.getData());
            LOGGER.info("MLP() Evaluation Subjective");
            subjectiveEval = new Evaluation(subjectiveArffExecution.getData());
            switch (evaluationModel) {
                case TRAINING_SET:
                    subjectiveEval.evaluateModel(subjectiveMLP, subjectiveArffExecution.getData());
                    break;
                case CROSS_VALIDATION:
                    subjectiveEval.crossValidateModel(subjectiveMLP, subjectiveArffExecution.getData(), 5, new Random(1));
                    break;
            }
            logSubjectiveModelEvaluation();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logSubjectiveModelEvaluation() {
        LOGGER.info("::: Subjective Evaluation :::");
        LOGGER.info("Subjective Error: " + subjectiveEval.errorRate());
        LOGGER.info("Subjective Summary: " + subjectiveEval.toSummaryString());
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public OPLASolutionSet getResultFront() {
        return resultFront;
    }

    public void setResultFront(OPLASolutionSet resultFront) {
        this.resultFront = resultFront;
    }

    public ClassifierAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(ClassifierAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public ArffExecution getSubjectiveArffExecution() {
        return subjectiveArffExecution;
    }

    public void setSubjectiveArffExecution(ArffExecution subjectiveArffExecution) {
        this.subjectiveArffExecution = subjectiveArffExecution;
    }

    public int getNumObjectives() {
        return numObjectives;
    }

    public void setNumObjectives(int numObjectives) {
        this.numObjectives = numObjectives;
    }


    public MultilayerPerceptron getSubjectiveMLP() {
        return subjectiveMLP;
    }

    public void setSubjectiveMLP(MultilayerPerceptron subjectiveMLP) {
        this.subjectiveMLP = subjectiveMLP;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }

    public DistributeUserEvaluation getDistributeUserEvaluation() {
        return distributeUserEvaluation;
    }

    public void setDistributeUserEvaluation(DistributeUserEvaluation distributeUserEvaluation) {
        this.distributeUserEvaluation = distributeUserEvaluation;
    }

    public EvaluationModels getEvaluationModel() {
        return evaluationModel;
    }

    public void setEvaluationModel(EvaluationModels evaluationModel) {
        this.evaluationModel = evaluationModel;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public String getHiddenLayers() {
        return hiddenLayers == null ? String.valueOf(Math.round(subjectiveArffExecution.getAttrIndices())) : hiddenLayers;
    }

    public String getArchitecturalMLPHiddenLayers() {
        return hiddenLayers == null ? String.valueOf(Math.round(architecturalArffExecution.getAttrIndices())) : hiddenLayers;
    }

    public void setHiddenLayers(String hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    public ArffExecution getArchitecturalArffExecution() {
        return architecturalArffExecution;
    }

    public void setArchitecturalArffExecution(ArffExecution architecturalArffExecution) {
        this.architecturalArffExecution = architecturalArffExecution;
    }

    public List<Element> getEvaluatedElements() {
        return evaluatedElements;
    }

    public void setEvaluatedElements(List<Element> evaluatedElements) {
        this.evaluatedElements = evaluatedElements;
    }

    public void addInteraction(OPLASolutionSet offspringPopulation) {
        interactions.add(offspringPopulation);
    }

    public MultilayerPerceptron getArchitecturalMLP() {
        return architecturalMLP;
    }

    public void setArchitecturalMLP(MultilayerPerceptron architecturalMLP) {
        this.architecturalMLP = architecturalMLP;
    }

    public List<OPLASolutionSet> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<OPLASolutionSet> interactions) {
        this.interactions = interactions;
    }

    public boolean isTrained() {
        return trained;
    }

    public void setTrained(boolean trained) {
        this.trained = trained;
    }

    public Evaluation getSubjectiveEval() {
        return subjectiveEval;
    }

    public void setSubjectiveEval(Evaluation subjectiveEval) {
        this.subjectiveEval = subjectiveEval;
    }

    public Evaluation getArchitectureEval() {
        return architectureEval;
    }

    public void setArchitectureEval(Evaluation architectureEval) {
        this.architectureEval = architectureEval;
    }

    public List<Element> getFreezedElements() {
        return freezedElements;
    }

    public void setFreezedElements(List<Element> freezedElements) {
        this.freezedElements = freezedElements;
    }

    public List<Element> getNotFreezedElements() {
        return notFreezedElements;
    }

    public void setNotFreezedElements(List<Element> notFreezedElements) {
        this.notFreezedElements = notFreezedElements;
    }
}
