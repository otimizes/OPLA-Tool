package br.uem.din.metrics.conventionalMetrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.uem.din.architectureEvolution.representation.Architecture;
import br.uem.din.architectureEvolution.representation.AssociationEnd;
import br.uem.din.architectureEvolution.representation.AssociationInterClassRelationship;
import br.uem.din.architectureEvolution.representation.Class;
import br.uem.din.architectureEvolution.representation.Component;
import br.uem.din.architectureEvolution.representation.DependencyInterClassRelationship;
import br.uem.din.architectureEvolution.representation.InterClassRelationship;

public class ECElegance {

	private Architecture architecture;
	private double results;
	
	//External Coupling Elegance metric	
	public ECElegance(Architecture architecture){
	
	this.architecture = architecture;
	this.results = 0.0;
	double stdDeviationCouples = 0.0;
	double externalCouplesNumbers[]= new double[10000];
    int i = 0;
    
	//Instancia a classe utilitária

    Estatistica e = new Estatistica();
     
    for (Class cls: architecture.getClasses()){
		//busca os external couples de cada classe   	
    	externalCouplesNumbers[i]=searchClassDependencies(cls);
		i++;
    }
			
	e.setArray(externalCouplesNumbers);
	stdDeviationCouples = e.getDesvioPadrao();
		
	this.results = stdDeviationCouples;
	
		 
}
	//----------------------------------------------------------------------------------

		private int searchClassDependencies (Class source){
			//List<Class> depClasses = new ArrayList<Class> ();
			int cont=0;	
			List<InterClassRelationship> relationships = new ArrayList<InterClassRelationship> (source.getRelationshipsHolder().getInterClassRelationships());
			if (relationships!=null){
				for (InterClassRelationship relationship : relationships) {
					if (relationship instanceof DependencyInterClassRelationship){
						DependencyInterClassRelationship dependency = (DependencyInterClassRelationship) relationship;
						if (dependency.getClient().equals(source)) cont++;
					}
					else{
						if (relationship instanceof AssociationInterClassRelationship){
							AssociationInterClassRelationship association = (AssociationInterClassRelationship) relationship;
							for (AssociationEnd associationEnd : association.getParticipants()) {
            					if (associationEnd.getCLSClass().equals(source)) cont++; 
							}							
						}
					}
				}
			}	
			return cont;
		}
		
	public double getResults() {
		return results;
	}
	
}
