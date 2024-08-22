package br.otimizes.oplatool.core.jmetal4.core;

/**
 * Architectural element type used in machine learning
 */
public enum ArchitecturalElementType {
    PACKAGE, KLASS, INTERFACE, ATTRIBUTE, METHOD;

    public static double getTypeId(String value) {
        switch (value.toUpperCase()) {
            case "PACKAGE":
                return 1.0;
            case "KLASS":
                return 0.8;
            case "INTERFACE":
                return 0.6;
            case "ATTRIBUTE":
                return 0.4;
            case "METHOD":
                return 0.2;
        }
        return 0;
    }

    public static ArchitecturalElementType getTypeById(double id) {
        if (id == 1.0) {
            return ArchitecturalElementType.PACKAGE;
        } else if (id == 0.8) {
            return ArchitecturalElementType.KLASS;
        } else if (id == 0.6) {
            return ArchitecturalElementType.INTERFACE;
        } else if (id == 0.4) {
            return ArchitecturalElementType.ATTRIBUTE;
        } else if (id == 0.2) {
            return ArchitecturalElementType.METHOD;
        }
        return null;
    }
}
