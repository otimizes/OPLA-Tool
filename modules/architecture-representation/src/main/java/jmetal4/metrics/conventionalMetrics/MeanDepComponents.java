package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;

import java.util.ArrayList;
import java.util.List;

public class MeanDepComponents {

    /**
     * @param args
     */
    private Architecture architecture;
    private double results;

    public MeanDepComponents(Architecture architecture) {

        this.architecture = architecture;
        this.results = 0;
        List<Element> depComponents = new ArrayList<Element>();
        int totalComponents = this.architecture.getAllPackages().size();
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
            this.results = totalDependencies / totalComponents;
        }
    }

    public double getResults() {
        return results;
    }

}
