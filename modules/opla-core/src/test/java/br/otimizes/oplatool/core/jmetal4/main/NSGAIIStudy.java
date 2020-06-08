//  NSGAIIStudy.java
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

package br.otimizes.oplatool.core.jmetal4.main;

import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.experiments.Experiment;
import br.otimizes.oplatool.core.jmetal4.experiments.Settings;
import br.otimizes.oplatool.core.jmetal4.experiments.settings.NSGAII_Settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class implementing an example of experiment using NSGA-II as base algorithm.
 * The experiment consisting in studying the effect of the crossover probability
 * in NSGA-II.
 */
public class NSGAIIStudy extends Experiment {
    public static void main(String[] args) throws JMException, IOException {
        NSGAIIStudy exp = new NSGAIIStudy(); // exp = experiment

        exp.experimentName_ = "NSGAIIStudy";
        exp.algorithmNameList_ = new String[]{
                "NSGAIIa", "NSGAIIb", "NSGAIIc", "NSGAIId"};
        exp.problemList_ = new String[]{
                "ZDT1", "ZDT2", "ZDT3", "ZDT4", "DTLZ1", "WFG2"};
        exp.paretoFrontFile_ = new String[]{
                "ZDT1.pf", "ZDT2.pf", "ZDT3.pf", "ZDT4.pf", "DTLZ1.2D.pf", "WFG2.2D.pf"};
        exp.indicatorList_ = new String[]{"HV", "SPREAD", "IGD", "EPSILON"};

        int numberOfAlgorithms = exp.algorithmNameList_.length;

        exp.experimentBaseDirectory_ = System.getProperty("user.home") + "/oplatool/output/";
        exp.paretoFrontDirectory_ = System.getProperty("user.home") + "/oplatool/output/";

        exp.algorithmSettings_ = new Settings[numberOfAlgorithms];

        exp.independentRuns_ = 4;

        int numberOfThreads;
        exp.runExperiment(numberOfThreads = 1);
        exp.generateLatexTables();
        int rows;
        int columns;
        String prefix;
        String[] problems;

        rows = 2;
        columns = 3;
        prefix = "Problems";
        problems = new String[]{"ZDT1", "ZDT2", "ZDT3", "ZDT4", "DTLZ1", "WFG2"};

        boolean notch;
        exp.generateRBoxplotScripts(rows, columns, problems, prefix, notch = true, exp);
        exp.generateRWilcoxonScripts(problems, prefix, exp);
    } // main

    public synchronized void algorithmSettings(String problemName,
                                               int problemIndex,
                                               Algorithm[] algorithm)
            throws ClassNotFoundException {
        try {
            int numberOfAlgorithms = algorithmNameList_.length;
            HashMap[] parameters = new HashMap[numberOfAlgorithms];
            for (int i = 0; i < numberOfAlgorithms; i++) {
                parameters[i] = new HashMap();
            } // for

            parameters[0].put("crossoverProbability_", 1.0);
            parameters[1].put("crossoverProbability_", 0.9);
            parameters[2].put("crossoverProbability_", 0.8);
            parameters[3].put("crossoverProbability_", 0.7);

            if ((!paretoFrontFile_[problemIndex].equals("")) ||
                    (paretoFrontFile_[problemIndex] == null)) {
                for (int i = 0; i < numberOfAlgorithms; i++)
                    parameters[i].put("paretoFrontFile_", paretoFrontFile_[problemIndex]);
            } // if

            for (int i = 0; i < numberOfAlgorithms; i++)
                algorithm[i] = new NSGAII_Settings(problemName).configure(parameters[i]);

        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NSGAIIStudy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NSGAIIStudy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMException ex) {
            Logger.getLogger(NSGAIIStudy.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // algorithmSettings
} // NSGAIIStudy


