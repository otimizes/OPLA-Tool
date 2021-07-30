package br.otimizes.oplatool.architecture.builders;


import br.otimizes.oplatool.architecture.base.ArchitectureHelper;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import org.eclipse.uml2.uml.Realization;

/**
 * Realization relationship builder
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class RealizationRelationshipBuilder extends ArchitectureHelper {

    private final Architecture architecture;

    public RealizationRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    /**
     * Creates the realization relationship
     *
     * @param realization realization
     * @return realization relationship
     */
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

        return new RealizationRelationship(
                clientElement, supplierElement, name, getModelHelper().getXmiId(realization));
    }
}