package learning;

import arquitetura.io.ReaderConfig;
import arquitetura.representation.Element;
import domain.core.Solution;
import domain.core.SolutionSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//https://stackoverflow.com/questions/28694971/using-neural-network-class-in-weka-in-java-code
public class SubjectiveAnalyzeAlgorithm {
    private static final long serialVersionUID = 1L;
    public static final Logger LOGGER = Logger.getLogger(SubjectiveAnalyzeAlgorithm.class);

    private SolutionSet resultFront;
    private ClassifierAlgorithm algorithm;
    private int numObjectives;
    private ArffExecution subjectiveArffExecution;
    private MultilayerPerceptron subjectiveMLP;
    private ArffExecution architecturalArffExecution;
    private MultilayerPerceptron architecturalMLP;
    private int trainingTime = 2500;
    private DistributeUserEvaluation distributeUserEvaluation = DistributeUserEvaluation.ALL;
    private EvaluationModel evaluationModel = EvaluationModel.CROSS_VALIDATION;
    private double momentum = 0.2;
    private double learningRate = 0.3;
    private String hiddenLayers;
    private List<Element> evaluatedElements;
    private List<SolutionSet> interactions = new ArrayList<>();
    private boolean trained = false;
    public static int currentEvaluation = 0;

    public SubjectiveAnalyzeAlgorithm() {
    }

    public SubjectiveAnalyzeAlgorithm(SolutionSet resultFront, ClassifierAlgorithm algorithm, DistributeUserEvaluation distributeUserEvaluation) {
        this.distributeUserEvaluation = distributeUserEvaluation;
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.subjectiveArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(), resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(), resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
        this.numObjectives = this.resultFront.getSolutionSet().get(0).numberOfObjectives();
    }

    public SubjectiveAnalyzeAlgorithm(SolutionSet resultFront, ClassifierAlgorithm algorithm) {
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.subjectiveArffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(), resultFront.writeUserEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(), resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
        this.numObjectives = this.resultFront.getSolutionSet().get(0).numberOfObjectives();
    }

    private void distributeUserEvaluations(SolutionSet resultFront) {
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
    public SolutionSet run(SolutionSet solutionSet, boolean middle) {
        try {
            return MLP(solutionSet, middle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() throws Exception {
        run(null, false);
        for (SolutionSet interaction : interactions) {
            run(interaction, false);
        }
    }

    /**
     * MLP Execution Method
     *
     * @return Solution Set
     * @throws Exception Default Exception
     */
    public SolutionSet MLP(SolutionSet solutionSet, boolean middle) {
        currentEvaluation++;
        LOGGER.info("MLP()");
        long startsIn = new Date().getTime();
        if (subjectiveMLP == null) {
            LOGGER.info("MLP() Start objects");
            subjectiveMLP = new MultilayerPerceptron();
            subjectiveMLP.setHiddenLayers(getHiddenLayers());
            subjectiveMLP.setTrainingTime(getTrainingTime());
            subjectiveMLP.setLearningRate(getLearningRate());
            subjectiveMLP.setMomentum(getMomentum());

            architecturalMLP = new MultilayerPerceptron();
            architecturalMLP.setHiddenLayers(getArchitecturalMLPHiddenLayers());
            architecturalMLP.setTrainingTime(getTrainingTime());
            architecturalMLP.setLearningRate(getLearningRate());
            architecturalMLP.setMomentum(getMomentum());
        }

        if (solutionSet != null) {
            if (!solutionSet.hasUserEvaluation()) {
                LOGGER.info("MLP() donthasUserEvaluation");
                evaluateSolutionSetSubjectiveMLP(solutionSet);
            } else {
                LOGGER.info("MLP() hasUserEvaluation");
                distributeUserEvaluations(solutionSet);
            }
            if (!middle) {
                ArffExecution newArffArchitectureMLP = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(), resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
                newArffArchitectureMLP.getData().setClassIndex(newArffArchitectureMLP.getAttrIndices());
                architecturalArffExecution.getData().addAll(newArffArchitectureMLP.getData());
            }
            ArffExecution newArffSubjectiveMLP = new ArffExecution(solutionSet.writeObjectivesAndElementsNumberToMatrix(), solutionSet.writeUserEvaluationsToMatrix(), null);
            newArffSubjectiveMLP.getData().setClassIndex(newArffSubjectiveMLP.getAttrIndices());
            resultFront.getSolutionSet().addAll(solutionSet.getSolutionSet());
            subjectiveArffExecution.getData().addAll(newArffSubjectiveMLP.getData());
        }
        subjectiveArffExecution.getData().setClassIndex(subjectiveArffExecution.getAttrIndices());
        architecturalArffExecution.getData().setClassIndex(architecturalArffExecution.getAttrIndices());
        Thread threadSubjectiveMLP = new Thread(this::buildSubjectiveMLP);
        threadSubjectiveMLP.start();
        Thread threadArchitecturalMLP = null;
        if (!middle) {
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

        LOGGER.info("-------------->>>>>>>>> Tempo: " + ((new Date().getTime() - startsIn) / 1000));
        return resultFront;
    }

    private void logMachineLearningModel() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(ReaderConfig.getDirExportTarget() + System.getProperty("file.separator") + "LogMachineLearningModel_" + currentEvaluation + ".arff");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(architecturalArffExecution.getData().toString());
        printWriter.close();
    }

    public void evaluateSolutionSetSubjectiveMLP(SolutionSet solutionSet) {
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            try {
                solutionSet.get(i).setEvaluation((int) subjectiveMLP.classifyInstance(new DenseInstance(1.0, solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i])));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void evaluateSolutionSetSubjectiveAndArchitecturalMLP(SolutionSet solutionSet, boolean subjective) throws Exception {
        LOGGER.info("evaluateSolutionSetSubjectiveAndArchitecturalMLP()");
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            if (subjective) {
                solutionSet.get(i).setEvaluation((int) subjectiveMLP.classifyInstance(new DenseInstance(1.0, solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i])));
            }
            for (Element element : solutionSet.get(i).getAlternativeArchitecture().getElementsWithPackages()) {
                double[] data = solutionSet.get(i).getObjectives();
                data = ArrayUtils.addAll(data, solutionSet.writeCharacteristicsFromElement(element, solutionSet.get(i)));

                Solution solution = solutionSet.get(i);
                double[] objectives = new double[0];
                try {
                    objectives = solutionSet.generateSolutionFromElementsAndGetDoubles(element, solution);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                data = ArrayUtils.addAll(data, objectives);

                data = ArrayUtils.addAll(data, new double[]{
                        solutionSet.get(i).containsArchitecturalEvaluation() ? 1 : 0,
                        element.isFreezeByDM() ? 1 : 0
                });
                DenseInstance denseInstance = new DenseInstance(1.0, data);
                denseInstance.setDataset(architecturalArffExecution.getData());
                element.setFreezeFromDM(architecturalMLP.classifyInstance(denseInstance));
                LOGGER.info(element.getName() + ":" + element.getTypeElement() + " was " + (element.isFreezeByDM() ? " FREEZED" : " NOT FREEZED"));
                if (element.isFreezeByDM()) {
                    LOGGER.info("->>>>> Congelou " + element.getName());
                }
            }
        }
    }

    private void buildArchitecturalMLP() {
        LOGGER.info("MLP() buildArchitecturalMLP()");
        try {
            architecturalMLP.buildClassifier(architecturalArffExecution.getData());
            LOGGER.info("MLP() Evaluation Architectural");
            Evaluation architectureEval = new Evaluation(architecturalArffExecution.getData());
            switch (evaluationModel) {
                case TRAINING_SET:
                    architectureEval.evaluateModel(architecturalMLP, architecturalArffExecution.getData());
                    break;
                case CROSS_VALIDATION:
                    architectureEval.crossValidateModel(architecturalMLP, architecturalArffExecution.getData(), 5, new Random(1));
                    break;
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--> Architectural Evaluation <------------------------------------- \n");
            stringBuilder.append("Architecture Error: " + architectureEval.errorRate() + "\n");
            stringBuilder.append("Architecture Summary: " + architectureEval.toSummaryString() + "\n");
            for (int i = 0; i < architecturalArffExecution.getData().size(); i++) {
                List<Element> elementWithNumberId = getResultFront().findElementWithNumberId(architecturalArffExecution.getData().get(i).value(3));
                stringBuilder.append(elementWithNumberId.get(0).getTypeElement() + " " + elementWithNumberId.get(0).getName() + ": Expected: " + architecturalArffExecution.getData().get(i).classValue() + " - Predicted: " + ((int) architecturalMLP.classifyInstance(architecturalArffExecution.getData().get(i))) + "\n");
            }
            LOGGER.info(stringBuilder.toString());
            logMachineLearningModelEvaluation(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void logMachineLearningModelEvaluation(StringBuilder stringBuilder) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(ReaderConfig.getDirExportTarget() + System.getProperty("file.separator") + "LogMachineLearningModelEvaluation_" + currentEvaluation + ".arff");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(stringBuilder);
        printWriter.close();
    }

    private void buildSubjectiveMLP() {
        LOGGER.info("MLP() buildSubjectiveMLP()");
        try {
            subjectiveMLP.buildClassifier(subjectiveArffExecution.getData());
            LOGGER.info("MLP() Evaluation Subjective");
            Evaluation subjectiveEval = new Evaluation(subjectiveArffExecution.getData());
            switch (evaluationModel) {
                case TRAINING_SET:
                    subjectiveEval.evaluateModel(subjectiveMLP, subjectiveArffExecution.getData());
                    break;
                case CROSS_VALIDATION:
                    subjectiveEval.crossValidateModel(subjectiveMLP, subjectiveArffExecution.getData(), 5, new Random(1));
                    break;
            }
            LOGGER.info("--> Subjective Evaluation <----------------------------------------");
            LOGGER.info("Subjective Error: " + subjectiveEval.errorRate());
            LOGGER.info("Subjective Summary: " + subjectiveEval.toSummaryString());
//        for (int i = 0; i < subjectiveArffExecution.getData().size(); i++) {
//            System.out.println("Solution " + i + ": Expected: " + resultFront.get(i).getEvaluation() + " - Predicted: " + ((int) subjectiveMLP.classifyInstance(subjectiveArffExecution.getData().get(i))));
//        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public SolutionSet getResultFront() {
        return resultFront;
    }

    public void setResultFront(SolutionSet resultFront) {
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

    public EvaluationModel getEvaluationModel() {
        return evaluationModel;
    }

    public void setEvaluationModel(EvaluationModel evaluationModel) {
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

    public void addInteraction(SolutionSet offspringPopulation) {
        interactions.add(offspringPopulation);
    }

    public MultilayerPerceptron getArchitecturalMLP() {
        return architecturalMLP;
    }

    public void setArchitecturalMLP(MultilayerPerceptron architecturalMLP) {
        this.architecturalMLP = architecturalMLP;
    }

    public List<SolutionSet> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<SolutionSet> interactions) {
        this.interactions = interactions;
    }

    public boolean isTrained() {
        return trained;
    }

    public void setTrained(boolean trained) {
        this.trained = trained;
    }
}
