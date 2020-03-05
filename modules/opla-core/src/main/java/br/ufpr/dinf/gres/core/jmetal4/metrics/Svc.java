package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;


public class Svc extends Metrics {

    private double svc;
    private float results;

    public Svc(Architecture architecture) {

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

        float denominador = tcommoncomp + tvariablecomp;

        if (denominador == 0) {
            this.results = 0;
        } else {
            this.results = tvariablecomp / denominador;
        }

    }

    public float getResults() {
        return results;
    }

    public Svc(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getSvc() {
        return svc;
    }

    public void setSvc(double svc) {
        this.svc = svc;
    }

}
