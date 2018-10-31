package jmetal4.interactive;

import jmetal4.core.SolutionSet;
import results.Execution;
import results.Experiment;

public interface InteractiveFunction {

    void run(SolutionSet solutionSet, Execution execution);
}
