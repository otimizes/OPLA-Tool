package jmetal4.metrics.newPlasMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Package;

public class SVC {
	
	private float results;
	private float tcommoncomp;
	private float tvariablecomp;
	private int commoncomp;
	private int variablecomp;
	private float denominador;
	private Architecture architecture;
	
	
	public SVC(Architecture architecture){

		this.tcommoncomp = 0;
		this.tvariablecomp = 0;
		this.architecture = architecture;
		
		for(Package pacote : this.architecture.getAllPackages()){
			
			this.commoncomp = 0;
			this.variablecomp = 0;
				
			for(Element elemento : pacote.getElements()){
				
				if(elemento.getVariationPoint() != null){
					this.variablecomp = 1;
				}else{
					this.commoncomp = 1;
				}
			}
			
			if(variablecomp == 1){
				
				this.tvariablecomp++;
			
			}else{
				
				this.tcommoncomp++;
			}
			
		}
		
		this.denominador = this.tcommoncomp + this.tvariablecomp;
		
		if (this.denominador == 0) {
			this.results = 0;
		}else{
		
		 this.results = this.tvariablecomp / this.denominador;
		 
		 //System.out.println("tvariablecomp ="+ this.tvariablecomp);
		 
		 //System.out.println("this.tcommoncomp + this.tvariablecomp ="+ this.tcommoncomp + this.tvariablecomp);
		 
		 //System.out.println("result ="+ this.results);
		
		}
	
	}	
	
	public float getResults() {
		
		return results;
	}

}


