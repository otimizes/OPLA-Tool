package br.otimizes.oplatool.core.jmetal4.main;

import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaII.NSGAII;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.Crossover;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverFactory;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.MutationFactory;
import br.otimizes.oplatool.core.jmetal4.operators.selection.Selection;
import br.otimizes.oplatool.core.jmetal4.operators.selection.SelectionFactory;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.domain.config.FileConstants;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class NSGAIICrossover {

    public static int populationSize_;
    public static int maxEvaluations_;
    public static double mutationProbability_;
    public static double crossoverProbability_;

    public static void main(String[] args) throws IOException, JMException, ClassNotFoundException {


        int runsNumber = 30; //30;
        populationSize_ = 100; //100;
        maxEvaluations_ = 30000; //300 gerações
        int[] discardedSolutions = new int[runsNumber];

        crossoverProbability_ = 0.1;
        mutationProbability_ = 0.9;
        String context = "OPLA";
        //Thelma - Dez2013 linha adicionada para identificar o algoritmo no nome do arquivo do hypervolume
        String moea = "NSGAII-MC";

        //File directory = new File("resultado/nsgaii/" + context);
        File directory = new File("experiment/OPLA/NSGA-II/FeatureCrossover" + FileConstants.FILE_SEPARATOR);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.out.println("Não foi possível criar o diretório do resultado");
                System.exit(0);
            }
        }


        String[] plas = new String[]{
                "/Users/elf/mestrado/sourcesMestrado/br.otimizes.oplatool.arquitetura/src/test/java/resources/agmfinal/agm.uml"};
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
            SolutionSet allSolutions = new SolutionSet();

            Crossover crossover;
            Mutation mutation;
            Selection selection;

            Map<String, Object> parameters; // Operator parameters


            algorithm = new NSGAII(problem);

            // Algorithm parameters
            algorithm.setInputParameter("populationSize", populationSize_);
            algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

            // Mutation and Crossover
            parameters = new HashMap<>();
            parameters.put("probability", crossoverProbability_);
            crossover = CrossoverFactory.getCrossoverOperator("PLACrossover", parameters);

            parameters = new HashMap<>();
            parameters.put("probability", mutationProbability_);
            mutation = MutationFactory.getMutationOperator("PLAMutationOperator", parameters);

            // Selection Operator
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            // Add the operators to the algorithm
            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
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
            int beginIndex = pla.lastIndexOf(FileConstants.FILE_SEPARATOR) + 1;
            int endIndex = pla.length() - 4;
            String PLAName = pla.substring(beginIndex, endIndex);

            long[] time = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {

                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                //System.out.println("Iruns: " + runs + "\tTotal time: " + estimatedTime);
                time[runs] = estimatedTime;

                resultFront = problem.removeDominated(resultFront);
                resultFront = problem.removeRepeated(resultFront);

                new OPLASolutionSet(resultFront).printObjectivesToFile(directory + "/FUN_" + PLAName + "_" + runs + ".txt");
                //resultFront.printVariablesToFile(directory + "/VAR_" + runs);
                MainTestUtil.printInformationToFile(allSolutions, directory + "/INFO_" + PLAName + "_" + runs + ".txt");
                // resultFront.saveVariablesToFile(directory + "/VAR_" + runs + "_");
                new OPLASolutionSet(resultFront).saveVariablesToFile("VAR_" + runs + "_");

                //armazena as solucoes de todas runs
                todasRuns = todasRuns.union(resultFront);


                //Thelma - Dez2013
                allSolutions = allSolutions.union(resultFront);
                MainTestUtil.printMetricsToFile(allSolutions, directory + "/Metrics_" + PLAName + "_" + runs + ".txt");
            }
            //Thelma - Dez2013 - duas proximas linhas
            String NameOfPLA = pla.substring(10, 15);
            new OPLASolutionSet(allSolutions).printObjectivesToFile(directory + "/Hypervolume/" + NameOfPLA + FileConstants.FILE_SEPARATOR + NameOfPLA + "_HV_" + moea + ".txt");

            MainTestUtil.printTimeToFile(directory + "/TIME_" + PLAName, runsNumber, time, pla);
            todasRuns = problem.removeDominated(todasRuns);
            todasRuns = problem.removeRepeated(todasRuns);


            System.out.println("------    All Runs - Non-dominated solutions --------");
            new OPLASolutionSet(todasRuns).printObjectivesToFile(directory + "/FUN_All_" + PLAName + ".txt");
            //todasRuns.printVariablesToFile(directory + "/VAR_All");
            MainTestUtil.printInformationToFile(allSolutions, directory + "/INFO_All_" + PLAName + ".txt");
            new OPLASolutionSet(todasRuns).saveVariablesToFile("VAR_All_");

            //Thelma - Dez2013
            MainTestUtil.printMetricsToFile(allSolutions, directory + "/Metrics_All_" + PLAName + ".txt");
            MainTestUtil.printAllMetricsToFile(allSolutions, directory + "/FUN_Metrics_All_" + PLAName + ".txt");

            MainTestUtil.printDiscardedSolutionsToFile(discardedSolutions, directory + "/AllDiscardedSolutions_" + PLAName + ".txt");

        }
    }
}
