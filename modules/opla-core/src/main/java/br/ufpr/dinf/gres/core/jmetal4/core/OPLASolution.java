package br.ufpr.dinf.gres.core.jmetal4.core;

public class OPLASolution extends Solution{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Solution modifySolution;

    public Solution getModifySolution() {
        return modifySolution;
    }

    public void setModifySolution(Solution modifySolution) {
        this.modifySolution = modifySolution;
    }

    public OPLASolution(Solution input){
        this.setModifySolution(new Solution(input));
    }
}
