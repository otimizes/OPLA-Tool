package br.otimizes.oplatool.core.jmetal4.metrics.asp;

import br.otimizes.oplatool.architecture.helpers.StatisticalMethodsHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;

public class EleganceNAC extends ObjectiveFunctionImplementation {

    public EleganceNAC(Architecture architecture) {
        super(architecture);
        ArrayList<Integer> lstAtrib = new ArrayList<>();
        ArrayList<Integer> lstMeth = new ArrayList<>();
        for (Class contClass : architecture.getAllClasses()) {
            lstAtrib.add(contClass.getAllAttributes().size());
            lstMeth.add(contClass.getAllMethods().size());
        }
        Double atribClass = StatisticalMethodsHelper.getStandardDeviation(lstAtrib);
        Double methodClas = StatisticalMethodsHelper.getStandardDeviation(lstMeth);
        Double THzNAC = (atribClass + methodClas) / 2;
        System.out.println("THZ for NAC: " + THzNAC);
        this.setResults(THzNAC);
    }


}
