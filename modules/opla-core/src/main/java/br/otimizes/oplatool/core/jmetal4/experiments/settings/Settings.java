//  Settings.java
//
//  Authors:
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

package br.otimizes.oplatool.core.jmetal4.experiments.settings;

import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.ArrayRealSolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.BinaryRealSolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.BinarySolutionType;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.RealSolutionType;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.Crossover;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverFactory;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.MutationFactory;
import br.otimizes.oplatool.common.exceptions.JMException;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Class representing Settings objects.
 */
public abstract class Settings {
    protected Problem problem_;
    protected String problemName_;
    protected String paretoFrontFile_;

    public Settings() {
    } // Constructor

    public Settings(String problemName) {
        problemName_ = problemName;
    } // Constructor

    /**
     * Default configure method
     *
     * @return A problem with the default configuration
     * @throws JMException default exception
     */
    abstract public Algorithm configure() throws JMException;

    /**
     * Configure method. Change the default configuration
     *
     * @param settings settings
     * @return A problem with the settings indicated as argument
     * @throws JMException            default exception 1
     * @throws ClassNotFoundException default exception 2
     */
    public final Algorithm configure(HashMap settings) throws JMException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        if (settings != null) {
            Field[] fields = this.getClass().getFields();
            for (Field field : fields) {
                if (field.getName().endsWith("_")) { // it is a configuration field
                    // The configuration field is an integer
                    if (field.getType().equals(int.class) ||
                            field.getType().equals(Integer.class)) {
                        if (settings.containsKey(field.getName())) {
                            Integer value = (Integer) settings.get(field.getName());
                            field.setInt(this, value.intValue());
                        }
                    } else if (field.getType().equals(double.class) ||
                            field.getType().equals(Double.class)) {
                        Double value = (Double) settings.get(field.getName());

                        if (settings.containsKey(field.getName())) {
                            if (field.getName().equals("mutationProbability_") &&
                                    value == null) {
                                if ((problem_.getSolutionType().getClass() == RealSolutionType.class) ||
                                        (problem_.getSolutionType().getClass() == ArrayRealSolutionType.class)) {
                                    value = 1.0 / problem_.getNumberOfVariables();
                                } else if (problem_.getSolutionType().getClass() == BinarySolutionType.class ||
                                        problem_.getSolutionType().getClass() == BinaryRealSolutionType.class) {
                                    int length = problem_.getNumberOfBits();
                                    value = 1.0 / length;
                                } else {
                                    int length = 0;
                                    for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
                                        length += problem_.getLength(j);
                                    }
                                    value = 1.0 / length;
                                }
                                field.setDouble(this, value);
                            } // if
                            else {
                                field.setDouble(this, value);
                            }
                        }
                    } else {
                        Object value = settings.get(field.getName());
                        if (value != null) {
                            if (field.getType().equals(Crossover.class)) {
                                Object value2 = CrossoverFactory.getCrossoverOperator((String) value, settings);
                                value = value2;
                            }

                            if (field.getType().equals(Mutation.class)) {
                                Object value2 = MutationFactory.getMutationOperator((String) value, settings);
                                value = value2;
                            }

                            field.set(this, value);
                        }
                    }
                }
            } // for

            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType().equals(Crossover.class) ||
                        fields[i].getType().equals(Mutation.class)) {
                    Operator operator = (Operator) fields[i].get(this);
                    String tmp = fields[i].getName();
                    String aux = fields[i].getName().substring(0, tmp.length() - 1);

                    for (int j = 0; j < fields.length; j++) {
                        if (i != j) {
                            if (fields[j].getName().startsWith(aux)) {
                                tmp = fields[j].getName().substring(aux.length(), fields[j].getName().length() - 1);

                                if (
                                        (fields[j].get(this) != null)) {
                                    if (fields[j].getType().equals(int.class) ||
                                            fields[j].getType().equals(Integer.class)) {
                                        operator.setParameter(tmp, (double) fields[j].getInt(this));
                                    } else if (fields[j].getType().equals(double.class) ||
                                            fields[j].getType().equals(Double.class)) {
                                        operator.setParameter(tmp, fields[j].getDouble(this));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            paretoFrontFile_ = (String) settings.get("paretoFrontFile_");
        }
        return configure();
    } // configure
} // Settings