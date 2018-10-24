package jmetal4.experiments;

public enum Metrics {

    CONVENTIONAL("conventional"),
    ELEGANCE("elegance"),
    FEATURE_DRIVEN("featureDriven"),
    PLA_EXTENSIBILIY("PLAExtensibility"),
    ACOMP("acomp"),
    ACLASS("aclass"),
    TAM("tam"),
    COE("coe"),
    EC("ec"),
    DC("dc");


    private String name;

    private Metrics(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
