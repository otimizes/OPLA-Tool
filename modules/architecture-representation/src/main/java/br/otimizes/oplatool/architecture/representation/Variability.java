package br.otimizes.oplatool.architecture.representation;


import java.util.ArrayList;
import java.util.List;

/**
 * Variability representation
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Variability extends Comment {

    private static final long serialVersionUID = 7041018135043156673L;
    private String minSelection;
    private String maxSelection;
    private String bindingTime;
    private boolean allowsAddingVar;
    private String ownerClass;
    private String idPackageOwner;
    private VariationPoint variationPoint;
    private final List<Variant> variants = new ArrayList<>();
    private String id;
    public String constraint;

    public Variability(String name, String minSelection, String maxSelection, String bindingTime,
                       boolean allowsAddingVar, String ownerClass, String idPackageOwner) {
        super(name, "variability");
        setName(name);
        setMinSelection(minSelection);
        setMaxSelection(maxSelection);
        setAllowsAddingVar(allowsAddingVar);
        setBindingTime(bindingTime);
        setOwner(ownerClass);
        setIdPackageOwner(idPackageOwner);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    private void setOwner(String ownerClass) {
        this.ownerClass = ownerClass;
    }

    public String getIdPackageOwner() {
        return idPackageOwner;
    }

    public void setIdPackageOwner(String idPackageOwner) {
        this.idPackageOwner = idPackageOwner;
    }

    public String getBindingTime() {
        return bindingTime;
    }

    public void addVariant(Variant variant) {
        variants.add(variant);
    }

    private void setBindingTime(String bindingTime) {
        this.bindingTime = bindingTime;
    }

    public String getMinSelection() {
        return minSelection;
    }

    private void setMinSelection(String minSelection) {
        this.minSelection = minSelection;
    }

    public String getMaxSelection() {
        return maxSelection;
    }

    private void setMaxSelection(String maxSelection) {
        this.maxSelection = maxSelection;
    }

    public boolean allowAddingVar() {
        return allowsAddingVar;
    }

    private void setAllowsAddingVar(boolean allowsAddingVar) {
        this.allowsAddingVar = allowsAddingVar;
    }

    public VariationPoint getVariationPoint() {
        return variationPoint;
    }

    public void setVariationPoint(VariationPoint variationPoint) {
        this.variationPoint = variationPoint;
    }

    @Override
    public String toString() {
        return "Variability [name=" + getName() + ", minSelection=" + minSelection
                + ", maxSelection=" + maxSelection + ", allowsAddingVar="
                + allowsAddingVar + ", variationPoint=" + variationPoint
                + ", variants=" + variants + "]";
    }

    public String generateNote(VariationPoint variationPoint) {
        return String.format("%1s%2s", this, variationPoint.toString());
    }


    public List<Variant> getVariants() {
        return variants;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
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
        Variability other = (Variability) obj;
        if (getName() == null) {
            return other.getName() == null;
        } else return getName().equals(other.getName());
    }

    public String getOwnerClass() {
        return ownerClass;
    }
}