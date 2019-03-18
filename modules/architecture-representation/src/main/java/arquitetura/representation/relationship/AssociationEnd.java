package arquitetura.representation.relationship;

import arquitetura.representation.Class;
import arquitetura.representation.Element;

import java.io.Serializable;


/**
 * An association end specifies the role that the object at one end of a relationship performs.
 * Each end of a relationship has properties that specify the role of the association end,
 * its multiplicity, visibility, navigability, and constraints.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class AssociationEnd implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1246465575711005213L;
    private Element klass;
    private boolean isNavigable = false;
    //TODO Tipo da associação mudar nome da variavel
    private String aggregation;
    private Multiplicity multiplicity;
    private String name;

    public AssociationEnd(Element klass, boolean isNavigable, String aggregation, Multiplicity multiplicity, String name) {
        setCLSClass(klass);
        setNavigable(isNavigable);
        setAggregation(aggregation);
        setMultiplicity(multiplicity);
        setName(name);
    }

    public AssociationEnd() {
    }

    public Element getCLSClass() {
        return klass;
    }

    public void setCLSClass(Element c) {
        this.klass = c;
    }

    public boolean isNavigable() {
        return isNavigable;
    }

    public void setNavigable(boolean isNavigable) {
        this.isNavigable = isNavigable;
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
     * Retorna o tipo da associação.<br />
     * Tipos:
     * <p>
     * <ul>
     * <li>Composiçãao - Composite</li>
     * <li>Agregação - Shared</li>
     * </ul>
     *
     * @return
     */
    public String getAggregation() {
        if ("shared".equalsIgnoreCase(aggregation))
            return "shared";
        else if ("composite".equalsIgnoreCase(aggregation))
            return "composite";
        else
            return "none";

    }

    public void replaceCLSClass(Class c) {
        setCLSClass(c);
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(Multiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }

    public boolean isComposite() {
        return getAggregation().equals("composite");
    }

    public boolean isAggregation() {
        return getAggregation().equals("shared");
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((klass == null) ? 0 : klass.hashCode());
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
        AssociationEnd other = (AssociationEnd) obj;
        if (klass == null) {
            if (other.klass != null)
                return false;
        } else if (!klass.equals(other.klass))
            return false;
        return true;
    }

}
