package arquitetura.representation;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Attribute extends Element {

    private static final long serialVersionUID = 8852796737816675507L;

    private String type;
    private String visibilityKind;
    private boolean generatVisualAttribute;

    /**
     * @param architecture
     * @param name
     * @param isVariationPoint
     * @param variantType
     * @param type
     * @param parent
     * @param namesapce
     * @param id
     */
    public Attribute(String name, String visibilityKind, Variant variantType, String type, String namesapce, String id, boolean shouldGenerateVisualAttribute) {
        super(name, variantType, "attribute", namesapce, id);
        setType(type);
        setVisibilityKind(visibilityKind);
        setGeneratVisualAttribute(shouldGenerateVisualAttribute);
    }

    /**
     * @param architecture
     * @param name
     * @param type
     * @param parent
     * @param namespace
     * @param id
     */
    public Attribute(String name, String visibilityKind, String type, String namespace, String id) {
        this(name, visibilityKind, null, type, namespace, id, true);
    }

    private void setVisibilityKind(String visibilityKind) {
        this.visibilityKind = visibilityKind;
    }


    /**
     * @return the generatVisualAttribute
     */
    public boolean isGeneratVisualAttribute() {
        return generatVisualAttribute;
    }

    private void setGeneratVisualAttribute(boolean shouldGenerateVisualAttribute) {
        this.generatVisualAttribute = shouldGenerateVisualAttribute;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public String getVisibility() {
        return this.visibilityKind;

    }

    @Override
    public Collection<Concern> getAllConcerns() {
        return new ArrayList<Concern>(getOwnConcerns());
    }
}