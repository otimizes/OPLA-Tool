/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.core.jmetal4.main.indicators;

import br.otimizes.oplatool.core.jmetal4.qualityIndicator.util.MetricsUtil;
import br.otimizes.oplatool.domain.config.FileConstants;

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
                String directoryPath = "experiment" + FileConstants.FILE_SEPARATOR + pla + FileConstants.FILE_SEPARATOR;

                double[][] front = mu.readFront(directoryPath + FileConstants.FILE_SEPARATOR + context + FileConstants.FILE_SEPARATOR + "FUN_All_" + pla + ".txt");

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
                System.out.println();

            }
        }
    }

}
