package br.uem.din.metrics.conventionalMetrics;

import br.uem.din.architectureEvolution.representation.Architecture;
import br.uem.din.architectureEvolution.representation.Class;
import br.uem.din.architectureEvolution.representation.Component;

//Numbers among classes elegance metric

public class NACElegance {

	private Architecture architecture;
	private double results;
	
		
	public NACElegance(Architecture architecture){
	
	this.architecture = architecture;
	this.results = 0.0;
	double stdDeviationAttributes = 0.0;
	double stdDeviationMethods = 0.0;
	double arrayAttributesNumbers[]= new double[10000];
    double arrayMethodsNumbers[]= new double[10000];
    int i = 0;
    int j = 0;
    
	//Instancia a classe utilitária

    Estatistica e = new Estatistica();
     
    for (Class cls: architecture.getClasses()){
			//seta valores dos arrays
			arrayAttributesNumbers[i]=cls.getAttributes().size();
			i++;
			arrayMethodsNumbers[j]=cls.getMethods().size();
			j++;			
	}
	e.setArray(arrayAttributesNumbers);
	stdDeviationAttributes = e.getDesvioPadrao();
		
	e.setArray(arrayMethodsNumbers);
	stdDeviationMethods = e.getDesvioPadrao();
		
	this.results = (stdDeviationAttributes + stdDeviationMethods ) / 2; 
}
	
	public double getResults() {
		return results;
	}
		
}
