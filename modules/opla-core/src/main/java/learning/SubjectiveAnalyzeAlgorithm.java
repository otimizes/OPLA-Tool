package learning;

import com.rits.cloning.Cloner;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;

import java.util.Date;
import java.util.Random;

//https://stackoverflow.com/questions/28694971/using-neural-network-class-in-weka-in-java-code
public class SubjectiveAnalyzeAlgorithm {
    private static final long serialVersionUID = 1L;
    public static final Logger LOGGER = Logger.getLogger(SubjectiveAnalyzeAlgorithm.class);

    private SolutionSet resultFront;
    private ClassifierAlgorithm algorithm;
    private ArffExecution arffExecution;
    private int numObjectives;
    private MultilayerPerceptron mlp;
    private int trainingTime = 5000;
    private DistributeUserEvaluation distributeUserEvaluation = DistributeUserEvaluation.ALL;
    private EvaluationModel evaluationModel = EvaluationModel.CROSS_VALIDATION;
    private double momentum = 0.2;
    private double learningRate = 0.3;
    private String hiddenLayers;

    public SubjectiveAnalyzeAlgorithm() {
    }

    public SubjectiveAnalyzeAlgorithm(SolutionSet resultFront, ClassifierAlgorithm algorithm) {
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        distributeUserEvaluations(resultFront);
        this.arffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(), resultFront.writeUserEvaluationsToMatrix(), null);
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
        if (mlp == null) {
            mlp = new MultilayerPerceptron();
            mlp.setHiddenLayers(getHiddenLayers());
            mlp.setTrainingTime(getTrainingTime());
            mlp.setLearningRate(getLearningRate());
            mlp.setMomentum(getMomentum());
        }

        if (solutionSet != null) {
            if (!solutionSet.hasUserEvaluation()) {
                for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
                    solutionSet.get(i).setEvaluation((int) mlp.classifyInstance(new DenseInstance(1.0, solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i])));
                }
            } else {
                distributeUserEvaluations(solutionSet);
            }
            ArffExecution newArff = new ArffExecution(solutionSet.writeObjectivesAndElementsNumberToMatrix(), solutionSet.writeUserEvaluationsToMatrix(), null);
            newArff.getData().setClassIndex(newArff.getAttrIndices());
            resultFront.getSolutionSet().addAll(solutionSet.getSolutionSet());
            arffExecution.getData().addAll(newArff.getData());
        }
        arffExecution.getData().setClassIndex(arffExecution.getAttrIndices());
        mlp.buildClassifier(arffExecution.getData());
        Evaluation eval = new Evaluation(arffExecution.getData());
        switch (evaluationModel) {
            case TRAINING_SET:
                eval.evaluateModel(mlp, arffExecution.getData());
                break;
            case CROSS_VALIDATION:
                eval.crossValidateModel(mlp, arffExecution.getData(), 5, new Random(1));
                break;
        }
        System.out.println("Error: " + eval.errorRate());
        System.out.println("Summary: " + eval.toSummaryString());

        for (int i = 0; i < arffExecution.getData().size(); i++) {
            System.out.println("Solution " + i + ": Expected: " + resultFront.get(i).getEvaluation() + " - Predicted: " + ((int) mlp.classifyInstance(arffExecution.getData().get(i))));
        }
        System.out.println("Tempo: " + ((new Date().getTime() - startsIn) / 1000));
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

    public ArffExecution getArffExecution() {
        return arffExecution;
    }

    public void setArffExecution(ArffExecution arffExecution) {
        this.arffExecution = arffExecution;
    }

    public int getNumObjectives() {
        return numObjectives;
    }

    public void setNumObjectives(int numObjectives) {
        this.numObjectives = numObjectives;
    }


    public MultilayerPerceptron getMlp() {
        return mlp;
    }

    public void setMlp(MultilayerPerceptron mlp) {
        this.mlp = mlp;
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
        return hiddenLayers == null ? String.valueOf(Math.round(arffExecution.getAttrIndices())) : hiddenLayers;
    }

    public void setHiddenLayers(String hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }
}
