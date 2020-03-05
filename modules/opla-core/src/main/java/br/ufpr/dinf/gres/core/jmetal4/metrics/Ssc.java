package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;


public class Ssc extends Metrics {

    private double ssc;
    private float results;

    public Ssc(Architecture architecture) {

        float tcommoncomp = 0;
        float tvariablecomp = 0;

        for (Package pacote : architecture.getAllPackages()) {

            int variablecomp = 0;

            for (Element elemento : pacote.getElements()) {

                if (elemento.getVariationPoint() != null) {
                    variablecomp = 1;
                }
            }

            if (variablecomp == 1) {
                tvariablecomp++;
            } else {
                tcommoncomp++;
            }
        }

        if (tcommoncomp == 0) {
            this.results = 0;
        } else {

            this.results = 1 / (tcommoncomp / (tvariablecomp + tcommoncomp));
        }

    }

    public float getResults() {
        return results;
    }

    public Ssc(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getSsc() {
        return ssc;
    }

    public void setSsc(double ssc) {
        this.ssc = ssc;
    }

}
