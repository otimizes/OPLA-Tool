package br.ufpr.dinf.gres.core.jmetal4.metrics.newPlasMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Method;
import br.ufpr.dinf.gres.architecture.representation.Package;

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
