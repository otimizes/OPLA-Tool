/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.indicadores;

import jmetal4.qualityIndicator.util.MetricsUtil;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * @author giovaniguizzo
 */
public class FormatForMe {

    public static void main(String[] args) throws IOException {
        String[] plas = {
                "agm", //            "MicrowaveOvenSoftware",
                //            "ServiceAndSupportSystem"
        };

        String[] contexts = {
                "PLAMutation",
                "PLAMutationWithPatterns",
                "OnlyPatternsMutation", //            "Original"
        };

        MetricsUtil mu = new MetricsUtil();

        DecimalFormat df = new DecimalFormat("0.000");
        DecimalFormat df2 = new DecimalFormat("0");

        for (String pla : plas) {

            for (String context : contexts) {

                /**
                 * PF_{known}
                 *
                 */
                String directoryPath = "experiment/" + pla + "/";

                double[][] front = mu.readFront(directoryPath + "/" + context + "/FUN_All_" + pla + ".txt");

                System.out.println(pla + " " + context + ":");

                int count = 0;
                for (double[] fitness : front) {
                    count++;
                    System.out.print("(" + df2.format(fitness[0]) + "," + df.format(fitness[1]) + ")");
                    if (count == 2) {
                        System.out.print(" ");
                        count = 0;
                    }
                }
                System.out.println("");

            }
        }
    }

}
