package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;

/**
 * Cohesion (Santos et al., 2015)
 * <p>
 * It aims to measure the relational cohesion between classes.
 * (H - Relational Cohesion)
 */
public class COE extends ObjectiveFunctionImplementation {

    public COE(Architecture architecture) {
        super(architecture);
        RelationalCohesion rc = new RelationalCohesion(architecture);
        this.setResults(rc.getResults());
    }

}
