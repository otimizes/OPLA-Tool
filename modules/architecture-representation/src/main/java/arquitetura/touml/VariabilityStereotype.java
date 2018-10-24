package arquitetura.touml;

import arquitetura.helpers.Strings;
import arquitetura.representation.Variability;


/**
 * Classe usada para definir o estereótipo Variability para ser setado em uma note.(Comment)
 *
 * @author edipofederle
 */
public class VariabilityStereotype implements Stereotype {

    private String stereotypeName = "variability";
    private String name;
    private String minSelection;
    private String maxSelection;
    private boolean allowAddingVar;
    private String bindingTime;
    private String variants; // Nomes separados por virgulas(,)
    private String idPackageOwner;

    public VariabilityStereotype(Variability variabilityStereotype) {
        setName(variabilityStereotype.getName());
        setMinSelection(variabilityStereotype.getMinSelection());
        setMaxSelection(variabilityStereotype.getMaxSelection());
        setAllowAddingVar(variabilityStereotype.allowAddingVar());
        setBindingTime(variabilityStereotype.getBindingTime());
        setVariants(Strings.spliterVariants(variabilityStereotype.getVariants()));
        setidForPackageOwner(variabilityStereotype.getIdPackageOwner());
    }

    private void setidForPackageOwner(String idPackageOwner) {
        this.idPackageOwner = idPackageOwner;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    /**
     * @return the minSelection
     */
    public String getMinSelection() {
        return minSelection;
    }

    private void setMinSelection(String minSelection) {
        this.minSelection = minSelection;
    }

    /**
     * @return the maxSelection
     */
    public String getMaxSelection() {
        return maxSelection;
    }

    private void setMaxSelection(String maxSelecion) {
        this.maxSelection = maxSelecion;
    }

    /**
     * @return the allowAddingVar
     */
    public boolean isAllowAddingVar() {
        return allowAddingVar;
    }

    private void setAllowAddingVar(boolean allowAddingVar) {
        this.allowAddingVar = allowAddingVar;
    }

    /**
     * @return the bindingTime
     */
    public String getBindingTime() {
        return bindingTime;
    }

    private void setBindingTime(String bindingTime) {
        this.bindingTime = bindingTime;
    }

    /**
     * @return the idPackageOwner
     */
    public String getIdPackageOwner() {
        return idPackageOwner;
    }

    /**
     * @return the variants
     */
    public String getVariants() {
        return variants;
    }

    /**
     * @param variants the variants to set
     */
    public void setVariants(String variants) {
        this.variants = variants;
    }

    /**
     * @return the stereotypeName
     */
    public String getStereotypeName() {
        return stereotypeName;
    }

    /**
     * @param stereotypeName the stereotypeName to set
     */
    public void setStereotypeName(String stereotypeName) {
        this.stereotypeName = stereotypeName;
    }

}