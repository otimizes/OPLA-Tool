/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.indicadores;

import jmetal4.core.SolutionSet;
import jmetal4.qualityIndicator.util.MetricsUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author giovaniguizzo
 */
public class Hypervolume {

    public static void clearFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void printFormatedHypervolumeFile(SolutionSet allSolutions, String path, boolean append) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        try (FileWriter fileWriter = new FileWriter(file, append)) {
            for (int i = 0; i < allSolutions.size(); i++) {
                fileWriter.write(allSolutions.get(i).toString());
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
        }
    }

    public static double[] printReferencePoint(double[][] allSolutions, String path, int objectives) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        double[] max = new MetricsUtil().getMaximumValues(allSolutions, objectives);
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (double d : max) {
                fileWriter.write(Double.toString(d + 0.1D) + " ");
            }
        }
        return max;
    }

}
