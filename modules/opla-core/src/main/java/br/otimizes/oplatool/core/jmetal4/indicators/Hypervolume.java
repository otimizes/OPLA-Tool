/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.core.jmetal4.indicators;

import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.util.MetricsUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Hypervolume file methods
 */
public class Hypervolume {

    /**
     * Delete the hypervolume file
     *
     * @param path path of the file
     */
    public static void clearFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Print all solutions in a formatted file
     *
     * @param allSolutions solutions
     * @param path         file path
     * @param append       append or replace the file
     * @throws IOException default exception
     */
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

    /**
     * Print the reference point of the solutions objectives in a file
     *
     * @param allSolutions solutions
     * @param path         path to save
     * @param objectives   number of objectives
     * @return reference point
     * @throws IOException default exception
     */
    public static double[] printReferencePoint(double[][] allSolutions, String path, int objectives) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        double[] max = new MetricsUtil().getMaximumValues(allSolutions, objectives);
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (double d : max) {
                fileWriter.write((d + 0.1D) + " ");
            }
        }
        return max;
    }
}
