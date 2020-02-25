package domain;

import domain.core.SolutionSet;

/**
 * Interface that allows to adapt the method of interaction with the user
 */
public interface InteractiveFunction {

    SolutionSet run(SolutionSet solutionSet);
}
