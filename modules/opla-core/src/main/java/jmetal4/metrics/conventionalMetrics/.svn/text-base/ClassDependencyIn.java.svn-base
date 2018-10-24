package br.uem.din.metrics.conventionalMetrics;

import java.util.ArrayList;
import java.util.List;

import br.uem.din.architectureEvolution.representation.Architecture;
import br.uem.din.architectureEvolution.representation.Class;
import br.uem.din.architectureEvolution.representation.Component;
import br.uem.din.architectureEvolution.representation.DependencyInterClassRelationship;
import br.uem.din.architectureEvolution.representation.InterClassRelationship;

public class ClassDependencyIn {

	/**
	 * @param args
	 */
	private Architecture architecture;
	private int results;
	
	public ClassDependencyIn(Architecture architecture) {
	
	this.architecture = architecture;
	this.results = 0;
	int depIn =0;
	
	
	for (Component component : architecture.getComponents()) {
	
		for (Class cls: component.getClasses()){
			depIn += searchClassDependencies(cls, component);
			//System.out.println("DepIn- Classe: "+ cls.getName() + " :" + depIn);
		}
		
		this.results += depIn; // somatorio de DepIn da arquitetura como um todo
		depIn= 0;
	}
	
}

//----------------------------------------------------------------------------------

	private int searchClassDependencies (Class source, Component comp){
		List<Class> depClasses = new ArrayList<Class> ();
			
		for (Class c: comp.getClasses()){
			List<InterClassRelationship> relationships = new ArrayList<InterClassRelationship> (source.getRelationships());
			for (InterClassRelationship relationship : relationships) {
								
				if (relationship instanceof DependencyInterClassRelationship){
					DependencyInterClassRelationship dependency = (DependencyInterClassRelationship) relationship;
					if (dependency.getSupplier().equals(source.getName()) && (!(depClasses.contains(c)))) {
						depClasses.add(c);
					}
				}
			}
		}//end for classes
		
		return depClasses.size();
		}

	// ---------------------------------------------------------------------------------

	public int getResults() {
			return results;
		}
	
}
