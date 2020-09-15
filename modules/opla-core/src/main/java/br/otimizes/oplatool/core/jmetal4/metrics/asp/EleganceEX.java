package br.otimizes.oplatool.core.jmetal4.metrics.asp;

import br.otimizes.oplatool.architecture.helpers.ASPHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;

public class EleganceEX extends ObjectiveFunctionImplementation {

    public EleganceEX(Architecture architecture) {
        super(architecture);
        ArrayList<Integer> externalcouples = new ArrayList<>();
        ArrayList<Integer> listAux = new ArrayList<>();
        int countExternal;

        for (Class clazz : architecture.getAllClasses()) { // para cada lasse existente

            listAux = architecture.getLinkOverload(clazz); //calculando o link overload da classe

            countExternal = (listAux.get(0) + listAux.get(1) + listAux.get(2)); //somando entrada + saida + ambos
            externalcouples.add(countExternal);
        }

        for (Interface interface_ : architecture.getAllInterfaces()) {

            listAux = architecture.getLinkOverload(interface_); //calculando o link overload da interface

            countExternal = (listAux.get(0) + listAux.get(1) + listAux.get(2)); //somando entrada + saida + ambos
            externalcouples.add(countExternal);

        }
        // calculo do desvio padrao
        Double thzEX = ASPHelper.getStandardDeviation(externalcouples);
        System.out.println(("THZ de ExternalCouples" + thzEX));

        this.setResults(thzEX);
    }


}
