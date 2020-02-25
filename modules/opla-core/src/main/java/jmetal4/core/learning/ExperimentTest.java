package jmetal4.core.learning;

import arquitetura.flyweights.VariabilityFlyweight;
import arquitetura.flyweights.VariantFlyweight;
import arquitetura.flyweights.VariationPointFlyweight;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.*;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.*;
import br.ufpr.dinf.gres.opla.entity.Experiment;
import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.core.Solution;
import jmetal4.core.core.SolutionSet;
import jmetal4.core.encodings.solutionType.ArchitectureSolutionType;
import jmetal4.core.problems.OPLA;
import liquibase.util.csv.CSVReader;
import org.apache.commons.lang.RandomStringUtils;
import jmetal4.utils.MathUtils;
import utils.QtdElements;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExperimentTest {

    public static SolutionSet getSolutionSetFromObjectiveList(Experiment experiment, List<String> metrics, List<Objective> objectives) {
        SolutionSet solutionSet = new SolutionSet();
        Architecture architecture = new Architecture(experiment.getName());
        objectives.forEach(objective -> {
            if (objective.getExecution() != null) {
                Solution solution = new Solution();
                solution.createObjective(3);
                String[] split = objective.getObjectives().split("\\|");
                for (int i = 0; i < split.length; i++) {
                    solution.setObjective(i, Double.parseDouble(split[i]));
                }

                try {
                    solution.setType(new ArchitectureSolutionType(new OPLA()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                solution.setProblem(new OPLA());
                ((OPLA)solution.getProblem()).setArchitecture_(architecture);
                ((OPLA)solution.getProblem()).setSelectedMetrics(metrics);

                solution.setSolutionName(objective.getSolutionName());
                solution.setExecutionId(objective.getExecution() != null ? objective.getExecution().getId() : 0);
                solution.setNumberOfObjectives(split.length);
                solutionSet.getSolutionSet().add(solution);
            }
        });
        return solutionSet;
    }


    public static SolutionSet getSolutionSetFromObjectiveListTest(List<Objective> objectives, List<QtdElements> elements, Long id) {

        File smarty = new File(Thread.currentThread().getContextClassLoader().getResource("smarty.profile.uml").getFile());
        File concerns = new File(Thread.currentThread().getContextClassLoader().getResource("concerns.profile.uml").getFile());
        File patterns = new File(Thread.currentThread().getContextClassLoader().getResource("patterns.profile.uml").getFile());
        File relationships = new File(Thread.currentThread().getContextClassLoader().getResource("relationships.profile.uml").getFile());
        ReaderConfig.setPathToProfileSMarty(smarty.toURI().getPath());
        ReaderConfig.setPathToProfileConcerns(concerns.toURI().getPath());
        ReaderConfig.setPathToProfilePatterns(patterns.toURI().getPath());
        ReaderConfig.setPathProfileRelationship(relationships.toURI().getPath());

        SolutionSet solutionSet = new SolutionSet();
        Architecture architecture = new Architecture("Teste");
        AtomicInteger i = new AtomicInteger();
        objectives.forEach(objective -> {
            if (objective.getExecution() != null && Objects.equals(objective.getExecution().getId(), id)) {
                Solution solution = new Solution();
                solution.createObjective(3);
                String[] split = objective.getObjectives().split("\\|");
                solution.setObjective(0, Double.parseDouble(split[0]));
                solution.setObjective(1, Double.parseDouble(split[1]));
                solution.setObjective(2, Double.parseDouble(split[2]));

                try {
                    solution.setType(new ArchitectureSolutionType(new OPLA()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                solution.setProblem(new OPLA());
                ((OPLA)solution.getProblem()).setArchitecture_(architecture);
                ((OPLA)solution.getProblem()).setSelectedMetrics(Arrays.asList("featureDriven", "aclass", "coe"));

                solution.setSolutionName(objective.getSolutionName());
                solution.setExecutionId(objective.getExecution() != null ? objective.getExecution().getId() : 0);
                solution.setNumberOfObjectives(3);


                if (elements != null) {

                    QtdElements qtdElements = elements.get(i.get());
                    ((OPLA)solution.getProblem()).getArchitecture_().addAllClasses(generateRandomClass(qtdElements.classes));
                    ((OPLA)solution.getProblem()).getArchitecture_().addAllInterfaces(generateRandomInterface(qtdElements.interfaces));
                    ((OPLA)solution.getProblem()).getArchitecture_().addAllPackages(generateRandomPackage(qtdElements.packages));
                    VariationPointFlyweight.getInstance().setVariationPoints(generateVariationPoints(qtdElements.variationPoints));
                    VariantFlyweight.getInstance().setVariants(generateVariant(qtdElements.variants));
                    VariabilityFlyweight.getInstance().setVariabilities(generateVariabilities(qtdElements.variabilities));
                    generateConcerns(qtdElements.concerns);

                    for (int j = 0; j < qtdElements.realizations; j++) {
                        ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().addRelationship(new RealizationRelationship());
                    }
                    for (int j = 0; j < qtdElements.generalizations; j++) {
                        ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().addRelationship(new GeneralizationRelationship());
                    }
                    for (int j = 0; j < qtdElements.dependencies; j++) {
                        ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().addRelationship(new DependencyRelationship());
                    }
                    for (int j = 0; j < qtdElements.agregations; j++) {
                        AssociationRelationship associationRelationship = new AssociationRelationship();
                        AssociationEnd associationEnd = new AssociationEnd();
                        associationEnd.setAggregation("shared");
                        AssociationEnd associationEnd2 = new AssociationEnd();
                        associationEnd2.setAggregation("shared");
                        associationRelationship.getParticipants().add(associationEnd);
                        associationRelationship.getParticipants().add(associationEnd2);
                        ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().addRelationship(associationRelationship);
                    }
                    for (int j = 0; j < qtdElements.compositions; j++) {
                        AssociationRelationship associationRelationship = new AssociationRelationship();
                        AssociationEnd associationEnd = new AssociationEnd();
                        associationEnd.setAggregation("composite");
                        AssociationEnd associationEnd2 = new AssociationEnd();
                        associationEnd2.setAggregation("composite");
                        associationRelationship.getParticipants().add(associationEnd);
                        associationRelationship.getParticipants().add(associationEnd2);
                        ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().addRelationship(associationRelationship);
                    }
                    int a = ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().getAllAssociations().size();
                    if (a < qtdElements.associations) {
                        for (int j = 0; j < qtdElements.associations - a; j++) {
                            AssociationRelationship associationRelationship = new AssociationRelationship();
                            AssociationEnd associationEnd = new AssociationEnd();
                            associationEnd.setAggregation("none");
                            AssociationEnd associationEnd2 = new AssociationEnd();
                            associationEnd2.setAggregation("none");
                            associationRelationship.getParticipants().add(associationEnd);
                            associationRelationship.getParticipants().add(associationEnd2);
                            ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().addRelationship(associationRelationship);
                        }
                    }
                    for (int j = 0; j < qtdElements.abstractions; j++) {
                        ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().addRelationship(new AbstractionRelationship());
                    }
                    for (int j = 0; j < qtdElements.usage; j++) {
                        ((OPLA)solution.getProblem()).architecture_.getRelationshipHolder().addRelationship(new UsageRelationship());
                    }



                }

                solutionSet.getSolutionSet().add(solution);
                i.getAndIncrement();
            }
        });
        return solutionSet;
    }

    public static HashMap<String, VariationPoint> generateVariationPoints(int qtd) {
        HashMap<String, VariationPoint> objects = new HashMap<>();
        for (int j = 0; j < qtd; j++) {
            objects.put(j + "", null);
        }
        return objects;
    }

    public static HashMap<String, Variant> generateVariant(int qtd) {
        HashMap<String, Variant> objects = new HashMap<>();
        for (int j = 0; j < qtd; j++) {
            objects.put(j + "", null);
        }
        return objects;
    }

    public static HashMap<String, Variability> generateVariabilities(int qtd) {
        HashMap<String, Variability> objects = new HashMap<>();
        for (int j = 0; j < qtd; j++) {
            objects.put(j + "", null);
        }
        return objects;
    }

    public static HashMap<String, Concern> generateConcerns(int qtd) {
        HashMap<String, Concern> objects = new HashMap<String, Concern>();
        for (int j = 0; j < qtd; j++) {
            objects.put(j + "", null);
            ConcernHolder.INSTANCE.getConcerns().put(j + "", null);
        }
        return objects;
    }

    public static Set<Class> generateRandomClass(int classes) {
        HashSet<Class> objects = new HashSet<>();
        for (int j = 0; j < classes; j++) {
            objects.add(new Class(null, RandomStringUtils.random(10),false,null));
        }
        return objects;
    }

    public static Set<Package> generateRandomPackage(int packages) {
        HashSet<Package> objects = new HashSet<>();
        for (int j = 0; j < packages; j++) {
            objects.add(new Package(null,RandomStringUtils.random(10), null));
        }
        return objects;
    }

    public static Set<Concern> generateRandomConcern(int concerns) {
        HashSet<Concern> objects = new HashSet<>();
        for (int j = 0; j < concerns; j++) {
            objects.add(new Concern(RandomStringUtils.random(10)));
        }
        return objects;
    }

    public static Set<Interface> generateRandomInterface(int interfaces) {
        HashSet<Interface> objects = new HashSet<>();
        for (int j = 0; j < interfaces; j++) {
            objects.add(new Interface(null, RandomStringUtils.random(10)));
        }
        return objects;
    }



    public static List<Objective> getObjectivesFromFile(String filename) throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextLine;
        List<Objective> objectives = new ArrayList<>();
        while ((nextLine = reader.readNext()) != null) {
            objectives.add(new Objective(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5]));
        }
        return objectives;
    }

    public static List<QtdElements> getElementsFromFile(String filename) throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextLine;
        List<QtdElements> objects= new ArrayList<>();
        while ((nextLine = reader.readNext()) != null) {
            objects.add(new QtdElements(nextLine));
        }
        return objects;
    }

    public static String getNormalizedNewObjectivesOfClusteredSolutions(SolutionSet run) {
        List<List<Double>> result = MathUtils.normalize(run);
        Long lastExec = null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            builder.append(result.get(i).toString().replace("[", "").replace("]", "").replaceAll(",", "") + "\n");
            if (!run.getSolutionSet().get(i).getExecutionId().equals(lastExec)) {
                lastExec = run.getSolutionSet().get(i).getExecutionId();
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public static List<List<Double>> getListXYZ(SolutionSet result) {
        List<List<Double>> listXYZ = new ArrayList<>();
        listXYZ.add(new ArrayList<>());
        listXYZ.add(new ArrayList<>());
        listXYZ.add(new ArrayList<>());
        result.getSolutionSet().forEach(r -> {
            listXYZ.get(0).add(r.getObjective(0));
            listXYZ.get(1).add(r.getObjective(1));
            listXYZ.get(2).add(r.getObjective(2));
        });
        return listXYZ;
    }

    public static List<Double> calculateHypervolume(SolutionSet run) throws IOException {
        String newObjectivesOfClusteredSolutions = getNormalizedNewObjectivesOfClusteredSolutions(run);
        File tempFile = File.createTempFile("opla-", "-test");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(newObjectivesOfClusteredSolutions);
        bw.close();

        List<Double> hypervolume = hypervolume("1.01 1.01 1.01", tempFile.toString());

        tempFile.deleteOnExit();
        return hypervolume;
    }

    public static List<Double> hypervolume(String referencePoint, String pathToFile) throws IOException {
        String hyperVolumeBin = getHome() + "/bins/./hv";
        //String hyperVolumeBin = UserHome.getOplaUserHome() + "bins/hv";
        ProcessBuilder builder = new ProcessBuilder(hyperVolumeBin, "-r", referencePoint, pathToFile);
        builder.redirectErrorStream(true);
        Process p = builder.start();


        return inheritIO(p.getInputStream());

    }

    public static String getHome() {
        return System.getProperties().get("user.home") + "/oplatool";
    }

    public static List<Double> inheritIO(final InputStream src) {
        List<Double> values = new ArrayList<>();

        Scanner sc = new Scanner(src);
        while (sc.hasNextLine())
            values.add(Double.valueOf(sc.nextLine()));

        return values;
    }

    public void showMinPointEpsilonPossibilities(SolutionSet solutionSet) throws Exception {
        for (int minPoints = 3; minPoints <= 3; minPoints++) {
            for (double epsilon = 0.3; epsilon <= 0.4; epsilon += 0.01) {
                System.out.println("-----------------------------------------------------------------------------------------------");
                System.out.println("MINPOINTS: " + minPoints + "; " + "EPSILON: " + epsilon);
                Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.DBSCAN);
                clustering.setEpsilon(epsilon);
                clustering.setMinPoints(minPoints);
                SolutionSet run = clustering.run();
            }
        }
    }
}
