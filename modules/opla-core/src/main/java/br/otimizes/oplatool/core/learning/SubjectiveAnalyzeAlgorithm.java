package br.otimizes.oplatool.core.learning;

import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import weka.classifiers.AbstractClassifier;
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
    private ArffExecution scoreArffExecution;
    private AbstractClassifier scoreAlgorithm;
    private Evaluation scoreEvaluation;
    private ArffExecution architecturalArffExecution;
    private AbstractClassifier architecturalAlgorithm;
    private Evaluation architectureEvaluation;
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
    List<Element> freezedElements = new ArrayList<>();
    List<Element> notFreezedElements = new ArrayList<>();

    public SubjectiveAnalyzeAlgorithm() {
    }

    public SubjectiveAnalyzeAlgorithm(OPLASolutionSet resultFront, ClassifierAlgorithm algorithm, DistributeUserEvaluation distributeUserEvaluation) {
        this.distributeUserEvaluation = distributeUserEvaluation;
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.scoreArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(),
                resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(),
                resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
        this.numObjectives = this.resultFront.getSolutionSet().get(0).numberOfObjectives();
    }

    public SubjectiveAnalyzeAlgorithm(OPLASolutionSet resultFront, ClassifierAlgorithm algorithm) {
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.scoreArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(),
                resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(),
                resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
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
     */
    public SolutionSet run(OPLASolutionSet solutionSet, boolean middle) {
        try {
            return getAlgorithm(solutionSet, middle);
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
     * Algorithm Execution Method
     *
     * @return Solution Set
     */
    public SolutionSet getAlgorithm(OPLASolutionSet solutionSet, boolean inOnMiddle) {
        currentEvaluation++;
        LOGGER.info("getAlgorithm()");
        long startsIn = new Date().getTime();
        if (scoreAlgorithm == null) {
            LOGGER.info("getAlgorithm() Start objects");
            scoreAlgorithm = newScoreAlgorithm();
            architecturalAlgorithm = newArchitecturalAlgorithm();
        }

        if (solutionSet != null) {
            joinSolutionSet(solutionSet, inOnMiddle);
        }
        setArffExecutionClassIndex(scoreArffExecution);
        setArffExecutionClassIndex(architecturalArffExecution);
        startAlgorithms(inOnMiddle);

        LOGGER.info("::: Time: " + ((new Date().getTime() - startsIn) / 1000));
        return resultFront.getSolutionSet();
    }


    private MultilayerPerceptron newScoreAlgorithm() {
        MultilayerPerceptron scoreAlgorithm = new MultilayerPerceptron();
        scoreAlgorithm.setHiddenLayers(getHiddenLayers());
        scoreAlgorithm.setTrainingTime(getTrainingTime());
        scoreAlgorithm.setLearningRate(getLearningRate());
        scoreAlgorithm.setMomentum(getMomentum());
        return scoreAlgorithm;
    }

    private MultilayerPerceptron newArchitecturalAlgorithm() {
        MultilayerPerceptron architecturalAlgorithm = new MultilayerPerceptron();
        architecturalAlgorithm.setHiddenLayers(getArchitecturalAlgorithmHiddenLayers());
        architecturalAlgorithm.setTrainingTime(getTrainingTime());
        architecturalAlgorithm.setLearningRate(getLearningRate());
        architecturalAlgorithm.setMomentum(getMomentum());
        return architecturalAlgorithm;
    }

    private void joinSolutionSet(OPLASolutionSet solutionSet, boolean inOnMiddle) {
        if (!solutionSet.hasUserEvaluation()) {
            LOGGER.info("hasNoUserEvaluation");
            evaluateSolutionSetScoreAlgorithm(solutionSet);
        } else {
            LOGGER.info("hasUserEvaluation");
            distributeUserEvaluations(solutionSet);
        }
        if (!inOnMiddle) {
            ArffExecution newArffArchitectureAlgorithm = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(),
                    resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
            if (newArffArchitectureAlgorithm.getData() != null) {
                newArffArchitectureAlgorithm.getData().setClassIndex(newArffArchitectureAlgorithm.getAttrIndices());
                if (architecturalArffExecution.getData() == null)
                    architecturalArffExecution = newArffArchitectureAlgorithm;
                else architecturalArffExecution.getData().addAll(newArffArchitectureAlgorithm.getData());
            }
        }
        ArffExecution newArffScoreAlgorithm = new ArffExecution(solutionSet.writeObjectivesAndElementsNumberToMatrix(),
                solutionSet.writeUserEvaluationsToMatrix(), null);
        newArffScoreAlgorithm.getData().setClassIndex(newArffScoreAlgorithm.getAttrIndices());
        resultFront.getSolutionSet().getSolutionSet().addAll(solutionSet.getSolutionSet().getSolutionSet());
        scoreArffExecution.getData().addAll(newArffScoreAlgorithm.getData());
    }

    private void setArffExecutionClassIndex(ArffExecution subjectiveArffExecution) {
        if (subjectiveArffExecution.getData() != null)
            subjectiveArffExecution.getData().setClassIndex(subjectiveArffExecution.getAttrIndices());
    }


    private void startAlgorithms(boolean inOnMiddle) {
        Thread threadScoreAlgorithm = new Thread(this::buildScoreAlgorithm);
        threadScoreAlgorithm.start();
        Thread threadArchitecturalAlgorithm = null;
        if (!inOnMiddle) {
            threadArchitecturalAlgorithm = new Thread(this::buildArchitecturalAlgorithm);
            threadArchitecturalAlgorithm.start();
        }

        try {
            threadScoreAlgorithm.join();
            if (threadArchitecturalAlgorithm != null) {
                threadArchitecturalAlgorithm.join();
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
            fileWriter = new FileWriter(ApplicationFileConfigThreadScope.getDirectoryToExportModels()
                    + FileConstants.FILE_SEPARATOR + "LogMachineLearningModel_" + currentEvaluation + ".arff");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (fileWriter != null) {
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(architecturalArffExecution.getData().toString());
            printWriter.close();
        }
    }

    public void evaluateSolutionSetScoreAlgorithm(OPLASolutionSet solutionSet) {
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            try {
                solutionSet.getSolutionSet().get(i).setEvaluation((int) scoreAlgorithm
                        .classifyInstance(new DenseInstance(1.0, solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i])));
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
    public void evaluateSolutionSetScoreAndArchitecturalAlgorithm(OPLASolutionSet solutionSet, boolean subjective) throws Exception {
        LOGGER.info("evaluateSolutionSetScoreAndArchitecturalAlgorithm()");
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            Solution solution = solutionSet.getSolutionSet().get(i);
            double[] solutionMatrix = solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i];
            if (subjective) {
                solution.setEvaluation((int) scoreAlgorithm.classifyInstance(new DenseInstance(1.0, solutionMatrix)));
            }
            List<Element> elementsWithPackages = solution.getAlternativeArchitecture().getElementsWithPackages();
            elementsWithPackages.parallelStream().forEach(element -> {
                double[] data = getDataSet(solutionSet, solution, element);
                if (architecturalArffExecution.getData() != null) {
                    DenseInstance denseInstance = new DenseInstance(1.0, data);
                    denseInstance.setDataset(architecturalArffExecution.getData());
                    try {
                        element.setFreezeFromDM(architecturalAlgorithm.classifyInstance(denseInstance));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LOGGER.info(element.getName() + ":" + element.getTypeElement() + " was "
                            + (element.isFreezeByDM() ? " FREEZED" : " NOT FREEZED"));
                    if (element.isFreezeByDM()) {
                        LOGGER.info("::: Freezed " + element.getName());
                        this.freezedElements.add(element);
                    } else {
                        this.notFreezedElements.add(element);
                    }
                }
            });
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

    private void buildArchitecturalAlgorithm() {
        if (architecturalArffExecution.getData() == null) return;
        LOGGER.info("buildArchitecturalAlgorithm()");
        try {
            Instances data = architecturalArffExecution.getData();
            int trainSize = Math.toIntExact(Math.round(data.numInstances() * getRatioTest()));
            int testSize = data.numInstances() - trainSize;
            Instances train = new Instances(data, 0, trainSize);
            Instances test = new Instances(data, trainSize, testSize);

            architecturalAlgorithm.buildClassifier(train);
            LOGGER.info("Evaluation Architectural");
            architectureEvaluation = new Evaluation(test);
            switch (evaluationModel) {
                case TRAINING_SET:
                    architectureEvaluation.evaluateModel(architecturalAlgorithm, test);
                    break;
                case CROSS_VALIDATION:
                    architectureEvaluation.crossValidateModel(architecturalAlgorithm, test, 10, new Random(1));
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
        stringBuilder.append("Architecture Error: ").append(architectureEvaluation.errorRate()).append("\n");
        stringBuilder.append("Architecture Summary: ").append(architectureEvaluation.toSummaryString()).append("\n");
        LOGGER.info(stringBuilder.toString());

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(ApplicationFileConfigThreadScope.getDirectoryToExportModels()
                    + FileConstants.FILE_SEPARATOR + "LogMachineLearningModelEvaluation_" + currentEvaluation + ".arff");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (fileWriter != null) {
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(stringBuilder);
            printWriter.close();
        }
    }

    private void buildScoreAlgorithm() {
        if (scoreArffExecution.getData() == null) return;
        LOGGER.info("buildScoreAlgorithm()");
        try {
            scoreAlgorithm.buildClassifier(scoreArffExecution.getData());
            LOGGER.info("Evaluation Score");
            scoreEvaluation = new Evaluation(scoreArffExecution.getData());
            switch (evaluationModel) {
                case TRAINING_SET:
                    scoreEvaluation.evaluateModel(scoreAlgorithm, scoreArffExecution.getData());
                    break;
                case CROSS_VALIDATION:
                    scoreEvaluation.crossValidateModel(scoreAlgorithm, scoreArffExecution.getData(), 5, new Random(1));
                    break;
            }
            logSubjectiveModelEvaluation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logSubjectiveModelEvaluation() {
        LOGGER.info("::: Subjective Evaluation :::");
        LOGGER.info("Subjective Error: " + scoreEvaluation.errorRate());
        LOGGER.info("Subjective Summary: " + scoreEvaluation.toSummaryString());
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

    public ArffExecution getScoreArffExecution() {
        return scoreArffExecution;
    }

    public void setScoreArffExecution(ArffExecution scoreArffExecution) {
        this.scoreArffExecution = scoreArffExecution;
    }

    public int getNumObjectives() {
        return numObjectives;
    }

    public void setNumObjectives(int numObjectives) {
        this.numObjectives = numObjectives;
    }


    public AbstractClassifier getScoreAlgorithm() {
        return scoreAlgorithm;
    }

    public void setScoreAlgorithm(AbstractClassifier scoreAlgorithm) {
        this.scoreAlgorithm = scoreAlgorithm;
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
        return hiddenLayers == null ? String.valueOf(Math.round(scoreArffExecution.getAttrIndices())) : hiddenLayers;
    }

    public String getArchitecturalAlgorithmHiddenLayers() {
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

    public AbstractClassifier getArchitecturalAlgorithm() {
        return architecturalAlgorithm;
    }

    public void setArchitecturalAlgorithm(MultilayerPerceptron architecturalAlgorithm) {
        this.architecturalAlgorithm = architecturalAlgorithm;
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

    public Evaluation getScoreEvaluation() {
        return scoreEvaluation;
    }

    public void setScoreEvaluation(Evaluation scoreEvaluation) {
        this.scoreEvaluation = scoreEvaluation;
    }

    public Evaluation getArchitectureEvaluation() {
        return architectureEvaluation;
    }

    public void setArchitectureEvaluation(Evaluation architectureEvaluation) {
        this.architectureEvaluation = architectureEvaluation;
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
