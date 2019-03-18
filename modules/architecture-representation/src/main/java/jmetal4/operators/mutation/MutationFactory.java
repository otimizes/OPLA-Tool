//  MutationFactory.java
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

package jmetal4.operators.mutation;

import jmetal4.util.Configuration;
import jmetal4.util.JMException;

import java.util.Map;


/**
 * Class implementing a factory for Mutation objects.
 */
public class MutationFactory {

    /**
     * Gets a crossover operator through its name.
     *
     * @param name of the operator
     * @return the operator
     * @throws JMException
     */
    public static Mutation getMutationOperator(String name, Map<String, Object> parameters) throws JMException {

        if (name.equalsIgnoreCase("PLAFeatureMutation"))
            return new PLAFeatureMutation(parameters);
        else {
            Configuration.logger_.severe("Operator '" + name + "' not found ");
            Class<?> cls = java.lang.String.class;
            String name2 = cls.getName();
            throw new JMException("Exception in " + name2 + ".getMutationOperator()");
        }
    } // getMutationOperator
} // MutationFactory
