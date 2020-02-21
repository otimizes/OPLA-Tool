//  GenerationalDistance.java
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

package jmetal4.qualityIndicator;

/**
 * This class implements the generational distance indicator. It can be used also
 * as a command line by typing:
 * "java jmetal4.qualityIndicator.GenerationalDistance <solutionFrontFile>
 * <trueFrontFile> <numberOfObjectives>"
 * Reference: Van Veldhuizen, D.A., Lamont, G.B.: Multiobjective Evolutionary
 * Algorithm Research: A History and Analysis.
 * Technical Report TR-98-03, Dept. Elec. Comput. Eng., Air Force
 * Inst. Technol. (1998)
 */
public class GenerationalDistance {
    static final double pow_ = 2.0;          //pow. This is the pow used for the
    //MetricsUtil funcionalities
    jmetal4.qualityIndicator.util.MetricsUtil utils_;  //utils_ is used to access to the
    //distances

    /**
     * Constructor.
     * Creates a new instance of the generational distance metric.
     */
    public GenerationalDistance() {
        utils_ = new jmetal4.qualityIndicator.util.MetricsUtil();
    } // GenerationalDistance

    /**
     * Returns the generational distance value for a given front
     *
     * @param front           The front
     * @param trueParetoFront The true pareto front
     */
    public double generationalDistance(double[][] front,
                                       double[][] trueParetoFront,
                                       int numberOfObjectives) {

        // STEP 3. Sum the distances between each point of the front and the
        // nearest point in the true Pareto front
        double sum = 0.0;
        for (int i = 0; i < front.length; i++)
            sum += Math.pow(utils_.distanceToClosedPoint(front[i], trueParetoFront), pow_);

        // STEP 4. Obtain the sqrt of the sum
        sum = Math.pow(sum, 1.0 / pow_);

        // STEP 5. Divide the sum by the maximum number of points of the front
        System.out.println("GDvalor front = " + front.length);
        System.out.println("GDvalor truefront = " + trueParetoFront.length);
        double generationalDistance = (sum / front.length);

        return generationalDistance;
    } // generationalDistance

} // GenerationalDistance
