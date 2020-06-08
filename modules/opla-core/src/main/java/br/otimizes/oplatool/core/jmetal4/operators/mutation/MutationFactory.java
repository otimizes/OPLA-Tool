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

package br.otimizes.oplatool.core.jmetal4.operators.mutation;

import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.experiments.ExperimentCommonConfigs;
import br.otimizes.oplatool.core.jmetal4.operators.MutationOperators;
import br.otimizes.oplatool.core.jmetal4.operators.pattern.impl.DesignPatternsMutationOperator;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.impl.CustomDesignPatternSelection;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class that call mutation operators
 */
public class MutationFactory {

    private static final String DESIGN_PATTERNS = MutationOperators.DESIGN_PATTERNS.toString();

    public static Mutation getMutationOperator(String name, HashMap<String, Object> parameters, ExperimentCommonConfigs configs) throws JMException {
        if (isOnlyDesignPattern(configs)) {
            return new DesignPatternsMutationOperator(parameters, configs.getDesignPatternStrategy(), new CustomDesignPatternSelection(configs.getPatterns()));
        } else if (isDesignPatternAndPlaFeatureMutation(configs)) {
            DesignPatternsMutationOperator dpm = new DesignPatternsMutationOperator(parameters, configs.getDesignPatternStrategy(),
                    new CustomDesignPatternSelection(configs.getPatterns()));

            configs.excludeDesignPatternsFromMutationOperatorList();
            PLAMutationOperator pf = new PLAMutationOperator(parameters, configs.getMutationOperators());

            return new DesignPatternAndPLAMutation(parameters, dpm, pf);
        } else if (isOnlyPLAFeatureMutation(configs)) {
            return new PLAMutationOperator(parameters, configs.getMutationOperators());
        } else {
            Configuration.logger_.severe("Operator '" + name + "' not found ");
            Class<String> cls = java.lang.String.class;
            String name2 = cls.getName();
            throw new JMException("Exception in " + name2 + ".getMutationOperator()");
        }
    }

    private static boolean isOnlyPLAFeatureMutation(ExperimentCommonConfigs configs) {
        return !configs.getMutationOperators().contains(DESIGN_PATTERNS) && configs.getMutationOperators().size() >= 1;
    }

    private static boolean isDesignPatternAndPlaFeatureMutation(ExperimentCommonConfigs configs) {
        return configs.getMutationOperators().contains(DESIGN_PATTERNS) && configs.getMutationOperators().size() > 1;
    }

    private static boolean isOnlyDesignPattern(ExperimentCommonConfigs configs) {
        return configs.getMutationOperators().contains(DESIGN_PATTERNS) && configs.getMutationOperators().size() == 1;
    }


    public static Mutation getMutationOperatorPatterns(String name, HashMap<String, Object> parameters,
                                                       ExperimentCommonConfigs configs) throws JMException {

        return new DesignPatternsMutationOperator(parameters, configs.getDesignPatternStrategy(),
                new CustomDesignPatternSelection(configs.getPatterns()));
    }


    public static Mutation getMutationOperator(String name, Map<String, Object> parameters) throws JMException {

        if (name.equalsIgnoreCase("PLAMutationOperator"))
            return new PLAMutationOperator(parameters);
        else {
            Configuration.logger_.severe("Operator '" + name + "' not found ");
            Class<?> cls = java.lang.String.class;
            String name2 = cls.getName();
            throw new JMException("Exception in " + name2 + ".getMutationOperator()");
        }
    }
}
