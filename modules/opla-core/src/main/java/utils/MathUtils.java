/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import learning.Clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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

    public static List<List<Double>> normalize(SolutionSet run) {
        List<Double> mins = new ArrayList<>();
        List<Double> maxs = new ArrayList<>();

        run.getSolutionSet().forEach(r -> {
            selectMinsMax(mins, maxs, r);
        });

        return getNormalizedByMinsMax(run, mins, maxs);
    }

    public static List<List<Double>> normalizeWithAllSolutions(SolutionSet resultFront) {
        List<Double> mins = new ArrayList<>();
        List<Double> maxs = new ArrayList<>();

        resultFront.getAllSolutions().forEach(r -> {
            selectMinsMax(mins, maxs, r);
        });

        return getNormalizedByMinsMax(resultFront, mins, maxs);
    }

    private static List<List<Double>> getNormalizedByMinsMax(SolutionSet resultFront, List<Double> mins, List<Double> maxs) {
        return resultFront.getSolutionSet().stream().map(r -> {
            List<Double> values = new ArrayList<>();
            for (int i = 0; i < r.numberOfObjectives(); i++) {
                values.add(i, (maxs.get(i) - mins.get(i)) == 0 ? 0 : (r.getObjective(i) - mins.get(i)) / (maxs.get(i) - mins.get(i)));
            }
            return values;
        }).collect(Collectors.toList());
    }

    private static void selectMinsMax(List<Double> mins, List<Double> maxs, Solution r) {
        for (int i = 0; i < r.numberOfObjectives(); i++) {
            if (mins.size() < r.numberOfObjectives()) mins.add(i, Double.MAX_VALUE);
            if (maxs.size() < r.numberOfObjectives()) maxs.add(i, Double.MIN_VALUE);
            if (r.getObjective(i) > maxs.get(i)) maxs.set(i, r.getObjective(i));
            if (r.getObjective(i) < mins.get(i)) mins.set(i, r.getObjective(i));
        }
    }

    public static Double normalizeValue(Double minValue, Double maxValue, Double objectiveValue) {
        return (maxValue - minValue) != 0 ? (objectiveValue - minValue) / (maxValue - minValue) : 0;
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
     * @param values
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
