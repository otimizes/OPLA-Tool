package br.otimizes.oplatool.architecture.builders;


import br.otimizes.oplatool.architecture.representation.relationship.AbstractionRelationship;
import br.otimizes.oplatool.architecture.base.ArchitectureHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Builder Responsible for creating relationships between Package and Class.
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AbstractionRelationshipBuilder extends ArchitectureHelper {

    private Architecture architecture;

    public AbstractionRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    /**
     * Create a AbstractionInterElementRelationship element
     *
     * @param modelElement model element
     * @return abstract relationship
     */
    public AbstractionRelationship create(Abstraction modelElement) {

        NamedElement clientElement = modelElement.getClients().get(0);
        NamedElement supplierElement = modelElement.getSuppliers().get(0);

        Element client = architecture.findElementById(getModelHelper().getXmiId(clientElement));
        Element supplier = architecture.findElementById(getModelHelper().getXmiId(supplierElement));

        AbstractionRelationship abs = new AbstractionRelationship(client, supplier, getModelHelper().getXmiId(modelElement));
//		abs.getClient().addRelationship(abs);
        //	abs.getSupplier().addRelationship(abs);
        return abs;


    }

}
