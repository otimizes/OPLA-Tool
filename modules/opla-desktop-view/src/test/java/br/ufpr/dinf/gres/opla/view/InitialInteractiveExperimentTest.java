package br.ufpr.dinf.gres.opla.view;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Package;
import br.ufpr.dinf.gres.opla.config.ApplicationFile;
import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.opla.view.util.Utils;
import jmetal45.core.Solution;
import jmetal45.core.SolutionSet;
import jmetal45.experiments.NSGAIIConfig;
import jmetal45.experiments.OPLAConfigs;
import jmetal45.operators.mutation.PLAFeatureMutation;
import jmetal45.problems.OPLA;
import jmetal4.core.learning.*;
import org.apache.log4j.Logger;
import org.junit.Test;

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
        List<Element> freezedElements = solution.getAlternativeArchitecture().getFreezedElements();
        //        InteractiveSolutions interactiveSolutions = new InteractiveSolutions(instance, ClusteringAlgorithm.KMEANS, solutionSet);
        System.out.println("fim");
    }

//    @Test
    public void fnCore() throws Exception {

        SolutionSet solutionSet1a = generateSolutionSet();
//        solutionSet1a.get(0).getAlternativeArchitecture().getAllAtributtes();
        SolutionSet solutionSet1b = generateSolutionSet();
        SolutionSet solutionSet1c = generateSolutionSet();

        Clustering clustering = new Clustering(solutionSet1a, ClusteringAlgorithm.KMEANS);
        clustering.setNumClusters(4);
        clustering.run();
        clustering.getSolutionsByClusterId(0).get(0).setEvaluation(5);
        for (Package allClass : clustering.getSolutionsByClusterId(0).get(0).getAlternativeArchitecture().getAllPackagesAllowedMofification()) {
            if (allClass.getName().equals("GameBoardGUI")) {
                allClass.setFreezeFromDM();
                System.out.println("Deve congelar " + allClass.getName());
            }
        }
        clustering.getSolutionsByClusterId(1).get(0).setEvaluation(3);
        for (Package allClass : clustering.getSolutionsByClusterId(1).get(0).getAlternativeArchitecture().getAllPackages()) {
            if (allClass.getName().equals("GameBoardGUI")) {
                allClass.setFreezeFromDM();
                System.out.println("Deve congelar " + allClass.getName());
            }
        }
        clustering.getSolutionsByClusterId(2).get(0).setEvaluation(2);
        for (Package allClass : clustering.getSolutionsByClusterId(2).get(0).getAlternativeArchitecture().getAllPackagesAllowedMofification()) {
            if (allClass.getName().equals("GameBoardGUI")) {
                allClass.setFreezeFromDM();
                System.out.println("Deve congelar " + allClass.getName());
            }
        }
        clustering.getSolutionsByClusterId(3).get(0).setEvaluation(2);
        for (Package allClass : clustering.getSolutionsByClusterId(3).get(0).getAlternativeArchitecture().getAllPackagesAllowedMofification()) {
            if (allClass.getName().equals("GameBoardGUI")) {
                allClass.setFreezeFromDM();
                System.out.println("Deve congelar " + allClass.getName());
            }
        }


        for (Solution solution : solutionSet1a.getSolutionSet()) {
            for (Element freezedElement : solution.getAlternativeArchitecture().getFreezedElements()) {
                if (freezedElement.isFreezeByDM()) {
                    System.out.println("freeze------------> " + freezedElement.getName());
                }
            }
        }

//        for (Solution solution : solutionSet1a.getSolutionSet()) {
//            for (Element elementsWithPackage : solution.getAlternativeArchitecture().getElementsWithPackages()) {
//                elementsWithPackage.unsetFreeze();
//                if (elementsWithPackage.isFreeze()) {
//                    System.out.println("CONGELADO " + elementsWithPackage.getName());
//                }
//            }
//        }

        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(solutionSet1a, ClassifierAlgorithm.CLUSTERING_MLP, DistributeUserEvaluation.MIDDLE);

        subjectiveAnalyzeAlgorithm.run(null, false);
        subjectiveAnalyzeAlgorithm.run(solutionSet1b, false);
        subjectiveAnalyzeAlgorithm.run(solutionSet1c, false);

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
        configs.setPopulationSize(200);
        configs.setClusteringAlgorithm(ClusteringAlgorithm.KMEANS);
        configs.setNumberOfRuns(3000);
        configs.setOplaConfigs(new OPLAConfigs(Arrays.asList("COE", "ACLASS", "FM")));

        SolutionSet solutionSet = new SolutionSet();
        int qtdSolutions = 200;
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
