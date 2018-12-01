/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.br.opla.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author elf
 */
public class MathUtils {
//

    public static HashMap<String, List<List<Double>>> normalize(HashMap<String, List<List<Double>>> listObjectivesValues, String[] objectives) {

        for (int i = 0; i < objectives.length; i++) {
            String name = objectives[i];
            List<Double> values = mergeAllObjectiveOfAllRuns(listObjectivesValues, name);

            Double max = findMaxValue(values);
            Double min = findMinValue(values);

            if (objectives[i].equalsIgnoreCase(objectives[i])) {
                //Normaliza valores para conventional
                for (Entry<String, List<List<Double>>> entry : listObjectivesValues.entrySet()) {
                    String function = entry.getKey();
                    if (function.startsWith(objectives[i])) {
                        List<List<Double>> valuesConventional = entry.getValue();
                        for (List<Double> list : valuesConventional) {
                            for (int j = 0; j < list.size(); j++) {
                                list.set(j, normalizeValue(min, max, list.get(j)));
                            }
                        }
                    }
                }
            }


        }
        return listObjectivesValues;
    }

    public static Double normalizeValue(Double minValue, Double maxValue, Double objectiveValue) {
        return (objectiveValue - minValue) / (maxValue - minValue);
    }

    public static Double findMaxValue(List<Double> numbers) {
        Double max = Double.MIN_VALUE;
        for (Double number : numbers) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    public static Double findMinValue(List<Double> numbers) {
        Double min = Double.MAX_VALUE;
        for (Double number : numbers) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }

    public static double mean(List<Double> values) {
        double total = 0;

        for (Double double1 : values) {
            total += double1;
        }

        return total / values.size();
    }

    /**
     * Desvio Padrão.
     *
     * @param objetos
     * @return
     */
    public static double stDev(List<Double> values) {
        double media = mean(values);
        double somatorio = 0d;

        for (Double d : values) {
            somatorio += (media - d.doubleValue()) * (media - d.doubleValue());
        }

        return Math.sqrt(((double) 1 / (values.size()))
                * somatorio);
    }

    private static List<Double> mergeAllObjectiveOfAllRuns(HashMap<String, List<List<Double>>> listObjectivesValues, String name) {
        List<Double> allObjectiveRuns = new ArrayList<>();
        for (Entry<String, List<List<Double>>> entry : listObjectivesValues.entrySet()) {
            if (entry.getKey().startsWith(name)) {
                List<List<Double>> values = entry.getValue();
                for (List<Double> list : values) {
                    for (Double double1 : list) {
                        allObjectiveRuns.add(double1);
                    }
                }
            }
        }


        return allObjectiveRuns;
    }
}
