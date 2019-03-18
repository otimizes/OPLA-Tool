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
    DC("dc"),
    // addYni
    WOCSCLASS("wocsclass"),
    WOCSINTER("wocsinterface"),
    CBCS("cbcs"),
    SVC("svc"),
    SSC("ssc"),
    AV("av"),
    // addYni
    LCC("lcc");


    private String name;

    private Metrics(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
