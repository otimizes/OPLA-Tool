package br.otimizes.oplatool.architecture.helpers;

import java.util.ArrayList;
import java.util.List;

public class StatisticalMethodsHelper {
    public static strictfp Double getMedia(ArrayList<Integer> valor) {
        try {
            return getSum(valor) / valor.size();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("The list has null values");
        }
    }

    public static strictfp Double getSum(List<Integer> valor) {
        Double soma = 0D;
        for (Integer integer : valor) {
            soma += integer;
        }
        return soma;
    }

    public static strictfp Double getStandardDeviation(ArrayList<Integer> valor) {
        Double media = getMedia(valor);
        int tam = valor.size();
        double stdDeviation = 0D;
        for (Integer vlr : valor) {
            Double aux = vlr - media;
            stdDeviation += aux * aux;
        }
        return Math.sqrt(stdDeviation / (tam - 1));
    }
}
