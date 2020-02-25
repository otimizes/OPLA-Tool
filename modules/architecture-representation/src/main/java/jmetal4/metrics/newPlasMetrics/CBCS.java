package jmetal4.metrics.newPlasMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;

public class CBCS {
	
	private float results;
	private Architecture architecture;
	
	public CBCS(Architecture architecture){
		
		this.results = 0;
		this.architecture = architecture;
		float valorcbcs = 0;
		float numinterface =  architecture.getAllInterfaces().size();
		
		for(Interface interf : this.architecture.getAllInterfaces()){
			
			//System.out.println("CBCS for Interface:" + interf.getName() + "=" + interf.getRelationships().size());
			
			//System.out.println("\n");
			
			valorcbcs += interf.getRelationships().size();
			
		}
		
		this.results = valorcbcs / numinterface;
		
}
	
	public float getResults() {
		
		return results;
	}

}


