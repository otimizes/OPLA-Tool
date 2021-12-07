package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;

import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;

public interface Ranking {
    public SolutionSet getSubfront(int layer);

    public int getNumberOfSubfronts();
}