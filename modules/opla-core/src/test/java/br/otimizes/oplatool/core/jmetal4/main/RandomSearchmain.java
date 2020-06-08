//  RandomSearch_main.java
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

package br.otimizes.oplatool.core.jmetal4.main;

import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.randomSearch.RandomSearch;
import br.otimizes.oplatool.core.jmetal4.problems.Kursawe;
import br.otimizes.oplatool.core.jmetal4.problems.ProblemFactory;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Class for configuring and running the RandomSearch algorithm
 */
public class RandomSearchmain {
    public static Logger logger_;      // Logger object
    public static FileHandler fileHandler_; // FileHandler object

    /**
     * @param args Command line arguments.
     * @throws JMException
     * @throws IOException
     * @throws SecurityException Usage: three options
     *                           - br.otimizes.oplatool.core.jmetal4.main.RandomSearch_main
     *                           - br.otimizes.oplatool.core.jmetal4.main.RandomSearch_main problemName
     */
    public static void main(String[] args) throws
            JMException, SecurityException, IOException, ClassNotFoundException {
        Problem problem;         // The problem to solve
        Algorithm algorithm;         // The algorithm to use
        Operator crossover;         // Crossover operator
        Operator mutation;         // Mutation operator
        Operator selection;         // Selection operator

        QualityIndicator indicators; // Object to get quality indicators

        // Logger object and file to store log messages
        logger_ = Configuration.logger_;
        fileHandler_ = new FileHandler("RandomSearch_main.log");
        logger_.addHandler(fileHandler_);

        indicators = null;
        if (args.length == 1) {
            Object[] params = {"Real"};
            problem = (new ProblemFactory()).getProblem(args[0], params);
        } // if
        else if (args.length == 2) {
            Object[] params = {"Real"};
            problem = (new ProblemFactory()).getProblem(args[0], params);
            indicators = new QualityIndicator(problem, args[1]);
        } // if
        else { // Default problem
            problem = new Kursawe("Real", 3);
            //problem = new Water("Real");
            //problem = new ZDT1("ArrayReal", 1000);
            //problem = new ZDT4("BinaryReal");
            //problem = new WFG1("Real");
            //problem = new DTLZ1("Real");
            //problem = new OKA2("Real") ;
        } // else

        algorithm = new RandomSearch(problem);

        // Algorithm parameters
        algorithm.setInputParameter("maxEvaluations", 25000);

        // Execute the Algorithm
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;

        // Result messages
        logger_.info("Total execution time: " + estimatedTime + "ms");
        logger_.info("Objectives values have been writen to file FUN");
        new OPLASolutionSet(population).printObjectivesToFile("FUN");
        logger_.info("Variables values have been writen to file VAR");
        new OPLASolutionSet(population).printVariablesToFile("VAR");

        if (indicators != null) {
            logger_.info("Quality indicators");
            logger_.info("Hypervolume: " + indicators.getHypervolume(population));
            logger_.info("GD         : " + indicators.getGD(population));
            logger_.info("IGD        : " + indicators.getIGD(population));
            logger_.info("Spread     : " + indicators.getSpread(population));
            logger_.info("Epsilon    : " + indicators.getEpsilon(population));
        } // if
    } //main
} // Randomsearch_main
