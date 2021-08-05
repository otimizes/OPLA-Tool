package br.otimizes.oplatool.architecture.representation;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Attribute representation class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Attribute extends Element {

    private static final long serialVersionUID = 8852796737816675507L;

    private String type;
    private String visibilityKind;
    private boolean generateVisualAttribute;

    public Attribute(String name, String visibilityKind, Variant variantType, String type, String namespace,
                     String id, boolean shouldGenerateVisualAttribute) {
        super(name, variantType, "attribute", namespace, id);
        setType(type);
        setVisibilityKind(visibilityKind);
        setGenerateVisualAttribute(shouldGenerateVisualAttribute);
    }

    public Attribute(String name, String visibilityKind, String type, String namespace, String id) {
        this(name, visibilityKind, null, type, namespace, id, true);
    }

    private void setVisibilityKind(String visibilityKind) {
        this.visibilityKind = visibilityKind;
    }

    public boolean isGenerateVisualAttribute() {
        return generateVisualAttribute;
    }

    private void setGenerateVisualAttribute(boolean shouldGenerateVisualAttribute) {
        this.generateVisualAttribute = shouldGenerateVisualAttribute;
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