package jmetal4.experiments;

import jmetal4.core.Algorithm;
import jmetal4.core.SolutionSet;
import jmetal4.metaheuristics.paes.PAES;
import jmetal4.operators.crossover.Crossover;
import jmetal4.operators.mutation.Mutation;
import jmetal4.operators.mutation.MutationFactory;
import jmetal4.operators.selection.Selection;
import jmetal4.operators.selection.SelectionFactory;
import jmetal4.problems.OPLA;
import jmetal4.util.JMException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class PAES_OPLA_FeatMut {

    public static int populationSize_;
    public static int maxEvaluations_;
    public static double mutationProbability_;
    public static double crossoverProbability_;

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException,
            ClassNotFoundException {

        int runsNumber = 30; // 30;
        maxEvaluations_ = 30000; // 300 gera��es
        int archiveSize = 100;
        int biSections = 5;
        mutationProbability_ = 0.8;
        String context = "OPLA";

        File directory = new File("experiment/OPLA/PAES/FeatureMutation" + "/");
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                System.out.println("N�o foi poss�vel criar o diret�rio do resultado");
                System.exit(0);
            }
        }

        String plas[] = new String[]{"resources/AGM-Final-concerns.xmi", "resources/AGM-improvement.xmi",
                "resources/MM-v6-completa.xmi", "resources/LPS-BET.xmi", "resources/MM-Final.xmi"};
        String xmiFilePath;

        for (String pla : plas) {

            xmiFilePath = pla;

            OPLA problem = null;
            try {
                problem = new OPLA(xmiFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Algorithm algorithm;
            SolutionSet todasRuns = new SolutionSet();

            Crossover crossover;
            Mutation mutation;
            Selection selection;

            HashMap parameters; // Operator parameters

            algorithm = new PAES(problem);

            // Algorithm parameters
            algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
            algorithm.setInputParameter("archiveSize", archiveSize);
            algorithm.setInputParameter("biSections", biSections);

            // Mutation

            parameters = new HashMap();
            parameters.put("probability", mutationProbability_);
            mutation = MutationFactory.getMutationOperator("PLAFeatureMutation", parameters);

            // Selection Operator
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            // Add the operators to the algorithm
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            System.out.println("\n================ PAES ================");
            System.out.println("Context: " + context);
            System.out.println("PLA: " + pla);
            System.out.println("Params:");
            System.out.println("\tMaxEva -> " + maxEvaluations_);
            System.out.println("\tMuta -> " + mutationProbability_);

            long heapSize = Runtime.getRuntime().totalMemory();
            heapSize = (heapSize / 1024) / 1024;
            System.out.println("Heap Size: " + heapSize + "Mb\n");

            String PLAName = pla.substring(10, 18);
            long time[] = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {

                // Execute the Algorithm

                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                // System.out.println("Iruns: " + runs + "\tTotal time: " +
                // estimatedTime);
                time[runs] = estimatedTime;

                resultFront = problem.removeDominadas(resultFront);
                resultFront = problem.removeRepetidas(resultFront);

                resultFront.printObjectivesToFile(directory + "/FUN_" + PLAName + "_" + runs + ".txt");
                // resultFront.printVariablesToFile(directory + "/VAR_" + runs);
                resultFront.printInformationToFile(directory + "/INFO_" + PLAName + "_" + runs + ".txt");
                // resultFront.saveVariablesToFile(directory + "/VAR_" + runs +
                // "_");
                resultFront.saveVariablesToFile("VAR_" + runs + "_");

                // armazena as solucoes de todas runs
                todasRuns = todasRuns.union(resultFront);

            }

            todasRuns.printTimeToFile(directory + "/TIME_" + PLAName, runsNumber, time, pla);

            todasRuns = problem.removeDominadas(todasRuns);
            todasRuns = problem.removeRepetidas(todasRuns);

            System.out.println("------    All Runs - Non-dominated solutions --------");
            todasRuns.printObjectivesToFile(directory + "/FUN_All_" + PLAName + ".txt");
            // todasRuns.printVariablesToFile(directory + "/VAR_All");
            todasRuns.printInformationToFile(directory + "/INFO_All_" + PLAName + ".txt");
            todasRuns.saveVariablesToFile("VAR_All_");
        }
    }
    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
}