package br.otimizes.oplatool.architecture.builders;


import br.otimizes.oplatool.architecture.representation.relationship.UsageRelationship;
import br.otimizes.oplatool.architecture.base.ArchitectureHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;

/**
 * Usage relationship builder
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class UsageRelationshipBuilder extends ArchitectureHelper {

    private Architecture architecture;

    public UsageRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    /**
     * Creates the usage relationship
     *
     * @param element usage
     * @return usage relationship
     */
    public UsageRelationship create(Usage element) {

        EList<NamedElement> suppliers = element.getSuppliers();
        EList<NamedElement> clieents = element.getClients();

        Element client = architecture.findElementById(getModelHelper().getXmiId(clieents.get(0)));
        Element supplier = architecture.findElementById(getModelHelper().getXmiId(suppliers.get(0)));
        UsageRelationship usageRelationship = new UsageRelationship(element.getName(), supplier, client, getModelHelper().getXmiId(element));

//		usageRelationship.getClient().addRelationship(usageRelationship);
//		usageRelationship.getSupplier().addRelationship(usageRelationship);

        return usageRelationship;
    }

}
