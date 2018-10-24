package arquitetura.representation;

import arquitetura.touml.Stereotype;

import java.util.ArrayList;
import java.util.List;


/**
 * Essa classe representa uma variant, contem os atributos referentes
 * as variants:<br/>
 * <ul>
 * <li>mandatory</li>
 * <li>optional</li>
 * <li>alternative_OR</li>
 * </li>alternative_XOR</li>
 * </ul>
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Variant implements Stereotype {

    private Element variantElement;
    private String name;
    private String rootVP;
    private String variantType;
    private List<Variability> variabilities = new ArrayList<Variability>();
    private List<VariationPoint> variationPoints = new ArrayList<VariationPoint>();

    public static Variant createVariant() {
        return new Variant();
    }

    /**
     * Retorna o nome da classe que tem a variant em questão.
     *
     * @return the variantType
     */
    public String getVariantName() {
        return name;
    }

    /**
     * @param variantType the variantType to set
     */
    private void setVariantName(String name) {
        this.name = name;
    }

    /**
     * @return the rootVP
     */
    public String getRootVP() {
        return this.rootVP;
    }

    /**
     * @param rootVP the rootVP to set
     */
    public void setRootVP(String rootVP) {
        this.rootVP = rootVP;
    }

    public Variant withName(String name) {
        setVariantName(name);
        return this;
    }

    /**
     * rootVP, representa o ponto de variação ao qual está associado .
     * <p>
     * Deve ser o nome da classe/interface que representa o rootVP
     *
     * @param rootVP
     * @return
     */
    public Variant andRootVp(String rootVP) {
        setRootVP(rootVP);
        return this;
    }


    public Variant build() {
        return this;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the variabilities
     */
    public List<Variability> getVariabilities() {
        return variabilities;
    }

    /**
     * @param variabilities the variabilities to set
     */
    public void setVariabilities(List<Variability> variabilities) {
        this.variabilities = variabilities;
    }

    public Variant withVariantType(String name) {
        this.variantType = name;
        return this;
    }

    /**
     * @return the variantType
     */
    public String getVariantType() {
        return variantType;
    }

    /**
     * @param variantType the variantType to set
     */
    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    /**
     * @return the variantElement
     */
    public Element getVariantElement() {
        return variantElement;
    }

    /**
     * @param variantElement the variantElement to set
     */
    public void setVariantElement(Element variantElement) {
        this.variantElement = variantElement;
    }

    /**
     * @return the variationPoints
     */
    public List<VariationPoint> getVariationPoints() {
        return variationPoints;
    }

    /**
     * @param variationPoints the variationPoints to set
     */
    public void setVariationPoints(List<VariationPoint> variationPoints) {
        this.variationPoints = variationPoints;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public Variant wihtVariabilities(List<Variability> variabilities2) {
        this.variabilities = variabilities2;
        return this;
    }


}