package br.ufpr.dinf.gres.core.jmetal4.metrics.classical;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyOut;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.DependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.DependencyOut;

public class ACLASS extends BaseMetricResults {

    public ACLASS(Architecture architecture) {
        super(architecture);
        double aclassFitness = 0.0;
        ClassDependencyIn CDepIN = new ClassDependencyIn(architecture);
        ClassDependencyOut CDepOUT = new ClassDependencyOut(architecture);
        aclassFitness = CDepIN.getResults() + CDepOUT.getResults();
        this.setResults(aclassFitness);
    }

}
