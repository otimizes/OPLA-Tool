package learning;

import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Logger;
import org.junit.Test;
import utils.ExperimentTest;
import utils.QtdElements;

import java.util.List;

/* GERAL:
    Eu inclui na base de dados as soluções que tiveram nota = 0, para a MLP aprender quais dados ela não deve avaliar.

    Aumentar o número de épocas de acordo com o número de elementos na base de dados não influencia nos resultados.

 */

public class SubjectiveAnalyzeAlgorithmExperimentTest {
    public static final Logger LOGGER = Logger.getLogger(SubjectiveAnalyzeAlgorithmExperimentTest.class);

    /*
        Na 1º interação, avaliei com nota máxima as soluções com melhor FM
            - Com 2000 épocas, a rede se ajusta em torno de 100%
        Na 2º interação, avaliei com nota máxima as soluções com pior FM
            - Como inverti totalmente o perfil de avaliação, a acurácia cai em torno de 75 a 80%
        Na 3 interação, avaliei com nota máxima as soluções com melhor ACLASS
            - Na terceira interação, ela se adapta em 80% de acurácia, mudando o perfil de avaliação para ACLASS
     */
    @Test
    public void agmOnMLP1ComAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_27112018.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_27112018.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 7127396432L);
        solutionSet1.getSolutionSet().forEach(s -> {
            if (s.getObjective(0) >= 700) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });

        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.MLP);
        subjectiveAnalyzeAlgorithm.run(null);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 7446612315L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b);

        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2242392391L);
        solutionSet2.getSolutionSet().forEach(s -> {
            if (s.getObjective(0) <= 700) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });
        subjectiveAnalyzeAlgorithm.run(solutionSet2);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 6614175339L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 9366513291L);
        solutionSet3.getSolutionSet().forEach(s -> {
            if (s.getObjective(1) >= 27) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });
        subjectiveAnalyzeAlgorithm.run(solutionSet3);

        LOGGER.info("3º Interação SEM NOTAS");
        //        3º Interação SEM NOTAS
        SolutionSet solutionSet3b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 9613813659L);
        subjectiveAnalyzeAlgorithm.run(solutionSet3b);
    }

    /*
        Em todas interações, a rede teve acurácia em torno de 100%
     */
    @Test
    public void agmOnMLP1SemAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_27112018.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_27112018.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 7127396432L);
        solutionSet1.getSolutionSet().forEach(s -> {
            if (s.getObjective(0) >= 700) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });

        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.MLP);
        subjectiveAnalyzeAlgorithm.run(null);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 7446612315L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b);

        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2242392391L);
        solutionSet2.getSolutionSet().forEach(s -> {
            if (s.getObjective(0) >= 700) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });
        subjectiveAnalyzeAlgorithm.run(solutionSet2);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 6614175339L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 9366513291L);
        solutionSet3.getSolutionSet().forEach(s -> {
            if (s.getObjective(0) >= 700) s.setEvaluation(5);
            else s.setEvaluation((int) Math.abs(Math.random() * 5));
        });
        subjectiveAnalyzeAlgorithm.run(solutionSet3);

        LOGGER.info("3º Interação SEM NOTAS");
        //        3º Interação SEM NOTAS
        SolutionSet solutionSet3b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 9613813659L);
        subjectiveAnalyzeAlgorithm.run(solutionSet3b);

    }


}
