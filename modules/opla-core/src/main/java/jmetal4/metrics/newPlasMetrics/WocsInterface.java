package jmetal4.metrics.newPlasMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;

public class WocsInterface {
	
	private float results;
	
	private Architecture architecture;
	
	public WocsInterface(Architecture architecture) {
		
		this.results = 0;
		this.architecture = architecture;
		float valorwocsi = 0;
		float tcomplexidade = 0;
		float numclass =  architecture.getAllInterfaces().size();
		
		for(Package pacote : this.architecture.getAllPackages()){
			
			for(Interface interfa : pacote.getAllInterfaces()){
				int cantparame = 0;
				int complexidade = 0;
				
				for(Method metodo : interfa.getOperations()){
					
					cantparame = metodo.getParameters().size() + 1;
					complexidade += cantparame;
				}
				
				//System.out.println("WOCS for Interface:" + interfa.getName() + "=" + complexidade);
				
				//this.results = complexidade;
				tcomplexidade = complexidade;
				
			}
			
		}
		valorwocsi = tcomplexidade / numclass;
		this.results = valorwocsi;

	}

	public float getResults() {
		
		return results;
	}

}
