package br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;

public class ATMRElegance {

    private Architecture architecture;
    private Double results;

    public ATMRElegance(Architecture architecture) {

        this.architecture = architecture;
        this.results = 0.0;
        double stdDeviationRatios = 0.0;
        double ratiosAttributesMethods[] = new double[10000];
        int i = 0;

        ConventionalMetricsStatistic e = new ConventionalMetricsStatistic();

        for (Class cls : this.architecture.getAllClasses()) {
            // seta valores dos arrays
            if (cls.getAllMethods().size() > 0) {
                ratiosAttributesMethods[i] = (double) cls.getAllAttributes().size() / cls.getAllMethods().size();
                i++;
            } else {
                ratiosAttributesMethods[i] = 0.0;
                i++;
            }

        }
        e.setArray(ratiosAttributesMethods);
        stdDeviationRatios = e.getSampleStandardDeviation();

        this.results = stdDeviationRatios;
    }

    public Double getResults() {
        return results;
    }

}
