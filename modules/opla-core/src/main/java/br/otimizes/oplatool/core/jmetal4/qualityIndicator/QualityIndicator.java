//  QualityIndicator.java
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

package br.otimizes.oplatool.core.jmetal4.qualityIndicator;

import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.util.MetricsUtil;
import org.apache.log4j.Logger;

/**
 * QualityIndicator class
 */
public class QualityIndicator {

    private static final Logger LOGGER = Logger.getLogger(QualityIndicator.class);

    SolutionSet trueParetoFront_;
    double trueParetoFrontHypervolume_;
    Problem problem_;
    MetricsUtil utilities_;

    public QualityIndicator(Problem problem, String paretoFrontFile) {
        problem_ = problem;
        utilities_ = new MetricsUtil();
        trueParetoFront_ = utilities_.readNonDominatedSolutionSet(paretoFrontFile);
        trueParetoFrontHypervolume_ = new Hypervolume().hypervolume(
                trueParetoFront_.writeObjectivesToMatrix(),
                trueParetoFront_.writeObjectivesToMatrix(),
                problem_.getNumberOfObjectives());
    } // Constructor

    /**
     * Returns the hypervolume of solution set
     *
     * @param solutionSet
     * @return The value of the hypervolume indicator
     */
    public double getHypervolume(SolutionSet solutionSet) {
        LOGGER.info("getHypervolume()");
        return new Hypervolume().hypervolume(solutionSet.writeObjectivesToMatrix(),
                trueParetoFront_.writeObjectivesToMatrix(),
                problem_.getNumberOfObjectives());
    } // getHypervolume


    /**
     * Returns the hypervolume of the true Pareto front
     *
     * @return The hypervolume of the true Pareto front
     */
    public double getTrueParetoFrontHypervolume() {
        return trueParetoFrontHypervolume_;
    }

    /**
     * Returns the inverted generational distance of solution set
     *
     * @param solutionSet
     * @return The value of the hypervolume indicator
     */
    public double getIGD(SolutionSet solutionSet) {
        return new InvertedGenerationalDistance().invertedGenerationalDistance(
                solutionSet.writeObjectivesToMatrix(),
                trueParetoFront_.writeObjectivesToMatrix(),
                problem_.getNumberOfObjectives());
    } // getIGD

    /**
     * Returns the generational distance of solution set
     *
     * @param solutionSet
     * @return The value of the hypervolume indicator
     */
    public double getGD(SolutionSet solutionSet) {
        return new GenerationalDistance().generationalDistance(
                solutionSet.writeObjectivesToMatrix(),
                trueParetoFront_.writeObjectivesToMatrix(),
                problem_.getNumberOfObjectives());
    } // getGD


    public double getGD(SolutionSet front, SolutionSet trueFront, int numberOfObjectives) {
        return new GenerationalDistance().generationalDistance(front.writeObjectivesToMatrix(),
                trueFront.writeObjectivesToMatrix(), numberOfObjectives);
    } // getGD

    public double getIGD(SolutionSet front, SolutionSet trueFront, int numberOfObjectives) {
        System.out.println("IGD");
        return new InvertedGenerationalDistance().invertedGenerationalDistance(front.writeObjectivesToMatrix(),
                trueFront.writeObjectivesToMatrix(), numberOfObjectives);
    } // getIGD

    /**
     * Returns the spread of solution set
     *
     * @param solutionSet
     * @return The value of the hypervolume indicator
     */
    public double getSpread(SolutionSet solutionSet) {
        return new Spread().spread(solutionSet.writeObjectivesToMatrix(),
                trueParetoFront_.writeObjectivesToMatrix(),
                problem_.getNumberOfObjectives());
    } // getGD

    /**
     * Returns the epsilon indicator of solution set
     *
     * @param solutionSet
     * @return The value of the hypervolume indicator
     */
    public double getEpsilon(SolutionSet solutionSet) {
        return new Epsilon().epsilon(solutionSet.writeObjectivesToMatrix(),
                trueParetoFront_.writeObjectivesToMatrix(),
                problem_.getNumberOfObjectives());
    } // getEpsilon
} // QualityIndicator
