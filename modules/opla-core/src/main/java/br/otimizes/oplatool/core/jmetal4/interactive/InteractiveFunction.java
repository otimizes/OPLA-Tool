package br.otimizes.oplatool.core.jmetal4.interactive;

import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;

/**
 * Interface that allows to adapt the method of interaction with the user
 */
public interface InteractiveFunction {

    SolutionSet run(SolutionSet solutionSet);
}
