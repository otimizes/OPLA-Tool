//  Real.java
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

package br.otimizes.oplatool.core.jmetal4.encodings.variable;

import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.Variable;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;


/**
 * This class implements a Real value decision variable
 */
public class Real extends Variable {

    private static final long serialVersionUID = -5616809484038914366L;

    /**
     * Stores the value of the real variable
     */
    private double value_;

    /**
     * Stores the lower bound of the real variable
     */
    private double lowerBound_;

    /**
     * Stores the upper bound of the real variable
     */
    private double upperBound_;

    public Real() {
    } // Real


    /**
     * Constructor
     *
     * @param lowerBound Lower limit for the variable
     * @param upperBound Upper limit for the variable
     */
    public Real(double lowerBound, double upperBound) {
        lowerBound_ = lowerBound;
        upperBound_ = upperBound;
        value_ = PseudoRandom.randDouble() * (upperBound - lowerBound) + lowerBound;
    } //Real


    /**
     * Copy constructor.
     *
     * @param variable The variable to copy.
     * @throws JMException exception
     */
    public Real(Variable variable) throws JMException {
        lowerBound_ = variable.getLowerBound();
        upperBound_ = variable.getUpperBound();
        value_ = variable.getValue();
    } //Real

    /**
     * Gets the value of the <code>Real</code> variable.
     *
     * @return the value.
     */
    public double getValue() {
        return value_;
    } // getValue

    /**
     * Sets the value of the variable.
     *
     * @param value The value.
     */
    public void setValue(double value) {
        value_ = value;
    } // setValue

    /**
     * Returns a exact copy of the <code>Real</code> variable
     *
     * @return the copy
     */
    public Variable deepCopy() {
        try {
            return new Real(this);
        } catch (JMException e) {
            Configuration.logger_.severe("Real.deepCopy.execute: JMException");
            return null;
        }
    } // deepCopy


    /**
     * Gets the lower bound of the variable.
     *
     * @return the lower bound.
     */
    public double getLowerBound() {
        return lowerBound_;
    } //getLowerBound

    /**
     * Sets the lower bound of the variable.
     *
     * @param lowerBound The lower bound.
     */
    public void setLowerBound(double lowerBound) {
        lowerBound_ = lowerBound;
    } // setLowerBound

    /**
     * Gets the upper bound of the variable.
     *
     * @return the upper bound.
     */
    public double getUpperBound() {
        return upperBound_;
    } // getUpperBound

    /**
     * Sets the upper bound of the variable.
     *
     * @param upperBound The upper bound.
     */
    public void setUpperBound(double upperBound) {
        upperBound_ = upperBound;
    } // setUpperBound


    /**
     * Returns a string representing the object
     *
     * @return the string
     */
    public String toString() {
        return value_ + "";
    } //toString
} // Real
