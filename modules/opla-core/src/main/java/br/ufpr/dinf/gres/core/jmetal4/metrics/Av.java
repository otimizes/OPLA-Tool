package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;

public class Av extends Metrics {
	
	 private double av;

	    public Av(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
		super.setExecutionResults(executionResults);
		super.setExperiement(experiement);
		super.setIdSolution(idSolution);
	    }

	    public double getAv() {
		return av;
	    }

	    public void setAv(double av) {
		this.av = av;
	    }


}
