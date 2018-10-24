package br.ufpr.inf.opla.patterns.indicadores;

import jmetal4.core.SolutionSet;
import jmetal4.qualityIndicator.util.MetricsUtil;
import jmetal4.util.JMException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeraTudoAKAGodClass {

    private static final Logger LOGGER = Logger.getLogger(GeraTudoAKAGodClass.class);

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    public static void main(String[] args)
            throws FileNotFoundException, IOException, JMException, ClassNotFoundException, InterruptedException {
        String[] plas = {
                // "MicrowaveOvenSoftware",
                // "ServiceAndSupportSystem",
                "agm"};

        String[] contexts = {"PLAMutation", "OnlyBridgeMutation", "OnlyMediatorMutation", "OnlyPatternsMutation",
                "OnlyPatternsMutationNR", "OnlyStrategyMutation", "PLAMutationWithBridge", "PLAMutationWithMediator",
                "PLAMutationWithPatterns", "PLAMutationWithPatternsNR", "PLAMutationWithStrategy"};

        MetricsUtil mu = new MetricsUtil();

        for (String pla : plas) {

            String directoryPath = "experiment/" + pla + "/";

            try (FileWriter funAll = new FileWriter(directoryPath + "FUN_All_" + pla + ".txt")) {
                for (String contexto : contexts) {
                    double[][] front = mu.readFront(directoryPath + contexto + "/" + "FUN_All_" + pla + ".txt");
                    for (double[] solucao : front) {
                        funAll.write(solucao[0] + " " + solucao[1] + "\n");
                    }
                }
            }

            executaHypervolume(directoryPath, pla, contexts);
            runFriedman(directoryPath, contexts);
            runWilcoxon(directoryPath, contexts);
            executeEuclideanDistance(directoryPath, pla, contexts);
        }
    }

    private static void executeEuclideanDistance(String directoryPath, String pla, String[] contexts)
            throws IOException {
        MetricsUtil mu = new MetricsUtil();
        SolutionSet ss = mu.readNonDominatedSolutionSet(directoryPath + "FUN_All_" + pla + ".txt");
        ss = removeDominadas(ss);
        ss.printObjectivesToFile(directoryPath + "FUN_All_" + pla + ".txt");

        double[] min = mu.getMinimumValues(ss.writeObjectivesToMatrix(), 2);
        try (FileWriter todosEds = new FileWriter(directoryPath + "ALL_ED_" + pla + ".txt")) {
            try (FileWriter menoresEds = new FileWriter(directoryPath + "MIN_ED_" + pla + ".txt")) {
                todosEds.write("--- " + min[0] + " " + min[1] + " ---" + "\n");
                for (String contexto : contexts) {
                    List<Integer> melhoresSolucoesPorContexto = new ArrayList<>();
                    double menorDistancia = Double.MAX_VALUE;

                    // double[][] front = new
                    // double[quantidadeSolucoes][numObjetivos];
                    // for(solucoes)for(objetivos)solucoes[solucaoI][numObjJ] =
                    // valor do banco;
                    double[][] front = mu.readFront(directoryPath + contexto + "/" + "FUN_All_" + pla + ".txt");
                    for (int i = 0; i < front.length; i++) {
                        double distanciaEuclidiana = mu.distance(min, front[i]);
                        todosEds.write(distanciaEuclidiana + "\n");
                        if (distanciaEuclidiana < menorDistancia) {
                            menorDistancia = distanciaEuclidiana;
                            melhoresSolucoesPorContexto.clear();
                        }
                        if (distanciaEuclidiana == menorDistancia) {
                            melhoresSolucoesPorContexto.add(i);
                        }
                    }

                    for (Integer melhorSolucao : melhoresSolucoesPorContexto) {
                        menoresEds.write(contexto + ": " + melhorSolucao + " - ED: " + menorDistancia + "\n");
                    }
                }
            }
        }
    }

    private static void executaHypervolume(String directoryPath, String pla, String[] contexts)
            throws IOException, InterruptedException {
        MetricsUtil mu = new MetricsUtil();
        double[] referencePoint = Hypervolume.printReferencePoint(
                mu.readFront(directoryPath + "FUN_All_" + pla + ".txt"), directoryPath + "/HYPERVOLUME_REFERENCE.txt",
                2);

        try (FileWriter sh = new FileWriter(directoryPath + "run_hypervolume.sh")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("#!/bin/sh\n");
            for (String context : contexts) {
                stringBuilder.append("\n");
                stringBuilder.append("#-----------------------------------------\n");
                stringBuilder.append("system=").append(directoryPath).append(context).append("\n");
                stringBuilder.append("reference=\"");
                for (double d : referencePoint) {
                    stringBuilder.append(Double.toString(d + 0.1D)).append(" ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append("\"\n");
                stringBuilder.append("\n");
                stringBuilder.append("echo \"$system\"\n");
                stringBuilder.append("FILES=./$system/HYPERVOLUME.txt\n");
                stringBuilder.append("for f in $FILES\n");
                stringBuilder.append("do\n");
                stringBuilder.append("\techo \"Processing $f\"\n");
                stringBuilder.append(
                        "\t./experiment/hv-1.3-src/hv -r \"$reference\" $f >> ./$system/HYPERVOLUME_RESULT.txt\n");
                stringBuilder.append("done\n");
                stringBuilder.append("echo \"\\n\"\n");
            }
            sh.write(stringBuilder.toString());
        }

        for (String context : contexts) {
            Hypervolume.clearFile(directoryPath + context + "/HYPERVOLUME_RESULT.txt");
        }

        ProcessBuilder processBuilder = new ProcessBuilder("sh", "./" + directoryPath + "run_hypervolume.sh");
        Process process = processBuilder.start();
        process.waitFor();
    }

    public static void runFriedman(String directoryPath, String[] contexts) throws IOException, InterruptedException {
        try (FileWriter friedman = new FileWriter(directoryPath + "friedman_script.txt")) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String context : contexts) {
                stringBuilder.append(context).append("<- c(");
                try (Scanner scanner = new Scanner(
                        new FileInputStream(directoryPath + context + "/HYPERVOLUME_RESULT.txt"))) {
                    while (scanner.hasNextLine()) {
                        String value = scanner.nextLine().trim();
                        if (!value.isEmpty()) {
                            stringBuilder.append(value).append(", ");
                        }
                    }
                } catch (Exception e) {
                    LOGGER.info(e);
                }
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                stringBuilder.append(")\n");
                stringBuilder.append("\n");
            }

            stringBuilder.append("require(pgirmess)\n");
            stringBuilder.append("AR1 <-cbind(");

            StringBuilder contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append(context).append(", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append(contextNames.toString()).append(")\n");
            stringBuilder.append("result<-friedman.test(AR1)\n");
            stringBuilder.append("\n");
            stringBuilder.append("m<-data.frame(result$statistic,result$p.value)\n");
            stringBuilder.append("write.csv2(m,file=\"./").append(directoryPath).append("friedman.csv\")\n");
            stringBuilder.append("\n");
            stringBuilder.append("pos_teste<-friedmanmc(AR1)\n");
            stringBuilder.append("write.csv2(pos_teste,file=\"./").append(directoryPath)
                    .append("friedman-compara.csv\")\n");
            stringBuilder.append("png(file=\"./").append(directoryPath)
                    .append("friedman-boxplot.png\", width=1440, height=500)\n");
            stringBuilder.append("boxplot(").append(contextNames.toString());

            contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append("\"").append(context).append("\", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append(", names=c(").append(contextNames.toString()).append("))");

            friedman.write(stringBuilder.toString());
        }

        ProcessBuilder processBuilder = new ProcessBuilder("R", "--no-save");
        Process process = processBuilder.redirectInput(new File("./" + directoryPath + "friedman_script.txt")).start();
        process.waitFor();
    }

    private static void runWilcoxon(String directoryPath, String[] contexts) throws IOException, InterruptedException {
        try (FileWriter wilcoxon = new FileWriter(directoryPath + "wilcoxon_script.txt")) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String context : contexts) {
                stringBuilder.append(context).append("<- c(");
                try (Scanner scanner = new Scanner(
                        new FileInputStream(directoryPath + context + "/HYPERVOLUME_RESULT.txt"))) {
                    while (scanner.hasNextLine()) {
                        String value = scanner.nextLine().trim();
                        if (!value.isEmpty()) {
                            stringBuilder.append(value).append(", ");
                        }
                    }
                } catch (Exception e) {
                    LOGGER.info(e);
                }
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                stringBuilder.append(")\n");
                stringBuilder.append("\n");
            }

            StringBuilder contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append(context).append(", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append("wilcox.test(").append(contextNames.toString()).append(")\n");
            stringBuilder.append("png(file=\"./").append(directoryPath)
                    .append("wilcoxon-boxplot.png\", width=500, height=500)\n");
            stringBuilder.append("boxplot(").append(contextNames.toString());

            contextNames = new StringBuilder();
            for (String context : contexts) {
                contextNames.append("\"").append(context).append("\", ");
            }
            contextNames.delete(contextNames.length() - 2, contextNames.length());

            stringBuilder.append(", names=c(").append(contextNames.toString()).append("))");

            wilcoxon.write(stringBuilder.toString());
        }

        ProcessBuilder processBuilder = new ProcessBuilder("R", "--no-save");
        Process process = processBuilder.redirectInput(new File("./" + directoryPath + "wilcoxon_script.txt")).start();
        process.waitFor();
    }

    public static SolutionSet removeDominadas(SolutionSet result) {
        boolean dominador, dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                dominador = true;
                dominado = true;

                for (int k = 0; k < result.get(i).numberOfObjectives(); k++) {
                    valor1 = result.get(i).getObjective(k);
                    valor2 = result.get(j).getObjective(k);

                    if (valor1 > valor2 || dominador == false) {
                        dominador = false;
                    } else if (valor1 <= valor2) {
                        dominador = true;
                    }

                    if (valor2 > valor1 || dominado == false) {
                        dominado = false;
                    } else if (valor2 < valor1) {
                        dominado = true;
                    }
                }

                if (dominador) {
                    result.remove(j);
                    j -= 1;
                } else if (dominado) {
                    result.remove(i);
                    j = i;
                }
            }
        }

        return result;
    }
}
