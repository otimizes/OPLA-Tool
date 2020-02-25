package jmetal4.interactive;

import jmetal4.core.core.SolutionSet;

/**
 * Interface that allows to adapt the method of interaction with the user
 */
public interface InteractiveFunction {

    SolutionSet run(SolutionSet solutionSet);
}
