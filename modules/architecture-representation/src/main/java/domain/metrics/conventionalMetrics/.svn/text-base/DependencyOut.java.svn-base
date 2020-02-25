package br.uem.din.metrics.conventionalMetrics;

import java.util.ArrayList;
import java.util.List;

import br.uem.din.architectureEvolution.representation.AbstractionInterElementRelationship;
import br.uem.din.architectureEvolution.representation.Architecture;
import br.uem.din.architectureEvolution.representation.Component;
import br.uem.din.architectureEvolution.representation.DependencyComponentInterfaceRelationship;
import br.uem.din.architectureEvolution.representation.InterElementRelationship;

public class DependencyOut {

	/**
	 * @param args
	 */
	private Architecture architecture;
	private int results;
	
  public DependencyOut(Architecture architecture) {

		this.architecture = architecture;
		this.results = 0;
		int depOut = 0;
		
	for (Component component : architecture.getComponents()) {
		List<InterElementRelationship> relationships = new ArrayList<InterElementRelationship> (architecture.getInterElementRelationships());
		for (InterElementRelationship relationship : relationships) {
				if (relationship instanceof DependencyComponentInterfaceRelationship){
					DependencyComponentInterfaceRelationship dependency = (DependencyComponentInterfaceRelationship) relationship;
					if (dependency.getComponent().equals(component)) depOut++;
				}
		}
		this.results += depOut; // somatorio de DepOut da arquitetura como um todo
		depOut= 0;
	}
  }

	public int getResults() {
		return results;
	}

}
