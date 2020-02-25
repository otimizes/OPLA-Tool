package jmetal4.core.encodings.solutionType;

import jmetal4.core.core.Problem;
import jmetal4.core.core.SolutionType;
import domain.Variable;
import jmetal4.core.problems.OPLA;

//criado por Thelma em agosto/2012

/**
 * Class representing the solution type of solutions composed of Architecture
 * variables
 */

public class ArchitectureSolutionType extends SolutionType {

    /**
     * Constructor
     *
     * @param problem
     * @throws ClassNotFoundException
     */

    public ArchitectureSolutionType(Problem problem) throws ClassNotFoundException {
        super(problem);
    }

    /**
     * Creates the variables of the solution
     *
     * @param decisionVariables
     */
    public Variable[] createVariables() {
        Variable[] variables = new Variable[problem_.getNumberOfVariables()];

        for (int var = 0; var < problem_.getNumberOfVariables(); var++)
            if (problem_.getName() == "OPLA") {
                variables[var] = ((OPLA) problem_).architecture_.deepCopy();
            }

        return variables;
    }

}
