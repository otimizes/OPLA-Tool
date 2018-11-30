package jmetal4.interactive;

import jmetal4.core.SolutionSet;
import results.Execution;
import results.Experiment;

/**
 * Interface that allows to adapt the method of interaction with the user
 */
public interface InteractiveFunction {

    void run(SolutionSet solutionSet, Execution execution);
}
