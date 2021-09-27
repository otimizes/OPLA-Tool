package br.otimizes.oplatool.core.jmetal4.metrics.asp;

import br.otimizes.oplatool.architecture.helpers.StatisticalMethodsHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;

public class EleganceEX extends ObjectiveFunctionImplementation {

    public EleganceEX(Architecture architecture) {
        super(architecture);
        ArrayList<Integer> externalcouples = new ArrayList<>();
        ArrayList<Integer> listAux;
        int countExternal;
        for (Class clazz : architecture.getAllClasses()) {
            listAux = architecture.getLinkOverload(clazz);
            countExternal = (listAux.get(0) + listAux.get(1) + listAux.get(2));
            externalcouples.add(countExternal);
        }
        for (Interface interface_ : architecture.getAllInterfaces()) {
            listAux = architecture.getLinkOverload(interface_);
            countExternal = (listAux.get(0) + listAux.get(1) + listAux.get(2));
            externalcouples.add(countExternal);
        }
        Double thzEX = StatisticalMethodsHelper.getStandardDeviation(externalcouples);
        System.out.println(("THZ of ExternalCouples" + thzEX));
        this.setResults(thzEX);
    }


}
