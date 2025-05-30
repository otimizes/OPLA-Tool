//  PermutationSolutionType.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
// 
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package br.otimizes.oplatool.core.jmetal4.encodings.solutionType;

import br.otimizes.oplatool.common.Variable;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.SolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.variable.Permutation;

/**
 * Class representing the solution type of solutions composed of Permutation
 * variables
 */
public class PermutationSolutionType extends SolutionType {

    public PermutationSolutionType(Problem problem) throws ClassNotFoundException {
        super(problem);
    } // PermutationSolution

    /**
     * Create variables array
     *
     * @return variables
     */
    public Variable[] createVariables() {
        Variable[] variables = new Variable[problem_.getNumberOfVariables()];
        for (int variableIndex = 0; variableIndex < problem_.getNumberOfVariables(); variableIndex++)
            variables[variableIndex] = new Permutation(problem_.getLength(variableIndex));
        return variables;
    } // createVariables
} // PermutationSolution
