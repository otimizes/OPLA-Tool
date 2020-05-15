package br.ufpr.dinf.gres.architecture.builders;


import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.AssociationEnd;
import br.ufpr.dinf.gres.architecture.representation.relationship.Multiplicity;
import org.eclipse.uml2.uml.Property;

/**
 * Represents an AssociationEnd in a association
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AssociationEndBuilder {

    /**
     * Creates the association end
     *
     * @param property property
     * @param klass    class
     * @return association end
     */
    public AssociationEnd create(Property property, Element klass) {
        boolean isNavigable = property.isNavigable();
        String aggregation = property.getAggregation().getName();

        //TODO refatorar duas linhas abaixo
        String upperValue = property.getUpperValue() == null ? "" : property.getUpperValue().stringValue();
        Multiplicity multiplicity = new Multiplicity(property.getLowerValue().stringValue(), upperValue);
        //System.out.println("parar");
        String nameAssociationEnd = property.getName();
        return new AssociationEnd(klass, isNavigable, aggregation, multiplicity, nameAssociationEnd);
    }

}