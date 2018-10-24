package arquitetura.builders;


import arquitetura.representation.Element;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.Multiplicity;
import org.eclipse.uml2.uml.Property;

/**
 * Representa uma AssociationEnd em uma associação.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class AssociationEndBuilder {

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