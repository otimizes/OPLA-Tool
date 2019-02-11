package br.ufpr.dinf.gres.opla.view.enumerators;

/**
 * @author Fernando
 */
public enum AlgorithmType {

    NSGA_II("NSGA II"), PAES("PAES");

    private String description;

    private AlgorithmType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

}
