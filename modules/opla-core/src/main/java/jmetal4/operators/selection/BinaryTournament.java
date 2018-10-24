//  BinaryTournament.java
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

package jmetal4.operators.selection;

import java.util.Comparator;
import java.util.Map;

import org.apache.log4j.Logger;

import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import jmetal4.util.PseudoRandom;
import jmetal4.util.comparators.DominanceComparator;

/**
 * This class implements an binary tournament selection operator
 */
public class BinaryTournament extends Selection {
	
	private static final Logger LOGGER = Logger.getLogger(BinaryTournament.class);

    /**
     * Stores the <code>Comparator</code> used to compare two
     * solutions
     */
    private Comparator comparator_;

    /**
     * Constructor
     * Creates a new Binary tournament operator using a BinaryTournamentComparator
     */
    public BinaryTournament(Map<String, Object> parameters) {
        super(parameters);
        if ((parameters != null) && (parameters.get("comparator") != null))
            comparator_ = (Comparator) parameters.get("comparator");
        else
            //comparator_ = new BinaryTournamentComparator();
            comparator_ = new DominanceComparator();
    } // BinaryTournament

    /**
     * Performs the operation
     *
     * @param object Object representing a SolutionSet
     * @return the selected solution
     */
    public Object execute(Object object) {
    	LOGGER.info("execute()");
        SolutionSet solutionSet = (SolutionSet) object;
        Solution solution1, solution2;
        
        LOGGER.info("Iniciando seleção randomina no espaço de busca de " + solutionSet.size());
        solution1 = solutionSet.get(PseudoRandom.randInt(0, solutionSet.size() - 1));
        solution2 = solutionSet.get(PseudoRandom.randInt(0, solutionSet.size() - 1));
        LOGGER.info("Seleção terminada");
        
        if (solutionSet.size() >= 2) {
        	LOGGER.info("Soluções Iguais, buscando uma nova solução:");
        	while (solution1 == solution2) {
        		solution2 = solutionSet.get(PseudoRandom.randInt(0, solutionSet.size() - 1));
        	}
        }
        LOGGER.info("Busca concluída");

        LOGGER.info("Compare");
        int flag = comparator_.compare(solution1, solution2);
        if (flag == -1)
            return solution1;
        else if (flag == 1)
            return solution2;
        else if (PseudoRandom.randDouble() < 0.5)
            return solution1;
        else
            return solution2;
    } // execute
} // BinaryTournament
