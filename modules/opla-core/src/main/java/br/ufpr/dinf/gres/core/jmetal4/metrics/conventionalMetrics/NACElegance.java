package br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;

//Numbers among classes elegance metric

public class NACElegance {

    private Double results;

    public NACElegance(Architecture architecture) {

        this.results = 0.0;
        double stdDeviationAttributes = 0.0;
        double stdDeviationMethods = 0.0;
        double[] arrayAttributesNumbers = new double[10000];
        double[] arrayMethodsNumbers = new double[10000];
        int i = 0;
        int j = 0;


        ConventionalMetricsStatistic e = new ConventionalMetricsStatistic();

        for (Class cls : architecture.getAllClasses()) {
            // seta valores dos arrays
            arrayAttributesNumbers[i] = cls.getAllAttributes().size();
            i++;
            arrayMethodsNumbers[j] = cls.getAllMethods().size();
            j++;
        }
        e.setArray(arrayAttributesNumbers);
        stdDeviationAttributes = e.getSampleStandardDeviation();

        e.setArray(arrayMethodsNumbers);
        stdDeviationMethods = e.getSampleStandardDeviation();

        this.results = (stdDeviationAttributes + stdDeviationMethods) / 2;
    }

    public Double getResults() {
        return results;
    }

}
