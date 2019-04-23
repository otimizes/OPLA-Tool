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
    public void agmOnMLP() throws Exception {
        LOGGER.info("AGM MLP");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_27112018.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_27112018.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 7127396432L);


        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet, ClassifierAlgorithm.MLP);
        SolutionSet run = subjectiveAnalyzeAlgorithm.run();

        System.out.println(subjectiveAnalyzeAlgorithm.getArffExecution().toString());

    }
}
