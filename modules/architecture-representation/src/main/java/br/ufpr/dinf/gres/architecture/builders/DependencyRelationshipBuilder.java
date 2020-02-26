package br.ufpr.dinf.gres.architecture.builders;


import br.ufpr.dinf.gres.architecture.base.ArchitectureHelper;
import br.ufpr.dinf.gres.architecture.helpers.StereotypeHelper;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.relationship.DependencyRelationship;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class DependencyRelationshipBuilder extends ArchitectureHelper {

    private Architecture architecture;

    public DependencyRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    public DependencyRelationship create(Dependency element) {


        EList<NamedElement> suppliers = element.getSuppliers();
        EList<NamedElement> clieents = element.getClients();

        Element client = architecture.findElementById(getModelHelper().getXmiId(clieents.get(0)));
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

