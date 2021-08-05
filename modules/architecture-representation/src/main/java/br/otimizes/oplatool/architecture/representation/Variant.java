package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.papyrus.touml.Stereotype;

import java.util.ArrayList;
import java.util.List;


/**
 * Variant representation, can be:<br/>
 * <ul>
 * <li>mandatory</li>
 * <li>optional</li>
 * <li>alternative_OR</li>
 * </li>alternative_XOR</li>
 * </ul>
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Variant implements Stereotype {

    private Element variantElement;
    private String name;
    private String rootVP;
    private String variantType;
    private List<Variability> variabilities = new ArrayList<>();
    private List<VariationPoint> variationPoints = new ArrayList<>();

    public static Variant createVariant() {
        return new Variant();
    }

    public String getVariantName() {
        return name;
    }

    private void setVariantName(String name) {
        this.name = name;
    }

    public String getRootVP() {
        return this.rootVP;
    }

    public void setRootVP(String rootVP) {
        this.rootVP = rootVP;
    }

    public Variant withName(String name) {
        setVariantName(name);
        return this;
    }

    public Variant andRootVp(String rootVP) {
        setRootVP(rootVP);
        return this;
    }

    public Variant build() {
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Variability> getVariabilities() {
        return variabilities;
    }

    public void addVariability(Variability variability) {
        variabilities.add(variability);
    }

    public void setVariabilities(List<Variability> variabilities) {
        this.variabilities = variabilities;
    }

    public Variant withVariantType(String name) {
        this.variantType = name;
        return this;
    }

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public Element getVariantElement() {
        return variantElement;
    }

    public void setVariantElement(Element variantElement) {
        this.variantElement = variantElement;
    }

    public List<VariationPoint> getVariationPoints() {
        return variationPoints;
    }

    public void addVariationPoint(VariationPoint vp) {
        variationPoints.add(vp);
    }

    public void setVariationPoints(List<VariationPoint> variationPoints) {
        this.variationPoints = variationPoints;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Variant other = (Variant) obj;
        if (name == null) {
            return other.name == null;
        } else return name.equals(other.name);
    }

    public Variant withVariabilities(List<Variability> variabilities) {
        this.variabilities = variabilities;
        return this;
    }
}