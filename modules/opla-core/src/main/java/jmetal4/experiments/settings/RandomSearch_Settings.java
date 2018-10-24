////  RandomSearch_Settings.java 
////
////  Authors:
////       Antonio J. Nebro <antonio@lcc.uma.es>
////       Juan J. Durillo <durillo@lcc.uma.es>
////
////  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
////
////  This program is free software: you can redistribute it and/or modify
////  it under the terms of the GNU Lesser General Public License as published by
////  the Free Software Foundation, either version 3 of the License, or
////  (at your option) any later version.
////
////  This program is distributed in the hope that it will be useful,
////  but WITHOUT ANY WARRANTY; without even the implied warranty of
////  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
////  GNU Lesser General Public License for more details.
//// 
////  You should have received a copy of the GNU Lesser General Public License
////  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//package jmetal4.experiments.settings;
//
//import jmetal4.core.Algorithm;
//import jmetal4.experiments.Settings;
//import jmetal4.metaheuristics.randomSearch.RandomSearch;
//import jmetal4.problems.ProblemFactory;
//import jmetal4.qualityIndicator.QualityIndicator;
//import jmetal4.util.JMException;
//
///**
// * Settings class of algorithm RandomSearch
// */
//public class RandomSearch_Settings extends Settings {
//  // Default settings
//  public int maxEvaluations_ = 25000;
//  
//  /**
//   * Constructor
//   * @param problem Problem to solve
//   */
//  public RandomSearch_Settings(String problem) {
//    super(problem);
//    
//    Object [] problemParams = {"Real"};
//    try {
//	    problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
//    } catch (JMException e) {
//	    e.printStackTrace();
//    }      
//  } // RandomSearch_Settings
//
//  /**
//   * Configure the MOCell algorithm with default parameter settings
//   * @return an algorithm object
//   * @throws jmetal4.util.JMException
//   */
//  public Algorithm configure() throws JMException {
//    Algorithm algorithm;
//
//    QualityIndicator indicators;
//
//    // Creating the problem
//    algorithm = new RandomSearch(problem_);
//
//    // Algorithm parameters
//    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
//
//    // Creating the indicator object
//    if ((paretoFrontFile_!=null) && (!paretoFrontFile_.equals(""))) {
//      indicators = new QualityIndicator(problem_, paretoFrontFile_);
//      algorithm.setInputParameter("indicators", indicators);
//    } // if
//    return algorithm;
//  } // Constructor
//} // RandomSearch_Settings
