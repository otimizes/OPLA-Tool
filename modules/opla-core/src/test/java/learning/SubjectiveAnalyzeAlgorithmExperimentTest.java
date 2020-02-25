package learning;

import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import utils.ExperimentTest;
import utils.QtdElements;

import java.util.List;

/* GERAL:
    Eu inclui na base de dados as soluções que tiveram nota = 0, para a MLP aprender quais dados ela não deve avaliar.

    Aumentar o número de épocas de acordo com o número de elementos na base de dados não influencia nos resultados.

 */

public class SubjectiveAnalyzeAlgorithmExperimentTest {
    public static final Logger LOGGER = Logger.getLogger(SubjectiveAnalyzeAlgorithmExperimentTest.class);


    /**
     * Abordagem que generaliza as notas no cluster
     * <p>
     * Primeira interação: 3
     * Intervalo de interações: 3
     * População: 200
     * Avaliações: 3000
     * Gerações: 3000 / 200 = 15
     * Execuções: 1
     * Interações: I: 3     M: 4    I: 6    M: 7    I: 9
     *
     * @throws Exception
     */
//    @Test
    public void abordagem1SemAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        Clustering.LOGGER.setLevel(Level.OFF);
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_03062019.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 0L);
        Clustering clustering = new Clustering(solutionSet1, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(4);
        clustering.run();
        clustering.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.CLUSTERING_MLP);
        subjectiveAnalyzeAlgorithm.run(null, false);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 11L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b, true);
//
        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1L);
        Clustering clustering2 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering2.setNumClusters(4);
        clustering2.run();
        clustering2.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering2.getSolutionsByClusterId(1).get(0).setEvaluation(4);
        clustering2.getSolutionsByClusterId(2).get(0).setEvaluation(3);
        clustering2.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet2, false);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 22L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b, true);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2L);
        Clustering clustering3 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering3.setNumClusters(4);
        clustering3.run();
        clustering3.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(1).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(2).get(0).setEvaluation(3);
        clustering3.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet3, false);

    }

    //    @Test
    public void abordagem1ComAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        Clustering.LOGGER.setLevel(Level.OFF);
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_03062019.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 0L);
        Clustering clustering = new Clustering(solutionSet1, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(4);
        clustering.run();
        clustering.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.CLUSTERING_MLP);
        subjectiveAnalyzeAlgorithm.run(null, false);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 11L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b, true);
//
        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1L);
        Clustering clustering2 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering2.setNumClusters(4);
        clustering2.run();
        clustering2.getSolutionsByClusterId(0).get(0).setEvaluation(2);
        clustering2.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering2.getSolutionsByClusterId(2).get(0).setEvaluation(4);
        clustering2.getSolutionsByClusterId(3).get(0).setEvaluation(5);
        subjectiveAnalyzeAlgorithm.run(solutionSet2, false);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 22L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b, true);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2L);
        Clustering clustering3 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering3.setNumClusters(4);
        clustering3.run();
        clustering3.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering3.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering3.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet3, false);
    }

    /**
     * Abordagem Sem generalização alguma das notas.
     * <p>
     * O erro é pequeno pois são poucas soluções avaliadas, porém quando analisa os dados, nenhuma solução avaliada foi avaliada corretamente.
     *
     * @throws Exception
     */
//    @Test
    public void abordagem2SemAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        Clustering.LOGGER.setLevel(Level.OFF);
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_03062019.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 0L);
        Clustering clustering = new Clustering(solutionSet1, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(4);
        clustering.run();
        clustering.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.MLP);
        subjectiveAnalyzeAlgorithm.run(null, false);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 11L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b, true);
//
        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1L);
        Clustering clustering2 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering2.setNumClusters(4);
        clustering2.run();
        clustering2.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering2.getSolutionsByClusterId(1).get(0).setEvaluation(4);
        clustering2.getSolutionsByClusterId(2).get(0).setEvaluation(3);
        clustering2.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet2, false);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 22L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b, true);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2L);
        Clustering clustering3 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering3.setNumClusters(4);
        clustering3.run();
        clustering3.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(1).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(2).get(0).setEvaluation(3);
        clustering3.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet3, false);

    }

    //    @Test
    public void abordagem2ComAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        Clustering.LOGGER.setLevel(Level.OFF);
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_03062019.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 0L);
        Clustering clustering = new Clustering(solutionSet1, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(4);
        clustering.run();
        clustering.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.MLP);
        subjectiveAnalyzeAlgorithm.run(null, false);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 11L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b, true);
//
        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1L);
        Clustering clustering2 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering2.setNumClusters(4);
        clustering2.run();
        clustering2.getSolutionsByClusterId(0).get(0).setEvaluation(2);
        clustering2.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering2.getSolutionsByClusterId(2).get(0).setEvaluation(4);
        clustering2.getSolutionsByClusterId(3).get(0).setEvaluation(5);
        subjectiveAnalyzeAlgorithm.run(solutionSet2, false);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 22L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b, true);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2L);
        Clustering clustering3 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering3.setNumClusters(4);
        clustering3.run();
        clustering3.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering3.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering3.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet3, false);
    }

    /**
     * Abordagem com meia generalização das notas.
     *
     * @throws Exception
     */
//    @Test
    public void abordagem3SemAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        Clustering.LOGGER.setLevel(Level.OFF);
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_03062019.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 0L);
        Clustering clustering = new Clustering(solutionSet1, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(4);
        clustering.run();
        clustering.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.CLUSTERING_MLP, DistributeUserEvaluation.MIDDLE);
        subjectiveAnalyzeAlgorithm.setDistributeUserEvaluation(DistributeUserEvaluation.MIDDLE);
        subjectiveAnalyzeAlgorithm.run(null, false);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 11L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b, false);
//
        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1L);
        Clustering clustering2 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering2.setNumClusters(4);
        clustering2.run();
        clustering2.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering2.getSolutionsByClusterId(1).get(0).setEvaluation(4);
        clustering2.getSolutionsByClusterId(2).get(0).setEvaluation(3);
        clustering2.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet2, true);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 22L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b, false);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2L);
        Clustering clustering3 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering3.setNumClusters(4);
        clustering3.run();
        clustering3.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(1).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(2).get(0).setEvaluation(3);
        clustering3.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet3, true);

    }

    //    @Test
    public void abordagem3ComAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        Clustering.LOGGER.setLevel(Level.OFF);
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_03062019.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 0L);
        Clustering clustering = new Clustering(solutionSet1, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(4);
        clustering.run();
        clustering.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.CLUSTERING_MLP, DistributeUserEvaluation.MIDDLE);
        subjectiveAnalyzeAlgorithm.run(null, false);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 11L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b, true);
//
        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1L);
        Clustering clustering2 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering2.setNumClusters(4);
        clustering2.run();
        clustering2.getSolutionsByClusterId(0).get(0).setEvaluation(2);
        clustering2.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering2.getSolutionsByClusterId(2).get(0).setEvaluation(4);
        clustering2.getSolutionsByClusterId(3).get(0).setEvaluation(5);
        subjectiveAnalyzeAlgorithm.run(solutionSet2, false);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 22L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b, true);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2L);
        Clustering clustering3 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering3.setNumClusters(4);
        clustering3.run();
        clustering3.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering3.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering3.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        subjectiveAnalyzeAlgorithm.run(solutionSet3, false);
    }


}
