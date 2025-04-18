//  Ranking.java
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

package br.otimizes.oplatool.core.jmetal4.util;

import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.util.comparators.DominanceComparator_ant;
import br.otimizes.oplatool.core.jmetal4.util.comparators.OverallConstraintViolationComparator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class implements some facilities for ranking solutions.
 * Given a <code>SolutionSet</code> object, their solutions are ranked
 * according to scheme proposed in NSGA-II; as a result, a set of subsets
 * are obtained. The subsets are numbered starting from 0 (in NSGA-II, the
 * numbering starts from 1); thus, subset 0 contains the non-dominated
 * solutions, subset 1 contains the non-dominated solutions after removing those
 * belonging to subset 0, and so on.
 */
public class Ranking_ant {
	
	private static final Logger LOGGER = Logger.getLogger(Ranking.class);

    /**
     * stores a <code>Comparator</code> for dominance checking
     */
    private static final Comparator dominance_ = new DominanceComparator_ant();
    /**
     * stores a <code>Comparator</code> for Overal Constraint Violation Comparator
     * checking
     */
    private static final Comparator constraint_ = new OverallConstraintViolationComparator();
    /**
     * The <code>SolutionSet</code> to rank
     */
    private SolutionSet solutionSet_;
    /**
     * An array containing all the fronts found during the search
     */
    private SolutionSet[] rankingAnt_;

    /**
     * Constructor.
     *
     * @param solutionSet The <code>SolutionSet</code> to be ranked.
     */
    public Ranking_ant(SolutionSet solutionSet) {
        solutionSet_ = solutionSet;

        // dominateMe[i] contains the number of solutions dominating i
        int[] dominateMe = new int[solutionSet_.size()];

        // iDominate[k] contains the list of solutions dominated by k
        List<Integer>[] iDominate = new List[solutionSet_.size()];

        // front[i] contains the list of individuals belonging to the front i
        List<Integer>[] front = new List[solutionSet_.size() + 1];

        // flagDominate is an auxiliar variable
        int flagDominate;

        // Initialize the fronts
        LOGGER.info("Initialize the fronts");
        for (int i = 0; i < front.length; i++)
            front[i] = new LinkedList<Integer>();

        for (int r=0; r < solutionSet_.size();r++) {
            solutionSet.get(r).checkMajorObjective(solutionSet);
        }

        //-> Fast non dominated sorting algorithm
        for (int p = 0; p < solutionSet_.size(); p++) {
            // Initialice the list of individuals that i dominate and the number
            // of individuals that dominate me
            iDominate[p] = new LinkedList<Integer>();
            dominateMe[p] = 0;

            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = 0; q < solutionSet_.size(); q++) {

                flagDominate = constraint_.compare(solutionSet.get(p), solutionSet.get(q));
                if (flagDominate == 0) {
                    flagDominate = dominance_.compare(solutionSet.get(p), solutionSet.get(q));
                }

// verif.domin. System.out.println("RankingAnt p " + p + "-" + solutionSet.get(p) + " q " + q + "-" + solutionSet.get(q) + " " + flagDominate);

                if (flagDominate == -1) {
                    iDominate[p].add(new Integer(q));
                } else if (flagDominate == 1) {
                    dominateMe[p]++;
                }
            }

            // If nobody dominates p, p belongs to the first front
            if (dominateMe[p] == 0) {
                front[0].add(new Integer(p));
                solutionSet.get(p).setRank(0);
            }
        }

        //Obtain the rest of fronts
        LOGGER.info("Obtain the rest of fronts");
        int i = 0;
        Iterator<Integer> it1, it2; // Iterators
        while (front[i].size() != 0) {
            i++;
            it1 = front[i - 1].iterator();
            while (it1.hasNext()) {
                it2 = iDominate[it1.next().intValue()].iterator();
                while (it2.hasNext()) {
                    int index = it2.next().intValue();
                    dominateMe[index]--;
                    if (dominateMe[index] == 0) {
                        front[i].add(new Integer(index));
                        solutionSet_.get(index).setRank(i);
                    }
                }
            }
        }
        LOGGER.info("rankingAnt_");
        rankingAnt_ = new SolutionSet[i];
        for (int j = 0; j < i; j++) {
            rankingAnt_[j] = new SolutionSet(front[j].size());
            it1 = front[j].iterator();
            while (it1.hasNext()) {
                rankingAnt_[j].add(solutionSet.get(it1.next().intValue()));
            }
        }

    } // Ranking

    /**
     * Returns a <code>SolutionSet</code> containing the solutions of a given rank.
     *
     * @param rank The rank
     * @return Object representing the <code>SolutionSet</code>.
     */
    public SolutionSet getSubfront(int rank) {
        return rankingAnt_[rank];
    } // getSubFront

    /**
     * Returns the total number of subFronts founds.
     */
    public int getNumberOfSubfronts() {
        return rankingAnt_.length;
    } // getNumberOfSubfronts

    public void clear(){
        solutionSet_.clear();
        for (SolutionSet solutionSet_ : rankingAnt_){
            solutionSet_.clear();
        }
    }
} // Ranking
