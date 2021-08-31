package br.otimizes.oplatool.architecture.representation;


/**
 * Variants type
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public enum VariantType {

    MANDATORY("mandatory"),
    OPTIONAL("optional"),
    ALTERNATIVE_OR("alternative_OR"),
    ALTERNATIVE_XOR("alternative_XOR"),
    VARIATION_POINT("variationPoint");

    private final String variantName;

    VariantType(String variantName) {
        this.variantName = variantName;
    }

    public static VariantType getByName(String name) {
        for (VariantType variantType : VariantType.values()) {
            if (variantType.toString().equalsIgnoreCase(name))
                return variantType;
        }
        return null;
    }

    @Override
    public String toString() {
        return variantName;
    }
}