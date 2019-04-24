package learning;

import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Logger;
import org.junit.Test;
import utils.ExperimentTest;
import utils.QtdElements;

import java.util.List;
import java.util.Objects;

public class SubjectiveAnalyzeAlgorithmExperimentTest {
    public static final Logger LOGGER = Logger.getLogger(SubjectiveAnalyzeAlgorithmExperimentTest.class);

    @Test
    public void agmOnMLP1() throws Exception {
        LOGGER.info("AGM MLP");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_27112018.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_27112018.csv");
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 7127396432L);
        solutionSet1.getSolutionSet().forEach(s -> {
            if (s.getObjective(0) >= 700) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });

        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.MLP);
        subjectiveAnalyzeAlgorithm.run(null);

        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 7446612315L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b);

        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2242392391L);
        solutionSet2.getSolutionSet().forEach(s -> {
            if (s.getObjective(0) <= 700) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });

        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1197143379L);

        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 9366513291L);
        solutionSet3.getSolutionSet().forEach(s -> {
            if (s.getObjective(1) >= 27) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });

        SolutionSet solutionSet3b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 9613813659L);

    }


}
