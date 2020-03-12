package br.ufpr.dinf.gres.core.jmetal4.metrics.all;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyOut;

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
