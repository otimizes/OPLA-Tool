package metrics;

import results.Execution;
import results.Experiment;

public class Cbcs extends Metrics {
		
	private double cbcs;

	public Cbcs(String idSolution, Execution execution, Experiment experiement) {
		super.setExecution(execution);
		super.setExperiement(experiement);
		super.setIdSolution(idSolution);
	}

	public double getCbcs() {
		return cbcs;
	}

	public void setCbcs(double cbcs) {
		this.cbcs = cbcs;
	}


}

