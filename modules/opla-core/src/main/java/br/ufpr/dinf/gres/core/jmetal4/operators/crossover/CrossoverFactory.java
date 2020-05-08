//  CrossoverFactory.java
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

package br.ufpr.dinf.gres.core.jmetal4.operators.crossover;

import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.experiments.ExperimentCommonConfigs;

import java.util.Map;

/**
 * Class implementing a factory for crossover operators.
 */
public class CrossoverFactory {

    public static Crossover getCrossoverOperator(String name, Map<String, Object> parameters,
                                                 ExperimentCommonConfigs configs) throws JMException {
        if (name.equalsIgnoreCase("PLACrossover")) {
            return new PLACrossover2(parameters);
        } else if (name.equals("PLAComplementaryCrossover")) {
            return new PLAComplementaryCrossover(parameters, configs);
        } else {
            throw new JMException("Exception in " + name + ".getCrossoverOperator()");
        }
    }

    public static Crossover getCrossoverOperator(String name, Map<String, Object> parameters) throws JMException {
        if (name.equalsIgnoreCase("PLACrossover"))
            return new PLACrossover2(parameters);
        else {
            throw new JMException("Exception in " + name + ".getCrossoverOperator()");
        }
    }

}
