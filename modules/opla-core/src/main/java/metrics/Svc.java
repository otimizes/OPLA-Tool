package metrics;

import results.Execution;
import results.Experiment;


public class Svc extends Metrics {
	
	 private double svc;

	    public Svc(String idSolution, Execution execution, Experiment experiement) {
		super.setExecution(execution);
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
