package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Mean of dependencies of components
 */
public class MeanDepComponents extends ObjectiveFunctionImplementation {
    public MeanDepComponents(Architecture architecture) {
        super(architecture);
        this.setResults(0.0);
        List<Element> depComponents = new ArrayList<>();
        int totalComponents = architecture.getAllPackages().size();
        int totalDependencies = 0;

        for (Package component : architecture.getAllPackages()) {
            for (Interface itf : component.getRequiredInterfaces()) {
                for (Element c : itf.getImplementors()) {
                    if (!depComponents.contains(c))
                        depComponents.add(c);
                }
            }
            totalDependencies += depComponents.size();

        }
        if (totalComponents != 0) {
            this.setResults(totalDependencies / totalComponents);
        }
    }

}
