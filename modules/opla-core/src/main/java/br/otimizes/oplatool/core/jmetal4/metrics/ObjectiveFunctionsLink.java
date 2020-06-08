package br.otimizes.oplatool.core.jmetal4.metrics;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ObjectiveFunctionDomain;

public interface ObjectiveFunctionsLink {
    Double evaluate(Architecture architecture);

    ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement,
                                  Architecture arch);
}
