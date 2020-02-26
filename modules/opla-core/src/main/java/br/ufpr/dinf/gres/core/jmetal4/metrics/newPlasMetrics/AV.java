package br.ufpr.dinf.gres.core.jmetal4.metrics.newPlasMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Package;

public class AV {
	
	private int results;
	private int compcomposto;
	private int compvariable;
	//private int tcompvariable;
	private int cantvcomponet;
	private int variablecomp;
	private int a;
	
	private Architecture architecture;
	
	
	public int cantComponetvariable(Architecture architecture){
		
		this.cantvcomponet= 0;

		for(Package pacote : this.architecture.getAllPackages()){
			
			this.variablecomp = 0;
				
			for(Element elemento : pacote.getElements()){
				
				if(elemento.getVariationPoint() != null){
					this.variablecomp = 1;
				}
			}
			
			if (this.variablecomp == 1){
				cantvcomponet ++;
			}
		}	
		
		return cantvcomponet;
	}
	
 	
	public AV(Architecture architecture){
		
		this.architecture = architecture;
		
		this.compvariable = cantComponetvariable(this.architecture);
		
		for(Package pacote : this.architecture.getAllPackages()){
			
			this.compcomposto = 0;
				
			for(Element elemento : pacote.getElements()){
								
				if(elemento.getTypeElement() == "package"){
					
					this.compcomposto++;
					
				}
			}
			
			if(this.compcomposto != 0){
				this.a = this.compcomposto;	
			}else{
				this.a = this.compvariable;
			}
		}
		
		this.results = compvariable + this.a;
}
	
				
	
	public int getResults() {
		
		return results;
	}

}




/*
 
	public AV(Architecture architecture){
		
		this.architecture = architecture;
		
		for(Package pacote : this.architecture.getAllPackages()){
			
			this.compcomposto = 0;
			this.compvariable = 0;
				
			for(Element elemento : pacote.getElements()){
				
				if(elemento.getVariationPoint() != null){
					this.compvariable = 1;
				}
				
				if(elemento.getTypeElement() == "package"){
					
					this.compcomposto++;
					
				}else{
					
					this.compcomposto = 0;
				}
			}
			
			if(this.compvariable == 1){
				
				this.tcompvariable++;
			}			
			
		}
		
		this.br.ufpr.dinf.gres.core.jmetal4.results = this.tcompvariable + this.tcompvariable;
	}
				
 */
 


