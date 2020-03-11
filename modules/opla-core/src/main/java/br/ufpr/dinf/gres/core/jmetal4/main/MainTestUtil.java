package br.ufpr.dinf.gres.core.jmetal4.main;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.common.Configuration;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.metrics.MetricsEvaluation;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainTestUtil {
    public static void printMetricsToFile(SolutionSet solutionsList_, String path) {
        MetricsEvaluation metrics = new MetricsEvaluation();

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
                    bw.write("-----------------------------------  NEW SOLUTION -----------------------------------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Solution: " + i);
                    bw.newLine();
                    Architecture arch = (Architecture) solutionsList_.get(i).getDecisionVariables()[j];
                    bw.newLine();
                    bw.newLine();

                    bw.write("--------  ELEGANCE ------------------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("ELEGANCE: " + metrics.evaluateElegance(arch));
                    bw.newLine();
                    bw.write("NAC - ELEGANCE: " + metrics.evaluateNACElegance(arch));
                    bw.newLine();
                    bw.write("ATMR - ELEGANCE: " + metrics.evaluateATMRElegance(arch));
                    bw.newLine();
                    bw.write("EC - ELEGANCE: " + metrics.evaluateECElegance(arch));
                    bw.newLine();
                    bw.newLine();

                    bw.write("--------  PLA EXTENSIBILITY -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("PLA EXTENSIBILITY: " + metrics.evaluatePLAExtensibility(arch));
                    bw.newLine();

                    bw.write("-----  FEATURE-DRIVEN METRICS ----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("MSI AGGREGATION: " + metrics.evaluateMSIFitness(arch));
                    bw.newLine();
                    bw.write("CDAC: " + metrics.evaluateCDAC(arch));
                    bw.newLine();
                    bw.write("CDAI: " + metrics.evaluateCDAI(arch));
                    bw.newLine();
                    bw.write("CDAO: " + metrics.evaluateCDAO(arch));
                    bw.newLine();
                    bw.write("CIBC: " + metrics.evaluateCIBC(arch));
                    bw.newLine();
                    bw.write("IIBC: " + metrics.evaluateIIBC(arch));
                    bw.newLine();
                    bw.write("OOBC: " + metrics.evaluateOOBC(arch));
                    bw.newLine();
                    bw.write("LCC: " + metrics.evaluateLCC(arch));
                    bw.newLine();
                    bw.write("LCCClass: " + metrics.evaluateLCCClass(arch));
                    bw.newLine();
                    bw.write("CDAClass: " + metrics.evaluateCDAClass(arch));
                    bw.newLine();
                    bw.write("CIBClass: " + metrics.evaluateCIBClass(arch));
                    bw.newLine();
                    bw.newLine();

                    bw.write("------  CONVENTIONAL METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("MAC AGGREGATION: " + metrics.evaluateMACFitness(arch));
                    bw.newLine();
                    bw.write("Cohesion: " + metrics.evaluateCohesionFitness(arch));
                    bw.newLine();
                    bw.write("Mean Dep Comps: " + metrics.evaluateMeanDepComps(arch));
                    bw.newLine();
                    bw.write("Mean Num Ops: " + metrics.evaluateMeanNumOps(arch));
                    bw.newLine();
                    bw.write("Sum Classes DepIn: " + metrics.evaluateSumClassesDepIn(arch));
                    bw.newLine();
                    bw.write("Sum Classes DepOut: " + metrics.evaluateSumClassesDepOut(arch));
                    bw.newLine();
                    bw.write("Sum DepIn: " + metrics.evaluateSumDepIn(arch));
                    bw.newLine();
                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
                    bw.newLine();
                    bw.newLine();

                    bw.write("------  ACOMP METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Sum DepIn: "
                            + metrics.evaluateSumDepIn(arch));
                    bw.newLine();
                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
                    bw.newLine();
                    bw.newLine();


                    bw.write("------  ACLASS METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Sum DepIn: "
                            + metrics.evaluateSumDepIn(arch));
                    bw.newLine();
                    bw.write("Sum DepOut: " + metrics.evaluateSumDepOut(arch));
                    bw.newLine();
                    bw.newLine();


                    bw.write("------  TAM METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Mean Num OPs: " + metrics.evaluateMeanNumOps(arch));
                    bw.newLine();
                    bw.newLine();


                    bw.write("------  COE METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("Relational Cohesion: " + metrics.evaluateCohesion(arch));
                    bw.newLine();
                    bw.write("Lcc: " + metrics.evaluateLCC(arch));
                    bw.newLine();
                    bw.newLine();


                    bw.write("------  DC METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("CDAC: " + metrics.evaluateCDAC(arch));
                    bw.newLine();
                    bw.write("CDAI: " + metrics.evaluateCDAI(arch));
                    bw.newLine();
                    bw.write("CDAO: " + metrics.evaluateCDAO(arch));
                    bw.newLine();

                    bw.newLine();

                    bw.write("------  EC METRICS -----------");
                    bw.newLine();
                    bw.newLine();
                    bw.write("CIBC: " + metrics.evaluateCIBC(arch));
                    bw.newLine();
                    bw.write("IIBC: " + metrics.evaluateIIBC(arch));
                    bw.newLine();
                    bw.write("OOBC: " + metrics.evaluateOOBC(arch));
                    bw.newLine();

                    bw.newLine();
                }
            }

            /* Close the file */
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    } // printMetricsToFile
}
