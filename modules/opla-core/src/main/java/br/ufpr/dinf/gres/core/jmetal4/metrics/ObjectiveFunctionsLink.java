package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ObjectiveFunctionDomain;

public interface ObjectiveFunctionsLink {
    Double evaluate(Architecture architecture);

    ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement,
                                  Architecture arch);
}
