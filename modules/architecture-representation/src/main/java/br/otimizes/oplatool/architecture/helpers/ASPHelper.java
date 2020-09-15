package br.otimizes.oplatool.architecture.helpers;

import java.util.ArrayList;
import java.util.List;

public class ASPHelper {
    public static strictfp Double getMedia(ArrayList<Integer> valor) { // calculo da media de uma lista
        try {
            return getSum(valor) / valor.size();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("The list has null values");
        }
    }

    public static strictfp Double getSum(List<Integer> valor) { //calculo da soma
        Double soma = 0D;
        for (int i = 0; i < valor.size(); i++) {
            soma += valor.get(i);
        }
        return soma;
    }

    public static strictfp Double getStandardDeviation(ArrayList<Integer> valor) { //desvio padrao
        Double media = getMedia(valor);
        int tam = valor.size();
        Double desvPadrao = 0D;
        for (Integer vlr : valor) {
            Double aux = vlr - media;
            desvPadrao += aux * aux;
        }
        return Math.sqrt(desvPadrao / (tam - 1));
    }
}
