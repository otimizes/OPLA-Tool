package br.otimizes.oplatool.core.jmetal4.main;

import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctions;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainTestUtil {

    public static void printInformationToFile(SolutionSet solutionsList_, String path) {
        try {
            /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("Number of solutions: " + solutionsList_.size());
            int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
            for (int i = 0; i < solutionsList_.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    bw.newLine();
                    bw.write("--------  NEW SOLUTION ------------------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Solution: " + i);
                    bw.newLine();
                    Architecture arch = (Architecture) solutionsList_.get(i).getDecisionVariables()[j];

                    for (Concern concern : arch.getAllConcerns()) {
                        bw.newLine();
                        bw.write("Concern: " + concern.getName());
                    }

                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of Packages: " + arch.getAllPackages().size());
                    bw.newLine();

                    bw.newLine();
                    bw.write("Number of variabilities: " + arch.getAllVariabilities().size());

                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of interfaces: " + arch.getAllInterfaces().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of classes: " + arch.getAllClasses().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of DependencyInterElementRelationships: "
                            + arch.getRelationshipHolder().getAllDependencies().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of AbstractionInterElementRelationships: "
                            + arch.getRelationshipHolder().getAllAbstractions().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of GeneralizationsRelationships: "
                            + arch.getRelationshipHolder().getAllGeneralizations().size());
                    bw.newLine();
                    bw.newLine();
                    bw.write("Number of AssociationsRelationships: "
                            + arch.getRelationshipHolder().getAllAssociationsRelationships().size());
                    bw.newLine();
                    bw.write("Number of AssociationClasses: "
                            + arch.getRelationshipHolder().getAllAssociationsClass().size());
                    bw.newLine();
                }
            }

            /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printInformationToFile

    public static void printTimeToFile(String path, int run, long[] time, String pla) {
        try {
            /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("--------  " + pla + " ------------------");
            bw.newLine();
            bw.newLine();
            for (int i = 0; i < run; i++) {
                bw.write("" + time[i]);
                bw.newLine();
            }

            /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printTimeToFile

    public static void printDiscardedSolutionsToFile(int[] discardedSolutions, String path) {
        try {
            /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            bw.newLine();
            bw.write("Number of Discarded Solutions");

            int runs = discardedSolutions.length;
            for (int i = 0; i < runs; i++) {
                bw.newLine();
                bw.write("Run " + i + ":  " + discardedSolutions[i]);
                bw.newLine();
            }
            /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printDiscardedSolutionsToFile

    public static void printAllMetricsToFile(SolutionSet solutionsList_, String path) {
        try {
            /* Open the file */
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
            bw.write("MSI    MAC     Extensibility       Elegance");
            bw.newLine();
//            ObjectiveFunctionEvaluation metrics = new ObjectiveFunctionEvaluation();

            for (int i = 0; i < solutionsList_.size(); i++) {
                for (int j = 0; j < numberOfVariables; j++) {
                    Architecture arch = (Architecture) solutionsList_.get(i).getDecisionVariables()[j];
                    bw.write(ObjectiveFunctions.FM.evaluate(arch) + "  " + ObjectiveFunctions.CM.evaluate(arch) + "  "
                            + ObjectiveFunctions.EXT.evaluate(arch) + "  " + ObjectiveFunctions.ELEG.evaluate(arch));
                    // bw.write(solutionsList_.get(i).toString());
                    bw.newLine();
                }
            }

            /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printAllMetricsToFile

    public static void printMetricsToFile(SolutionSet solutionsList_, String path) {
//        ObjectiveFunctionEvaluation metrics = new ObjectiveFunctionEvaluation();
//
//        try {
//            /* Open the file */
//            FileOutputStream fos = new FileOutputStream(path);
//            OutputStreamWriter osw = new OutputStreamWriter(fos);
//            BufferedWriter bw = new BufferedWriter(osw);
//
//            bw.write("Number of solutions: " + solutionsList_.size());
//            int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
//
//            for (int i = 0; i < solutionsList_.size(); i++) {
//                for (int j = 0; j < numberOfVariables; j++) {
//                    bw.newLine();
//                    bw.write("-----------------------------------  NEW SOLUTION -----------------------------------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("Solution: " + i);
//                    bw.newLine();
//                    Architecture arch = (Architecture) solutionsList_.get(i).getDecisionVariables()[j];
//                    bw.newLine();
//                    bw.newLine();
//
//                    bw.write("--------  ELEGANCE ------------------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("ELEGANCE: " + ObjectiveFunctions.ELEG.evaluate(arch));
//                    bw.newLine();
//                    bw.write("NAC - ELEGANCE: " + metrics.evaluateNACElegance(arch));
//                    bw.newLine();
//                    bw.write("ATMR - ELEGANCE: " + metrics.evaluateATMRElegance(arch));
//                    bw.newLine();
//                    bw.write("EC - ELEGANCE: " + metrics.evaluateECElegance(arch));
//                    bw.newLine();
//                    bw.newLine();
//
//                    bw.write("--------  PLA EXTENSIBILITY -----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("PLA EXTENSIBILITY: " + ObjectiveFunctions.EXT.evaluate(arch));
//                    bw.newLine();
//
//                    bw.write("-----  FEATURE-DRIVEN METRICS ----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("MSI AGGREGATION: " + ObjectiveFunctions.FM.evaluate(arch));
//                    bw.newLine();
//                    bw.write("CDAC: " + metrics.evaluateCDAC(arch));
//                    bw.newLine();
//                    bw.write("CDAI: " + metrics.evaluateCDAI(arch));
//                    bw.newLine();
//                    bw.write("CDAO: " + metrics.evaluateCDAO(arch));
//                    bw.newLine();
//                    bw.write("CIBC: " + metrics.evaluateCIBC(arch));
//                    bw.newLine();
//                    bw.write("IIBC: " + metrics.evaluateIIBC(arch));
//                    bw.newLine();
//                    bw.write("OOBC: " + metrics.evaluateOOBC(arch));
//                    bw.newLine();
//                    bw.write("LCC: " + ObjectiveFunctions.LCC.evaluate(arch));
//                    bw.newLine();
//                    bw.write("LCCClass: " + metrics.evaluateLCCClass(arch));
//                    bw.newLine();
//                    bw.write("CDAClass: " + metrics.evaluateCDAClass(arch));
//                    bw.newLine();
//                    bw.write("CIBClass: " + metrics.evaluateCIBClass(arch));
//                    bw.newLine();
//                    bw.newLine();
//
//                    bw.write("------  CONVENTIONAL METRICS -----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("MAC AGGREGATION: " + ObjectiveFunctions.CM.evaluate(arch));
//                    bw.newLine();
//                    bw.write("Cohesion: " + ObjectiveFunctions.COE.evaluate(arch));
//                    bw.newLine();
//                    bw.write("Mean Dep Comps: " + metrics.evaluateMeanDepComps(arch));
//                    bw.newLine();
//                    bw.write("Mean Num Ops: " + metrics.evaluateMeanNumOps(arch));
//                    bw.newLine();
//                    bw.write("Sum Classes DepIn: " + metrics.evaluateSumClassesDepIn(arch));
//                    bw.newLine();
//                    bw.write("Sum Classes DepOut: " + metrics.evaluateSumClassesDepOut(arch));
//                    bw.newLine();
//                    bw.write("Sum DepIn: " + metrics.evaluateSumDepIn(arch));
//                    bw.newLine();
//                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
//                    bw.newLine();
//                    bw.newLine();
//
//                    bw.write("------  ACOMP METRICS -----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("Sum DepIn: "
//                            + metrics.evaluateSumDepIn(arch));
//                    bw.newLine();
//                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
//                    bw.newLine();
//                    bw.newLine();
//
//
//                    bw.write("------  ACLASS METRICS -----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("Sum DepIn: "
//                            + metrics.evaluateSumDepIn(arch));
//                    bw.newLine();
//                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
//                    bw.newLine();
//                    bw.newLine();
//
//
//                    bw.write("------  TAM METRICS -----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("Mean Num OPs: " + metrics.evaluateMeanNumOps(arch));
//                    bw.newLine();
//                    bw.newLine();
//
//
//                    bw.write("------  COE METRICS -----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("Relational Cohesion: " + ObjectiveFunctions.COE.evaluate(arch));
//                    bw.newLine();
//                    bw.write("Lcc: " + metrics.evaluateLCC(arch));
//                    bw.newLine();
//                    bw.newLine();
//
//
//                    bw.write("------  DC METRICS -----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("CDAC: " + metrics.evaluateCDAC(arch));
//                    bw.newLine();
//                    bw.write("CDAI: " + metrics.evaluateCDAI(arch));
//                    bw.newLine();
//                    bw.write("CDAO: " + metrics.evaluateCDAO(arch));
//                    bw.newLine();
//
//                    bw.newLine();
//
//                    bw.write("------  EC METRICS -----------");
//                    bw.newLine();
//                    bw.newLine();
//                    bw.write("CIBC: " + metrics.evaluateCIBC(arch));
//                    bw.newLine();
//                    bw.write("IIBC: " + metrics.evaluateIIBC(arch));
//                    bw.newLine();
//                    bw.write("OOBC: " + metrics.evaluateOOBC(arch));
//                    bw.newLine();
//
//                    bw.newLine();
//                }
//            }
//
//            /* Close the file */
//            bw.close();
//        } catch (IOException e) {
//            Configuration.logger_.severe("Error acceding to the file");
//            e.printStackTrace();
//        }
    } // printMetricsToFile
}
