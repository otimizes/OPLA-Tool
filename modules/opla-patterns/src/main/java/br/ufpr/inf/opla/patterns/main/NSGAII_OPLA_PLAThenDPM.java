package br.ufpr.inf.opla.patterns.main;

import arquitetura.io.ReaderConfig;
import br.ufpr.inf.opla.patterns.factory.MutationOperatorFactory;
import br.ufpr.inf.opla.patterns.indicadores.Hypervolume;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import jmetal4.metaheuristics.nsgaII.NSGAII;
import jmetal4.operators.crossover.Crossover;
import jmetal4.operators.crossover.CrossoverFactory;
import jmetal4.operators.mutation.Mutation;
import jmetal4.operators.selection.Selection;
import jmetal4.operators.selection.SelectionFactory;
import jmetal4.problems.OPLA;
import jmetal4.util.JMException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NSGAII_OPLA_PLAThenDPM {

    public static int populationSize_;
    public static int maxEvaluations_;
    public static double mutationProbability_;
    public static double crossoverProbability_;

    //--  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {

//        args = new String[]{"100", "1000", "0.9", ArchitectureRepository.AGM, "Teste", "false"};
        if (args.length < 6) {
            System.out.println("You need to inform the following parameters:");
            System.out.println("\t1 - Population Size (Integer);"
                    + "\n\t2 - Max Evaluations (Integer);"
                    + "\n\t3 - Mutation Probability (Double);"
                    + "\n\t4 - PLA path;"
                    + "\n\t5 - Context;"
                    + "\n\t6 - If you want to write the variables (Boolean).");
            System.exit(0);
        }

        int runsNumber = 30; //30;
        if (args[0] == null || args[0].trim().equals("")) {
            System.out.println("Missing population size argument.");
            System.exit(1);
        }
        try {
            populationSize_ = Integer.valueOf(args[0]); //100;
        } catch (NumberFormatException ex) {
            System.out.println("Population size argument not integer.");
            System.exit(1);
        }
        if (args[1] == null || args[1].trim().equals("")) {
            System.out.println("Missing max evaluations argument.");
            System.exit(1);
        }
        try {
            maxEvaluations_ = Integer.valueOf(args[1]); //300 geraçõeshttp://loggr.net/
        } catch (NumberFormatException ex) {
            System.out.println("Max evaluations argument not integer.");
            System.exit(1);
        }
        crossoverProbability_ = 0.0;
        if (args[2] == null || args[2].trim().equals("")) {
            System.out.println("Missing mutation probability argument.");
            System.exit(1);
        }
        try {
            mutationProbability_ = Double.valueOf(args[2]);
        } catch (NumberFormatException ex) {
            System.out.println("Mutation probability argument not double.");
            System.exit(1);
        }

        Map<String, Object> parameters; // Operator parameters
        parameters = new HashMap<>();
        parameters.put("probability", mutationProbability_);
        Mutation designPatternMutation = MutationOperatorFactory.create("DesignPatternsMutationOperator", parameters);
        Mutation plaMutation = MutationOperatorFactory.create("PLAMutation", parameters);

        if (args[3] == null || args[3].trim().equals("")) {
            System.out.println("Missing PLA Path argument.");
            System.exit(1);
        }
        String pla = args[3];

        if (args[4] == null || args[4].trim().equals("")) {
            System.out.println("Missing context argument.");
            System.exit(1);
        }
        String context = args[4];

        if (args[5] == null || args[5].trim().equals("")) {
            System.out.println("Missing print variables argument.");
            System.exit(1);
        }
        boolean shouldPrintVariables = Boolean.valueOf(args[5]);

        String plaName = getPlaName(pla);

        File directory = ArchitectureRepository.getOrCreateDirectory("experiment/" + plaName + "/" + context + "/");
        ArchitectureRepository.getOrCreateDirectory("experiment/" + plaName + "/" + context + "/manipulation");
        ArchitectureRepository.getOrCreateDirectory("experiment/" + plaName + "/" + context + "/output");

        ReaderConfig.setDirTarget("experiment/" + plaName + "/" + context + "/manipulation");
        ReaderConfig.setDirExportTarget("experiment/" + plaName + "/" + context + "/output");

        String plaDirectory = getPlaDirectory(pla);
        ReaderConfig.setPathToTemplateModelsDirectory(plaDirectory);
        ReaderConfig.setPathToProfileSMarty(plaDirectory + "smarty.profile.uml");
        ReaderConfig.setPathToProfileConcerns(plaDirectory + "concerns.profile.uml");
        ReaderConfig.setPathProfileRelationship(plaDirectory + "relationships.profile.uml");
        ReaderConfig.setPathToProfilePatterns(plaDirectory + "patterns.profile.uml");

        String xmiFilePath = pla;

        OPLA problem = null;
        try {
            problem = new OPLA(xmiFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NSGAII algorithm;
        SolutionSet todasRuns = new SolutionSet();
        // Thelma - Dez2013 - adicao da linha abaixo
        SolutionSet allSolutions = new SolutionSet();

        Crossover crossover;
        Selection selection;

        algorithm = new NSGAII(problem);

        // Algorithm parameters
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_ / 2);

        // Mutation and Crossover
        parameters = new HashMap<>();
        parameters.put("probability", crossoverProbability_);
        crossover = CrossoverFactory.getCrossoverOperator("PLACrossover", parameters);

        // Selection Operator 
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);

        algorithm.addOperator("mutation", plaMutation);
        algorithm.addOperator("selection", selection);

        System.out.println("\n================ NSGAII ================");
        System.out.println("Context: " + context);
        System.out.println("PLA: " + pla);
        System.out.println("Params:");
        System.out.println("\tPop -> " + populationSize_);
        System.out.println("\tMaxEva -> " + maxEvaluations_);
        System.out.println("\tCross -> " + crossoverProbability_);
        System.out.println("\tMuta -> " + mutationProbability_);

        long heapSize = Runtime.getRuntime().totalMemory();
        heapSize = (heapSize / 1024) / 1024;
        System.out.println("Heap Size: " + heapSize + "Mb\n");

        long time[] = new long[runsNumber];

        Hypervolume.clearFile(directory + "/HYPERVOLUME.txt");

        for (int runs = 0; runs < runsNumber; runs++) {

            // Execute the Algorithm PLA Mutation
            long initTime = System.currentTimeMillis();
            SolutionSet resultFront = algorithm.execute();

            resultFront = problem.removeDominadas(resultFront);
            resultFront = problem.removeRepetidas(resultFront);

            //Parte do algoritmo que executa o operador de mutação de padrões de projeto
            algorithm.addOperator("mutation", designPatternMutation);
            {
                SolutionSet tempResultFront = new SolutionSet(populationSize_);
                for (Iterator<Solution> it = resultFront.iterator(); it.hasNext(); ) {
                    Solution solution = it.next();
                    tempResultFront.add(solution);
                }
                resultFront = tempResultFront;
            }
            resultFront = algorithm.execute(resultFront, 0);
            long estimatedTime = System.currentTimeMillis() - initTime;

            resultFront = problem.removeDominadas(resultFront);
            resultFront = problem.removeRepetidas(resultFront);

            //System.out.println("Iruns: " + runs + "\tTotal time: " + estimatedTime);
            time[runs] = estimatedTime;

            resultFront.printObjectivesToFile(directory + "/FUN_" + plaName + "_" + runs + ".txt");
            //resultFront.printVariablesToFile(directory + "/VAR_" + runs);
            resultFront.printInformationToFile(directory + "/INFO_" + plaName + "_" + runs + ".txt");
            // resultFront.saveVariablesToFile(directory + "/VAR_" + runs + "_");
            if (shouldPrintVariables) {
                resultFront.saveVariablesToFile("VAR_" + runs + "_");
            }

            Hypervolume.printFormatedHypervolumeFile(resultFront, directory + "/HYPERVOLUME.txt", true);

            //armazena as solucoes de todas runs
            todasRuns = todasRuns.union(resultFront);

            //Thelma - Dez2013
            allSolutions = allSolutions.union(resultFront);
            resultFront.printMetricsToFile(directory + "/Metrics_" + plaName + "_" + runs + ".txt");

        }

        todasRuns.printTimeToFile(directory + "/TIME_" + plaName, runsNumber, time, pla);

        todasRuns = problem.removeDominadas(todasRuns);
        todasRuns = problem.removeRepetidas(todasRuns);

        System.out.println("------    All Runs - Non-dominated solutions --------");
        todasRuns.printObjectivesToFile(directory + "/FUN_All_" + plaName + ".txt");
        //todasRuns.printVariablesToFile(directory + "/VAR_All");
        todasRuns.printInformationToFile(directory + "/INFO_All_" + plaName + ".txt");
        //todasRuns.saveVariablesToFile(directory + "/VAR_All_");
        if (shouldPrintVariables) {
            todasRuns.saveVariablesToFile("VAR_All_");
        }

        //Thelma - Dez2013
        todasRuns.printMetricsToFile(directory + "/Metrics_All_" + plaName + ".txt");
        todasRuns.printAllMetricsToFile(directory + "/FUN_Metrics_All_" + plaName + ".txt");

    }

    public static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf('/') + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public static String getPlaDirectory(String pla) {
        int stop = pla.lastIndexOf('/');
        return pla.substring(0, stop + 1);
    }

}
