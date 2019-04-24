package learning;

import com.rits.cloning.Cloner;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;

//https://stackoverflow.com/questions/28694971/using-neural-network-class-in-weka-in-java-code
public class SubjectiveAnalyzeAlgorithm {
    private static final long serialVersionUID = 1L;
    public static final Logger LOGGER = Logger.getLogger(SubjectiveAnalyzeAlgorithm.class);

    private SolutionSet resultFront;
    private ClassifierAlgorithm algorithm;
    private ArffExecution arffExecution;
    private int numObjectives;
    MultilayerPerceptron mlp;

    public SubjectiveAnalyzeAlgorithm() {
    }

    public SubjectiveAnalyzeAlgorithm(SolutionSet resultFront, ClassifierAlgorithm algorithm) {
        this.resultFront = new Cloner().deepClone(resultFront);
        this.algorithm = algorithm;
        this.arffExecution = new ArffExecution(resultFront.writeObjectivesAndElementsNumberToMatrix(), resultFront.writeUserEvaluationsToMatrix(), null);
        this.numObjectives = this.resultFront.getSolutionSet().get(0).numberOfObjectives();
    }

    /**
     * Execution Method
     *
     * @return Solution Set - Best performing cluster with another solutions (filteredSolutions)
     * @throws Exception Default Exception
     */
    public SolutionSet run(SolutionSet solutionSet) throws Exception {
        switch (algorithm) {
            case MLP:
                return MLP(solutionSet);
            case FUZZY_KMEANS:
                return null;
        }
        return null;
    }

    /**
     * MLP Execution Method
     *
     * @return Solution Set
     * @throws Exception Default Exception
     */
    public SolutionSet MLP(SolutionSet solutionSet) throws Exception {
        if (mlp == null) {
            mlp = new MultilayerPerceptron();
            mlp.setHiddenLayers(String.valueOf(Math.round(arffExecution.getAttrIndices() / 2)));
            mlp.setTrainingTime(1500);
        }

        if (solutionSet != null) {

            if (!solutionSet.hasUserEvaluation()) {

                for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
                    solutionSet.get(i).setEvaluation((int) mlp.classifyInstance(new DenseInstance(1.0, solutionSet.writeObjectivesAndElementsNumberEvaluationToMatrix()[i])));
                }
            }
            ArffExecution newArff = new ArffExecution(solutionSet.writeObjectivesAndElementsNumberToMatrix(), solutionSet.writeUserEvaluationsToMatrix(), null);
            newArff.getData().setClassIndex(newArff.getAttrIndices());
            resultFront.getSolutionSet().addAll(solutionSet.getSolutionSet());
            arffExecution.getData().addAll(newArff.getData());
        }
        arffExecution.getData().setClassIndex(arffExecution.getAttrIndices());
        mlp.buildClassifier(arffExecution.getData());
        Evaluation eval = new Evaluation(arffExecution.getData());
        eval.evaluateModel(mlp, arffExecution.getData());
        System.out.println("Error: " + eval.errorRate());
        System.out.println("Summary: " + eval.toSummaryString());

        for (int i = 0; i < arffExecution.getData().size(); i++) {
            System.out.println(resultFront.get(i).getEvaluation() + " - " + mlp.classifyInstance(arffExecution.getData().get(i)));
        }
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
}
