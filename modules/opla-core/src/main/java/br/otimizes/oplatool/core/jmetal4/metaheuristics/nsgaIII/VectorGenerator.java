package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;

public abstract class VectorGenerator {
    protected double[][] lambda_;

    public double[][] getVectors() {
        return this.lambda_;
    }


}
