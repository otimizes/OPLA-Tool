package jmetal4core.interactive;

import jmetal4core.core.SolutionSet;

/**
 * Interface that allows to adapt the method of interaction with the user
 */
public interface InteractiveFunction {

    jmetal4core.core.SolutionSet run(SolutionSet solutionSet);
}
