package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;

public class BaseMetric {
    private Double results;
    private Architecture architecture;

    public BaseMetric(Architecture architecture) {
        this.architecture = architecture;
    }

    public Double getResults() {
        return results;
    }

    public void setResults(Double results) {
        this.results = results;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }
}
