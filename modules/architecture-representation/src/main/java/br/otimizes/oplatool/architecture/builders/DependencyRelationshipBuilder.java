package br.otimizes.oplatool.architecture.builders;


import br.otimizes.oplatool.architecture.base.ArchitectureHelper;
import br.otimizes.oplatool.architecture.helpers.StereotypeHelper;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Dependency relationship builder
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class DependencyRelationshipBuilder extends ArchitectureHelper {

    private final Architecture architecture;

    public DependencyRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    /**
     * Creates the dependency relationship
     *
     * @param element single dependency
     * @return dependency relationship
     */
    public DependencyRelationship create(Dependency element) {
        EList<NamedElement> suppliers = element.getSuppliers();
        EList<NamedElement> clients = element.getClients();

        Element client = architecture.findElementById(getModelHelper().getXmiId(clients.get(0)));
        Element supplier = architecture.findElementById(getModelHelper().getXmiId(suppliers.get(0)));

        if ((client instanceof Class) && (supplier instanceof Interface))
            ((Class) client).addRequiredInterface((Interface) supplier);

        if ((client instanceof Package) && (supplier instanceof Interface))
            ((Package) client).addRequiredInterface((Interface) supplier);

        DependencyRelationship dependency = new DependencyRelationship(supplier, client, element.getName(), getModelHelper().getXmiId(element));
        dependency.setStereotypes(StereotypeHelper.getAllRelationshipStereotypes(element));

        return dependency;
    }

}

