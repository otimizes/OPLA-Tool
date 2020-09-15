package br.otimizes.oplatool.core.jmetal4.metrics.asp;

import br.otimizes.oplatool.architecture.helpers.ASPHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;

public class EleganceNAC extends ObjectiveFunctionImplementation {

    public EleganceNAC(Architecture architecture) {
        super(architecture);
        // quantidade de atributos + metodos de cada classe da solução
        ArrayList<Integer> lstAtrib = new ArrayList<>();
        ArrayList<Integer> lstMeth = new ArrayList<>();


        // para cada classe contAtribMeth da arquitetura, contar a quantidade de metodos e atributos e adicionar a soma na lista lstAtriMeth
        for (Class contClass : architecture.getAllClasses()) {
            lstAtrib.add(contClass.getAllAttributes().size());
            lstMeth.add(contClass.getAllMethods().size());
        }

        // calculo do desvio padrao
        Double atribClass = ASPHelper.getStandardDeviation(lstAtrib);
        System.out.println(("desvio padrão atributos de uma classe" + atribClass));

        Double methodClas = ASPHelper.getStandardDeviation(lstMeth);
        System.out.println(("desvio padrão metodos de uma classe" + methodClas));


        // media de dois desnvios
        Double THzNAC = (atribClass + methodClas) / 2;

        System.out.println("THZ para NAC: " + THzNAC);
        this.setResults(THzNAC);
    }


}
