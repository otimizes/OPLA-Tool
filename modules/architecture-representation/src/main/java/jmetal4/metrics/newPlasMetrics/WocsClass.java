package jmetal4.metrics.newPlasMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Method;
import arquitetura.representation.Package;

public class WocsClass {
	
	private float results;
	
	private Architecture architecture;
	
	public WocsClass(Architecture architecture) {
		
		this.results = 0;
		this.architecture = architecture;
		float valorwocsc = 0;
		float tcomplexidade = 0;
		float numclass =  architecture.getAllClasses().size();
		
		
		for(Package pacote : this.architecture.getAllPackages()){
						
			for(Class classes : pacote.getAllClasses()){
				int cantparame = 0;
				int complexidade = 0;
				
				
				for(Method metodo : classes.getAllMethods()){
					
					cantparame = metodo.getParameters().size() + 1;
					complexidade += cantparame;
				}
				
				tcomplexidade = complexidade;
				
			}
			
		}
		
		
		valorwocsc = tcomplexidade / numclass;
		this.results = valorwocsc;

	}
	
	public float getResults() {
		return results;
	}


}
