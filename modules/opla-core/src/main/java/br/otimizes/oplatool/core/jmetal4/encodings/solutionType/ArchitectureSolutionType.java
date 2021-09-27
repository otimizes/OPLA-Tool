package br.otimizes.oplatool.core.jmetal4.encodings.solutionType;

import br.otimizes.oplatool.common.Variable;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.SolutionType;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;

/**
 * Class representing the solution type of solutions composed of Architecture
 * variables
 */

public class ArchitectureSolutionType extends SolutionType {

    public ArchitectureSolutionType(Problem problem) {
        super(problem);
    }

    /**
     * Create variables array
     *
     * @return variables
     */
    public Variable[] createVariables() {
        Variable[] variables = new Variable[problem_.getNumberOfVariables()];
        for (int variableIndex = 0; variableIndex < problem_.getNumberOfVariables(); variableIndex++)
            if (problem_.getName().equals("OPLA")) {
                variables[variableIndex] = ((OPLA) problem_).architecture_.deepCopy();
            }
        return variables;
    }
}
