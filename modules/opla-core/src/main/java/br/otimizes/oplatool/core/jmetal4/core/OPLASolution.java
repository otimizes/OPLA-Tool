package br.otimizes.oplatool.core.jmetal4.core;

/**
 * OPLA extension of solution, implement all new methods here
 */
public class OPLASolution extends Solution {

    private static final long serialVersionUID = 1L;

    private Solution solution;

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public OPLASolution(Solution input) {
        this.setSolution(new Solution(input));
    }
}
