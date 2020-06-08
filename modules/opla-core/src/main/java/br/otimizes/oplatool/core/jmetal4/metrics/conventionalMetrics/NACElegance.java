package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

public class NACElegance extends ObjectiveFunctionImplementation {

    public NACElegance(Architecture architecture) {
        super(architecture);
        this.setResults(0.0);
        double stdDeviationAttributes = 0.0;
        double stdDeviationMethods = 0.0;
        double[] arrayAttributesNumbers = new double[10000];
        double[] arrayMethodsNumbers = new double[10000];
        int i = 0;
        int j = 0;


        ConventionalStatisticMetrics e = new ConventionalStatisticMetrics();

        for (Class cls : architecture.getAllClasses()) {
            arrayAttributesNumbers[i] = cls.getAllAttributes().size();
            i++;
            arrayMethodsNumbers[j] = cls.getAllMethods().size();
            j++;
        }
        e.setArray(arrayAttributesNumbers);
        stdDeviationAttributes = e.getSampleStandardDeviation();

        e.setArray(arrayMethodsNumbers);
        stdDeviationMethods = e.getSampleStandardDeviation();

        this.setResults((stdDeviationAttributes + stdDeviationMethods) / 2);
    }
}
