package learning;

import arquitetura.representation.Element;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;

import java.util.Date;
import java.util.List;
import java.util.Random;

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
    private EvaluationModel evaluationModel = EvaluationModel.TRAINING_SET;
    private double momentum = 0.2;
    private double learningRate = 0.3;
    private String hiddenLayers;
    private List<Element> evaluatedElements;

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
        this.subjectiveArffExecution = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(), resultFront.writeArchitecturalEvaluationsToMatrix(), null);
        this.architecturalArffExecution = new ArffExecution(resultFront.writeObjectivesAndArchitecturalElementsNumberToMatrix(), resultFront.writeArchitecturalEvaluationsToMatrix(), null, true);
        this.numObjectives = this.resultFront.getSolutionSet().get(0).numberOfObjectives();
    }

    private void distributeUserEvaluations(SolutionSet resultFront) {
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
    public SolutionSet run(SolutionSet solutionSet) throws Exception {
        return MLP(solutionSet);
    }

    public SolutionSet run() throws Exception {
        return run(null);
    }

    /**
     * MLP Execution Method
     *
     * @return Solution Set
     * @throws Exception Default Exception
     */
    public SolutionSet MLP(SolutionSet solutionSet) throws Exception {
        long startsIn = new Date().getTime();
        if (subjectiveMLP == null) {
            subjectiveMLP = new MultilayerPerceptron();
            subjectiveMLP.setHiddenLayers(getHiddenLayers());
            subjectiveMLP.setTrainingTime(getTrainingTime());
            subjectiveMLP.setLearningRate(getLearningRate());
            subjectiveMLP.setMomentum(getMomentum());

            architecturalMLP = new MultilayerPerceptron();
            architecturalMLP.setHiddenLayers(getHiddenLayers());
            architecturalMLP.setTrainingTime(getTrainingTime());
            architecturalMLP.setLearningRate(getLearningRate());
            architecturalMLP.setMomentum(getMomentum());
        }

        if (solutionSet != null) {
            if (!solutionSet.hasUserEvaluation()) {
                for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
                    solutionSet.get(i).setEvaluation((int) subjectiveMLP.classifyInstance(new DenseInstance(1.0, solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i])));
                    for (Element element : solutionSet.get(i).getOPLAProblem().getArchitecture_().getElements()) {
                        double[] data = solutionSet.get(i).getObjectives();
                        data = ArrayUtils.addAll(data, solutionSet.writeCharacteristicsFromElement(element));
                        data = ArrayUtils.addAll(data, new double[]{element.isFreeze() ? 1 : 0});
                        DenseInstance denseInstance = new DenseInstance(1.0, data);
                        denseInstance.setDataset(architecturalArffExecution.getData());
                        element.setFreeze(architecturalMLP.classifyInstance(denseInstance));
                        if (element.isFreeze()) {
                            System.out.println("Congelou " + element.getName());
                        }
                    }
                }
            } else {
                distributeUserEvaluations(solutionSet);
            }
            ArffExecution newArffSubjectiveMLP = new ArffExecution(solutionSet.writeObjectivesAndElementsNumberToMatrix(), solutionSet.writeUserEvaluationsToMatrix(), null);
            newArffSubjectiveMLP.getData().setClassIndex(newArffSubjectiveMLP.getAttrIndices());
            resultFront.getSolutionSet().addAll(solutionSet.getSolutionSet());
            subjectiveArffExecution.getData().addAll(newArffSubjectiveMLP.getData());

            ArffExecution newArffArchitectureMLP = new ArffExecution(solutionSet.writeObjectivesAndArchitecturalElementsNumberToMatrix(), solutionSet.writeArchitecturalEvaluationsToMatrix(), null);
            newArffArchitectureMLP.getData().setClassIndex(newArffArchitectureMLP.getAttrIndices());
            architecturalArffExecution.getData().addAll(newArffArchitectureMLP.getData());
        }
        subjectiveArffExecution.getData().setClassIndex(subjectiveArffExecution.getAttrIndices());
        architecturalArffExecution.getData().setClassIndex(architecturalArffExecution.getAttrIndices());
        subjectiveMLP.buildClassifier(subjectiveArffExecution.getData());
        architecturalMLP.buildClassifier(architecturalArffExecution.getData());
        Evaluation subjectiveEval = new Evaluation(subjectiveArffExecution.getData());
        Evaluation architectureEval = new Evaluation(architecturalArffExecution.getData());
        switch (evaluationModel) {
            case TRAINING_SET:
                subjectiveEval.evaluateModel(subjectiveMLP, subjectiveArffExecution.getData());
                architectureEval.evaluateModel(architecturalMLP, architecturalArffExecution.getData());
                break;
            case CROSS_VALIDATION:
                subjectiveEval.crossValidateModel(subjectiveMLP, subjectiveArffExecution.getData(), 5, new Random(1));
                architectureEval.crossValidateModel(architecturalMLP, architecturalArffExecution.getData(), 5, new Random(1));
                break;
        }
        System.out.println("----------------------------------------> Subjective Evaluation <----------------------------------------");
        System.out.println("Subjective Error: " + subjectiveEval.errorRate());
        System.out.println("Subjective Summary: " + subjectiveEval.toSummaryString());
//        for (int i = 0; i < subjectiveArffExecution.getData().size(); i++) {
//            System.out.println("Solution " + i + ": Expected: " + resultFront.get(i).getEvaluation() + " - Predicted: " + ((int) subjectiveMLP.classifyInstance(subjectiveArffExecution.getData().get(i))));
//        }

        System.out.println("----------------------------------------> Architectural Evaluation <-------------------------------------");
        System.out.println("Architecture Error: " + architectureEval.errorRate());
        System.out.println("Architecture Summary: " + architectureEval.toSummaryString());
        System.out.println("Tempo: " + ((new Date().getTime() - startsIn) / 1000));
//        for (int i = 0; i < architecturalArffExecution.getData().size(); i++) {
//            System.out.println("Solution " + i + ": Expected: " + architecturalArffExecution.getData().get(i).classValue() + " - Predicted: " + ((int) architecturalMLP.classifyInstance(architecturalArffExecution.getData().get(i))));
//        }
        return resultFront;
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
}
