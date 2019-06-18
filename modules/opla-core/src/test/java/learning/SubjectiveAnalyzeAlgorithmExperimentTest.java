package learning;

import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Level;
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

    /**
     * Primeira interação: 3
     * Intervalo de interações: 3
     * População: 200
     * Avaliações: 3000
     * Gerações: 3000 / 200 = 15
     * Execuções: 1
     * Interações: I: 3     M: 4    I: 6    M: 7    I: 9
     *
     * Observações:
     * Utilizar notas = 0 como entrada da rede desbalancea muito a matriz de pesos da rede, pois a rede precisa de muito mais gerações para se adaptar. 0 é um problema pois afeta o cálculo do gradiente.
     *  É melhor utilizar só o espectro 1,2,3,4,5 da escala de likert.
     *
     * Nos testes anteriores a abordagem com 0 se saiu melhor pois são poucos dados e avaliei mais soluções. Neste caso considerei que o usuário avalie uma só por cluster (objetivo)
     *
     * Neste caso segui a ideia de ter um cluster por objetivo, pois utilizar a fórmula comumente utilizada na literatura pode gerar mais de 3 clusters o que faria que alguns clusters não seriam avaliados.
     *
     * Por mais que eu ache um algoritmo muito pesado, utilizei o crossvalidation para evitar o overfitting.
     * --------------------------------------------------------
     *    Tempo em segundos TrainingSet = 3 6 9 13 17
     * Inicio: Error: 0.015
     * Summary:
     * Correctly Classified Instances         197               98.5    %
     * Incorrectly Classified Instances         3                1.5    %
     * Kappa statistic                          0.9661
     * Mean absolute error                      0.0112
     * Root mean squared error                  0.0693
     * Relative absolute error                  7.3661 %
     * Root relative squared error             25.4209 %
     * Total Number of Instances              200
     * Fim: Error: 0.111
     * Summary:
     * Correctly Classified Instances         889               88.9    %
     * Incorrectly Classified Instances       111               11.1    %
     * Kappa statistic                          0.8439
     * Mean absolute error                      0.0597
     * Root mean squared error                  0.1714
     * Relative absolute error                 24.7619 %
     * Root relative squared error             49.3775 %
     * Total Number of Instances             1000
     * ---------------------------------------------------------
     *    Tempo em segundos CrossValidation5Folds = 17 32 48 65 79
     * Inicio: Error: 0.015
     * Summary:
     * Correctly Classified Instances         197               98.5    %
     * Incorrectly Classified Instances         3                1.5    %
     * Kappa statistic                          0.9661
     * Mean absolute error                      0.0119
     * Root mean squared error                  0.072
     * Relative absolute error                  7.7427 %
     * Root relative squared error             26.4067 %
     * Fim: Error: 0.134
     * Summary:
     * Correctly Classified Instances         866               86.6    %
     * Incorrectly Classified Instances       134               13.4    %
     * Kappa statistic                          0.8124
     * Mean absolute error                      0.0607
     * Root mean squared error                  0.1857
     * Relative absolute error                 25.1426 %
     * Root relative squared error             53.5015 %
     * Total Number of Instances             1000
     * Total Number of Instances              200
     * -----------------------------------------------------------
     *    Tempo em segundos CrossValidation10Folds = 32 66 94 138 170
     * Inicio: Error: 0.015
     * Summary:
     * Correctly Classified Instances         197               98.5    %
     * Incorrectly Classified Instances         3                1.5    %
     * Kappa statistic                          0.9661
     * Mean absolute error                      0.0118
     * Root mean squared error                  0.0723
     * Relative absolute error                  7.7181 %
     * Root relative squared error             26.5106 %
     * Total Number of Instances              200
     * Fim: Error: 0.123
     * Summary:
     * Correctly Classified Instances         877               87.7    %
     * Incorrectly Classified Instances       123               12.3    %
     * Kappa statistic                          0.8277
     * Mean absolute error                      0.0597
     * Root mean squared error                  0.1834
     * Relative absolute error                 24.7257 %
     * Root relative squared error             52.8269 %
     * Total Number of Instances             1000
     *
     *
     *
     * @throws Exception
     */
    @Test
    public void agmOnMLP2SemAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        Clustering.LOGGER.setLevel(Level.OFF);
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_03062019.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 0L);
        Clustering clustering = new Clustering(solutionSet1, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(3);
        clustering.run();
        clustering.getSolutionsByClusterWithMinObjective(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterWithMinObjective(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterWithMinObjective(2).get(0).setEvaluation(2);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.CLUSTERING_MLP);
        subjectiveAnalyzeAlgorithm.run(null);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 11L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b);
//
        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1L);
        Clustering clustering2 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering2.setNumClusters(3);
        clustering2.run();
        clustering2.getSolutionsByClusterWithMinObjective(0).get(0).setEvaluation(5);
        clustering2.getSolutionsByClusterWithMinObjective(0).get(1).setEvaluation(4);
        clustering2.getSolutionsByClusterWithMinObjective(0).get(2).setEvaluation(3);
        subjectiveAnalyzeAlgorithm.run(solutionSet2);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 22L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2L);
        Clustering clustering3 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering3.setNumClusters(3);
        clustering3.run();
        clustering3.getSolutionsByClusterWithMinObjective(0).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterWithMinObjective(0).get(1).setEvaluation(5);
        clustering3.getSolutionsByClusterWithMinObjective(0).get(2).setEvaluation(3);
        subjectiveAnalyzeAlgorithm.run(solutionSet3);

    }

    @Test
    public void agmOnMLP2ComAlteracoesNoPerfilDeAvaliacao() throws Exception {
        LOGGER.info("AGM MLP");
        Clustering.LOGGER.setLevel(Level.OFF);
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        List<QtdElements> elements = ExperimentTest.getElementsFromFile("agm_elements_03062019.csv");

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        SolutionSet solutionSet1 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 0L);
        Clustering clustering = new Clustering(solutionSet1, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(3);
        clustering.run();
        clustering.getSolutionsByClusterWithMinObjective(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterWithMinObjective(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterWithMinObjective(2).get(0).setEvaluation(2);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1, ClassifierAlgorithm.CLUSTERING_MLP);
        subjectiveAnalyzeAlgorithm.run(null);

        LOGGER.info("1º Interação SEM NOTAS");
        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 11L);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b);
//
        LOGGER.info("2º Interação COM NOTAS");
        //        2º Interação COM NOTAS
        SolutionSet solutionSet2 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 1L);
        Clustering clustering2 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering2.setNumClusters(3);
        clustering2.run();
        clustering2.getSolutionsByClusterWithMinObjective(1).get(0).setEvaluation(5);
        clustering2.getSolutionsByClusterWithMinObjective(1).get(1).setEvaluation(4);
        clustering2.getSolutionsByClusterWithMinObjective(1).get(2).setEvaluation(3);
        subjectiveAnalyzeAlgorithm.run(solutionSet2);

        LOGGER.info("2º Interação SEM NOTAS");
        //        2º Interação SEM NOTAS
        SolutionSet solutionSet2b = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 22L);
        subjectiveAnalyzeAlgorithm.run(solutionSet2b);

        LOGGER.info("3º Interação COM NOTAS");
        //        3º Interação COM NOTAS
        SolutionSet solutionSet3 = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, elements, 2L);
        Clustering clustering3 = new Clustering(solutionSet2, ClusteringAlgorithm.KMEANS);
        clustering3.setNumClusters(3);
        clustering3.run();
        clustering3.getSolutionsByClusterWithMinObjective(2).get(0).setEvaluation(5);
        clustering3.getSolutionsByClusterWithMinObjective(2).get(1).setEvaluation(5);
        clustering3.getSolutionsByClusterWithMinObjective(2).get(2).setEvaluation(3);
        subjectiveAnalyzeAlgorithm.run(solutionSet3);

    }


}
