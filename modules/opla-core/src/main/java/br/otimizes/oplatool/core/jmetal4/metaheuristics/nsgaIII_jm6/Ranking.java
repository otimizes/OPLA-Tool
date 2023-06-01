package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII_jm6;

import java.util.List;

import br.otimizes.oplatool.core.jmetal4.core.Solution;

/**
 * Ranks a list of population according to the dominance relationship
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface Ranking {
  Ranking compute(List<Solution> solutionList) ;
  List<Solution> getSubFront(int rank) ;
  int getNumberOfSubFronts() ;
  Integer getRank(Solution solution) ;
  Object getAttributedId() ;
}
