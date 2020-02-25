package metrics;

import results.Execution;
import results.Experiment;

public class Av extends Metrics {
	
	 private double av;

	    public Av(String idSolution, Execution execution, Experiment experiement) {
		super.setExecution(execution);
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
