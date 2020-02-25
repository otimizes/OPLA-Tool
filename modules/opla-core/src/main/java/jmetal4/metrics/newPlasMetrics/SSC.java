package jmetal4.metrics.newPlasMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Package;

public class SSC {
	
	private float results;
	private float tcommoncomp;
	private float tvariablecomp;
	private int commoncomp;
	private int variablecomp;
	private Architecture architecture;
	
	public SSC(Architecture architecture){

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
			
			if(this.variablecomp == 1){
				
				this.tvariablecomp++;
			
			}else{
				
				this.tcommoncomp++;
			}
			
		}
		
		if (tcommoncomp == 0) {
			this.results = 0;
		}else{

		this.results = 1 / ( this.tcommoncomp /(this.tvariablecomp + this.tcommoncomp));
		}
		
	}
	
	public float getResults() {
		
		return results;
	}

}


