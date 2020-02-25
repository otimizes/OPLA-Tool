package jmetal4.experiments.indicators;

import jmetal4.core.SolutionSet;
import jmetal4.problems.OPLA;
import jmetal4.qualityIndicator.QualityIndicator;
import jmetal4.qualityIndicator.util.MetricsUtil;

import java.io.FileWriter;

public class ExecutaIndicadores2Objetives {

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    public static void main(String[] args) throws Exception {
        String[] abordagens = {"OPLA"};
        String[] algoritmos = {"Bestof2", "Genetico"};
        String[] plas = {"bank"};
        String caminho;
        caminho = "/home/note/000-analise-exp-parte-2/coe-aclass/";

        for (String software : plas) {
            System.out.println("\n ---" + software + "---\n");
            for (String algorithm : algoritmos) {
                // for (String contexto : contextos) {
                System.out.println("\n ---" + algorithm);
                FileWriter os = null;
                double value = 0;
                MetricsUtil mu = new MetricsUtil();

                OPLA problem = new OPLA();
                problem.setnumberOfObjectives(2);

                System.out.println(caminho + software + "/teste/" + software + "_AllSolutions.txt");
                System.out.println(caminho + software + "/teste/" + software + "_trueParetoFront.txt");

                // le o conjunto com todas as solucoes encontradas por
                // todos os algoritmos
                SolutionSet ss = mu
                        .readNonDominatedSolutionSet(caminho + software + "/teste/" + software + "_AllSolutions.txt");

                // remove as solucoes dominadas e repeditas formando o
                // conjunto de pareto rela

                ss = removeDominadas(ss);
//				JOptionPane.showMessageDialog(null, "true");

                // escreve o conjunto de pareto real em um arquivo
                ss.printObjectivesToFile(caminho + software + "/teste/" + software + "_trueParetoFront.txt");

                // le o arquivo com todas as melhores solucoes geradas
                // nas 30 runs por um algoritmo

                double[][] melhoresSolucoesAlgoritmo = mu
                        .readFront(caminho + software + "/teste/" + algorithm + "/" + algorithm + "All.txt");
                // retorna a solucao minima de cada objetivo do conjunto // de
                // pareto real
                double[] min = mu.getMinimumValues(ss.writeObjectivesToMatrix(), 2);

                // comparar o minimo em relacao as solucoes de cada //
                // algoritmo para cada problema
                os = new FileWriter(caminho + software + "/teste/" + algorithm + "/ED.txt", true);
                os.write("--- " + min[0] + " " + min[1] + " ---" + "\n");

                for (int i = 0; i < melhoresSolucoesAlgoritmo.length; i++) {
                    double distanciaEuclidiana = mu.distance(min, melhoresSolucoesAlgoritmo[i]);

                    os.write("" + distanciaEuclidiana + "\n");
                }
                os.close();

                // calcula GD e IGD do conjuto de pareto real em relacao
                // a cada run do algoritmo

                for (int run = 1; run < 31; run++) {
                    try {

                        QualityIndicator indicators = new QualityIndicator(problem,
                                caminho + software + "/teste/" + algorithm
                                        + "/" + algorithm + "/" + run + ".txt");

                        SolutionSet front = mu
                                .readNonDominatedSolutionSet(caminho + software + "/teste/" + algorithm
                                        + "/" + algorithm + "/" + run + ".txt");

                        value = indicators.getIGD(front, ss, 2);

                        os = new FileWriter(caminho + software + "/teste/"
                                + algorithm + "/IGD.txt", true);
                        os.write("" + value + "\n");
                        os.close();

                        value = indicators.getGD(front, ss, 2);

                        os = new FileWriter(caminho + software + "/teste/"
                                + algorithm + "/GD.txt", true);
                        os.write("" + value + "\n");
                        os.close();
                    } catch (Exception e) {
                        System.out.println("erro na " + run);
                        e.printStackTrace();
                    }
                }

            }

        }
    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    public static SolutionSet removeDominadas(SolutionSet result) {
        int numberObjetives = 2;
        boolean dominador, dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                dominador = true;
                dominado = true;

                for (int k = 0; k < numberObjetives; k++) {
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
                    System.out.println("--------------------------------------------");
                    System.out.println("Solucao [" + i + "] domina a Solucao [" + j + "]");
                    System.out.println("[" + i + "] " + result.get(i).toString());
                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(j);
                    j = j - 1;
                } else if (dominado) {
                    System.out.println("--------------------------------------------");
                    System.out.println("Solucao [" + j + "] domina a Solucao [" + i + "]");
                    System.out.println("[" + i + "] " + result.get(i).toString());
                    System.out.println("[" + j + "] " + result.get(j).toString());

                    result.remove(i);
                    j = i;
                }
            }
        }

        return result;
    }
    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

//	// -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
//	public static SolutionSet removeDominadas(SolutionSet result) {
//		int numberObjetives = 2;
//		boolean dominador, dominado;
//		double valor1 = 0;
//		double valor2 = 0;
//
//		for (int i = 0; i < (result.size() - 1); i++) {
//			for (int j = (i + 1); j < result.size(); j++) {
//				dominador = true;
//				dominado = true;
//
//				for (int k = 0; k < numberObjetives; k++) {
//					valor1 = numberObjetives;
//					valor2 = numberObjetives;
//
//					if (valor1 > valor2 || dominador == false) {
//						dominador = false;
//					} else if (valor1 <= valor2) {
//						dominador = true;
//					}
//
//					if (valor2 > valor1 || dominado == false) {
//						dominado = false;
//					} else if (valor2 < valor1) {
//						dominado = true;
//					}
//				}
//
//				if (dominador) {
//					System.out.println("--------------------------------------------");
//					System.out.println("Solucao [" + i + "] domina a Solucao [" + j + "]");
//					System.out.println("[" + i + "] " + result.get(i).toString());
//					System.out.println("[" + j + "] " + result.get(j).toString());
//
//					result.remove(j);
//					j = j - 1;
//				} else if (dominado) {
//					System.out.println("--------------------------------------------");
//					System.out.println("Solucao [" + j + "] domina a Solucao [" + i + "]");
//					System.out.println("[" + i + "] " + result.get(i).toString());
//					System.out.println("[" + j + "] " + result.get(j).toString());
//
//					result.remove(i);
//					j = i;
//				}
//			}
//		}
//
//		return result;
//	}
//	// -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

}
