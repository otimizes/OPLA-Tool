package br.ufpr.dinf.gres.opla.view;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.opla.config.ApplicationFile;
import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.opla.entity.Objective;
import br.ufpr.dinf.gres.opla.view.util.Utils;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import jmetal4.core.Variable;
import jmetal4.encodings.solutionType.ArchitectureSolutionType;
import jmetal4.experiments.NSGAIIConfig;
import jmetal4.experiments.OPLAConfigs;
import jmetal4.operators.mutation.PLAFeatureMutation;
import jmetal4.problems.OPLA;
import learning.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import utils.ExperimentTest;
import utils.QtdElements;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InitialInteractiveExperimentTest {


    public static final Logger LOGGER = Logger.getLogger(InitialInteractiveExperimentTest.class);

    @Test
    public void savePositionsUML() throws Exception {
        List<String> xmis = Arrays.asList(
                "/home/wmfsystem/oplatool/plas/agm/agm.uml"
        );

        ArchitectureBuilder architectureBuilder = new ArchitectureBuilder();
        List<Architecture> arrayList = xmis.stream().map(x -> {
            try {
                return architectureBuilder.create(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        SolutionSet solutionSet = new SolutionSet();
        Solution solution = new Solution();


        for (Architecture architecture : arrayList) {
            System.out.println();
            String i = "2";
            String path = "agm" + i;
            architecture.save(architecture, path, i);
            System.out.println(architecture.getName());
            Process process = Utils.executePapyrus("/home/wmfsystem/App/eclipse/eclipse", "/home/wmfsystem/oplatool/output/" + path + path + ".di");
            process.waitFor();
        }
    }

    @Test
    public void savePositionsUML2() throws Exception {
        List<String> xmis = Arrays.asList(
                "/home/wmfsystem/oplatool/plas/agm/agm.uml"
        );

        ArchitectureBuilder architectureBuilder = new ArchitectureBuilder();
        List<Architecture> arrayList = xmis.stream().map(x -> {
            try {
                return architectureBuilder.create(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        ManagerApplicationConfig instance = ApplicationFile.getInstance();
        NSGAIIConfig configs = new NSGAIIConfig();
        configs.setPopulationSize(20);
        configs.setClusteringAlgorithm(ClusteringAlgorithm.KMEANS);
        configs.setNumberOfRuns(300);
        configs.setOplaConfigs(new OPLAConfigs(Arrays.asList("COE", "ACLASS", "FM")));

        OPLA opla = new OPLA("/home/wmfsystem/oplatool/plas/agm/agm.uml", configs);
        SolutionSet solutionSet = new SolutionSet();
        solutionSet.setCapacity(1);
        Solution solution = new Solution(opla);
        solutionSet.add(solution);

        InteractiveSolutions interactiveSolutions = new InteractiveSolutions(instance, ClusteringAlgorithm.KMEANS, solutionSet);
        System.out.println("fim");
    }

    @Test
    public void fnCore() throws Exception {

        SolutionSet solutionSet1a = generateSolutionSet();

        LOGGER.info("1º Interação COM NOTAS");
        //        1º Interação COM NOTAS
        Clustering clustering = new Clustering(solutionSet1a, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(4);
        clustering.run();
        clustering.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        clustering.getSolutionsByClusterId(0).get(0).getOPLAProblem().getArchitecture_().getAllClasses().stream().findFirst().get().setFreeze();
        System.out.println("Deve Congelar " + clustering.getSolutionsByClusterId(0).get(0).getOPLAProblem().getArchitecture_().getAllClasses().stream().findFirst().get().getName());
        clustering.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        clustering.getSolutionsByClusterId(1).get(0).getOPLAProblem().getArchitecture_().getAllClasses().stream().findFirst().get().setFreeze();
        System.out.println("Deve Congelar " + clustering.getSolutionsByClusterId(1).get(0).getOPLAProblem().getArchitecture_().getAllClasses().stream().findFirst().get().getName());
        clustering.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        clustering.getSolutionsByClusterId(2).get(0).getOPLAProblem().getArchitecture_().getAllClasses().stream().findFirst().get().setFreeze();
        System.out.println("Deve Congelar " + clustering.getSolutionsByClusterId(2).get(0).getOPLAProblem().getArchitecture_().getAllClasses().stream().findFirst().get().getName());
        clustering.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        clustering.getSolutionsByClusterId(3).get(0).getOPLAProblem().getArchitecture_().getAllClasses().stream().findFirst().get().setFreeze();
        System.out.println("Deve Congelar " + clustering.getSolutionsByClusterId(3).get(0).getOPLAProblem().getArchitecture_().getAllClasses().stream().findFirst().get().getName());
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1a, ClassifierAlgorithm.CLUSTERING_MLP, DistributeUserEvaluation.MIDDLE);
        subjectiveAnalyzeAlgorithm.run(null);

        LOGGER.info("1º Interação SEM NOTAS");
//        //        1º Interação SEM NOTAS
        SolutionSet solutionSet1b = generateSolutionSet();
        subjectiveAnalyzeAlgorithm.run(solutionSet1b);


        System.out.println("aaaaa");


    }

    private SolutionSet generateSolutionSet() throws Exception {
        List<String> xmis = Arrays.asList(
                "/home/wmfsystem/oplatool/plas/agm/agm.uml"
        );


        ArchitectureBuilder architectureBuilder = new ArchitectureBuilder();
        List<Architecture> arrayList = xmis.stream().map(x -> {
            try {
                return architectureBuilder.create(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        ManagerApplicationConfig instance = ApplicationFile.getInstance();
        NSGAIIConfig configs = new NSGAIIConfig();
        configs.setPopulationSize(20);
        configs.setClusteringAlgorithm(ClusteringAlgorithm.KMEANS);
        configs.setNumberOfRuns(300);
        configs.setOplaConfigs(new OPLAConfigs(Arrays.asList("COE", "ACLASS", "FM")));

        SolutionSet solutionSet = new SolutionSet();
        int qtdSolutions = 30;
        solutionSet.setCapacity(qtdSolutions);

        for (int i = 0; i < qtdSolutions; i++) {
            OPLA opla = new OPLA("/home/wmfsystem/oplatool/plas/agm/agm.uml", configs);
            opla.setSelectedMetrics(Arrays.asList("featureDriven", "coe", "aclass"));
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("probability", 0.9);
            Solution solution = new Solution(opla);
            PLAFeatureMutation plaFeatureMutation = new PLAFeatureMutation(parameters, Arrays.asList("featureMutation", "moveMethodMutation", "addClassMutation", "moveAttributeMutation", "moveOperationMutation", "addManagerClassMutation"));
            plaFeatureMutation.execute(solution);
            opla.evaluate(solution);
            opla.evaluateConstraints(solution);
            solutionSet.add(solution);
        }
        return solutionSet;
    }
}
