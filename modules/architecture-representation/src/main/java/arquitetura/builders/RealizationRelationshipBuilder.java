package arquitetura.builders;


import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.*;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.RealizationRelationship;
import org.eclipse.uml2.uml.Realization;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class RealizationRelationshipBuilder extends ArchitectureHelper {

    private Architecture architecture;

    public RealizationRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    public RealizationRelationship create(Realization realization) {

        String idClient = getModelHelper().getXmiId(realization.getClients().get(0));
        String idSupplier = getModelHelper().getXmiId(realization.getSuppliers().get(0));

        Element clientElement = architecture.findElementById(idClient);
        Element supplierElement = architecture.findElementById(idSupplier);
        String name = realization.getName() != null ? realization.getName() : "";

        if ((clientElement instanceof Class) && (supplierElement instanceof Interface))
            ((Class) clientElement).addImplementedInterface((Interface) supplierElement);

        if ((clientElement instanceof Package) && (supplierElement instanceof Interface))
            ((Package) clientElement).addImplementedInterface((Interface) supplierElement);

        RealizationRelationship realizationRelationship = new RealizationRelationship(
                clientElement, supplierElement, name, getModelHelper().getXmiId(realization));

        //realizationRelationship.getClient().addRelationship(realizationRelationship);
        //realizationRelationship.getSupplier().addRelationship(realizationRelationship);

        return realizationRelationship;
    }

}