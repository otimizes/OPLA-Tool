package br.uem.din.metrics.conventionalMetrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.uem.din.architectureEvolution.representation.Architecture;
import br.uem.din.architectureEvolution.representation.Component;
import br.uem.din.architectureEvolution.representation.InterClassRelationship;
import br.uem.din.architectureEvolution.representation.Interface;
import br.uem.din.architectureEvolution.representation.Class;

public class MeanDepComponents {

	/**
	 * @param args
	 */
	private Architecture architecture;
	private double results;
	
	public MeanDepComponents(Architecture architecture) {
		
		this.architecture = architecture;
		this.results = 0;
		List<Component> depComponents = new ArrayList<Component> ();
		int totalComponents = architecture.getComponents().size();
		int totalDependencies =0;
		for (Component component : architecture.getComponents()) {
			depComponents.clear();
			for (Interface itf: component.getRequiredInterfaces()){
				for (Component c: itf.getImplementors()){
			    	if (!(depComponents.contains(c))) depComponents.add(c);
			    }
			    
			}
			totalDependencies += depComponents.size();
		    
		}
		if (totalComponents != 0 ){
			this.results = totalDependencies / totalComponents;
		}
	}

	public double getResults() {
		return results;
	}
	
}
