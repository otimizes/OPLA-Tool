package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Conventional Statistic Metrics
 */
public class ConventionalStatisticMetrics {

    private double[] array;

    public double getPearson() {
        return (getSampleStandardDeviation() / getArithmeticAverage()) * 100;
    }

    public double getArithmeticAverage() {
        double total = 0;
        for (double v : array) total += v;
        return total / array.length;

    }

    public double getSumOfElements() {
        double total = 0;
        for (double v : array) total += v;
        return total;
    }

    public double getSumOfElementsSquare() {
        double total = 0;
        for (double v : array) total += Math.pow(v, 2);
        return total;
    }

    public double getArithmeticAverage(double[] array) {
        double total = 0;
        for (double v : array) total += v;
        return total / array.length;
    }

    public double getSumOfElements(double[] array) {
        double total = 0;
        for (double v : array) total += v;
        return total;
    }

    public int find(int value) {
        return Arrays.binarySearch(array, value);
    }

    public double getSampleVariance() {
        double p1 = 1 / (double) (array.length - 1);
        double p2 = getSumOfElementsSquare()
                - (Math.pow(getSumOfElements(), 2) / (double) array.length);
        return p1 * p2;
    }

    public double getSampleStandardDeviation() {
        return Math.sqrt(getSampleVariance());
    }

    public double getMedian() {
        Arrays.sort(array);
        int tipo = array.length % 2;
        if (tipo == 1) {
            return array[((array.length + 1) / 2) - 1];
        } else {
            int m = array.length / 2;
            return (array[m - 1] + array[m]) / 2;
        }
    }

    public double getModa() {
        HashMap<Double, Integer> map = new HashMap<>();
        Integer i;
        double moda = 0.0;
        Integer numAtual, numMaior = 0;

        for (double v : array) {
            i = map.get(v);
            if (i == null) {
                map.put(v, 1);
            } else {
                map.put(v, i + 1);
                numAtual = i + 1;
                if (numAtual > numMaior) {
                    numMaior = numAtual;
                    moda = v;
                }
            }
        }
        return moda;

    }

    public double getCoefAssimetry() {
        return (getArithmeticAverage() - getModa()) / getSampleStandardDeviation();
    }

    public double[] getArray() {
        return array;
    }

    public void setArray(double[] array) {
        this.array = array;
    }
}
