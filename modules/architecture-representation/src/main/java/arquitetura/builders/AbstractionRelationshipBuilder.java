package arquitetura.builders;


import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.AbstractionRelationship;
import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Builder Responsável por criar relacionamentos entre Pacote e Classe.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class AbstractionRelationshipBuilder extends ArchitectureHelper {

    private Architecture architecture;

    public AbstractionRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    /**
     * Cria o elemento AbstractionInterElementRelationship
     *
     * @param modelElement
     * @return
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
