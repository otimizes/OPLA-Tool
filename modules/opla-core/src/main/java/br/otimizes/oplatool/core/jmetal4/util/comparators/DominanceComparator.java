//  DominanceComparator.java
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

package br.otimizes.oplatool.core.jmetal4.util.comparators;

import br.otimizes.oplatool.core.jmetal4.core.Solution;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on a constraint violation test +
 * dominance checking, as in NSGA-II.
 */
public class DominanceComparator implements Comparator {

    /**
     * stores a comparator for check the OverallConstraintComparator
     */
    private static final Comparator overallConstraintViolationComparator_ =
            new OverallConstraintViolationComparator();

    /**
     * Compares two solutions.
     *
     * @param object1 Object representing the first <code>Solution</code>.
     * @param object2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
     * non-dominated, or solution1  is dominated by solution22, respectively.
     */
    public int compare(Object object1, Object object2) {
        if (object1 == null)
            return 1;
        else if (object2 == null)
            return -1;

        Solution solution1 = (Solution) object1;
        Solution solution2 = (Solution) object2;

        int dominate1; // dominate1 indicates if some objective of solution1
        // dominates the same objective in solution2. dominate2
        int dominate2; // is the complementary of dominate1.

        dominate1 = 0;
        dominate2 = 0;

        int flag; //stores the result of the comparison

        if (solution1.getOverallConstraintViolation() !=
                solution2.getOverallConstraintViolation() &&
                (solution1.getOverallConstraintViolation() < 0) ||
                (solution2.getOverallConstraintViolation() < 0)) {
            return (overallConstraintViolationComparator_.compare(solution1, solution2));
        }

        // Equal number of violated constraints. Applying a dominance Test then
        double value1, value2;
/**
// aqui
        if (solution1.getEvaluation() == 4 || solution1.getEvaluatedByUser() || solution1.getEvaluation() == 3 || solution1.getEvaluatedByUser3() || +
                solution2.getEvaluation() == 4 || solution2.getEvaluatedByUser() || solution2.getEvaluation() == 3 || solution2.getEvaluatedByUser3()) {

            System.out.println("==== (79) Dominance MaximoObjetivos " + solution1.getObjectiveMax(0) + " " + +solution1.getObjectiveMax(1) + " " + +solution1.getObjectiveMax(2));
            System.out.println("   = Solution1 " + solution1.getObjective(0) + " " + +solution1.getObjective(1) + " " + +solution1.getObjective(2) + " - Evaluation: " + solution1.getEvaluation() + " Eval4: " + solution1.getEvaluatedByUser() + " Eval3: " + solution1.getEvaluatedByUser3());
            System.out.println("   = Solution2 " + solution2.getObjective(0) + " " + +solution2.getObjective(1) + " " + +solution2.getObjective(2) + " - Evaluation: " + solution2.getEvaluation() + " Eval4: " + solution2.getEvaluatedByUser() + " Eval3: " + solution2.getEvaluatedByUser3());
        }
//
 **/
        String solution1N="", solution2N = "", solution1P="", solution2P = "";

        for (int i = 0; i < solution1.numberOfObjectives(); i++) {

            // não pegar os valores objetivos mas sim os valores normalizados
            //value1 = solution1.getObjective(i);
            //value2 = solution2.getObjective(i);

            //valor normalizado - double (0 - 5)
            value1 = solution1.getObjective(i) / solution1.getObjectiveMax(i) * 5;
            value2 = solution2.getObjective(i) / solution1.getObjectiveMax(i) * 5;
//montar mensagem
            solution1N = solution1N + value1 + " ";
            solution2N = solution2N + value2 + " ";

            // Poderação para avaliações com notas 4 e 3
            if (solution1.getEvaluation() == 4 || solution1.getEvaluation() == 3 ) {
                    value1 = solution1.getObjectiveWithWeight(value1,solution1.getEvaluation());
            }
            if (solution2.getEvaluation() == 4 || solution2.getEvaluation() == 3) {
                    value2 = solution2.getObjectiveWithWeight(value2, solution2.getEvaluation());
            }

            //montar mensagem
            solution1P = solution1P + value1 + " ";
            solution2P = solution2P + value2 + " ";

            if (value1 < value2) {
                flag = -1;
            } else if (value1 > value2) {
                flag = 1;
            } else {
                flag = 0;
            }

            if (flag == -1) {
                dominate1 = 1;
            }
            if (flag == 1) {
                dominate2 = 1;
            }
        }
/***
        if (solution1.getEvaluation() == 4 || solution1.getEvaluatedByUser() || solution1.getEvaluation() == 3 || solution1.getEvaluatedByUser3() || +
                solution2.getEvaluation() == 4 || solution2.getEvaluatedByUser() || solution2.getEvaluation() == 3 || solution2.getEvaluatedByUser3()) {
             System.out.println("   === (130) DominanceCompar - Normalization " +
                     "\n  solution1: " + solution1N +
                     "\n  solution2: " + solution2N );
            System.out.println("   === (133) DominanceCompar - Ponderation " +
                    "\n  solution1: " + solution1P +
                    "\n  solution2: " + solution2P );
        }
**/

         if (dominate1 == dominate2) {
//            System.out.println("Dominancia: iguais ");
            return 0; //No one dominate the other
        }
        if (dominate1 == 1) {
//            System.out.println("Dominancia: obj1 -1");
            return -1; // solution1 dominate
        }
//        System.out.println("Dominancia: obj2 1");
        return 1;    // solution2 dominate
    } // compare
} // DominanceComparator
