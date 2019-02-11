package arquitetura.representation;


/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public enum VariantType {

    MANDATORY("mandatory"),
    OPTIONAL("optional"),
    ALTERNATIVE_OR("alternative_OR"),
    ALTERNATIVE_XOR("alternative_XOR"),
    VARIATIONPOINT("variationPoint");

    private final String variantName;

    VariantType(String variantName) {
        this.variantName = variantName;
    }

    public static VariantType getByName(String name) {
        for (VariantType e : VariantType.values()) {
            if (e.toString().equalsIgnoreCase(name))
                return e;
        }
        return null;
    }

    @Override
    public String toString() {
        return variantName;
    }
}