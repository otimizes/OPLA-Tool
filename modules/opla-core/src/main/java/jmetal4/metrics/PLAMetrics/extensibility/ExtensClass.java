package jmetal4.metrics.PLAMetrics.extensibility;

import arquitetura.representation.Class;


public class ExtensClass {

    private final Class class_;

    public ExtensClass(Class class_) {
        this.class_ = class_;
    }

    public float getValue() {
        float numberOfMethods = class_.getAllMethods().size();
        float numberOfAbstractMethods = class_.getAllAbstractMethods().size();
        if (numberOfMethods == 0) return 0;
        return numberOfAbstractMethods / numberOfMethods;
    }

    @Override
    public String toString() {
        return class_.getName() + ": " + getValue();
    }
}
