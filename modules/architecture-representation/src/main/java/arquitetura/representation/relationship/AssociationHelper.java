package arquitetura.representation.relationship;

import arquitetura.base.ArchitectureHelper;
import arquitetura.builders.AssociationEndBuilder;
import arquitetura.representation.Architecture;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import java.util.ArrayList;
import java.util.List;

public class AssociationHelper extends ArchitectureHelper {

    private AssociationEndBuilder associationEndBuilder;
    private Architecture architecture;

    public AssociationHelper(AssociationEndBuilder associationEndBuilder, Architecture architecture) {
        this.associationEndBuilder = associationEndBuilder;
        this.architecture = architecture;
    }

    public List<? extends AssociationEnd> getParticipants(Association association) {
        List<AssociationEnd> elementsOfAssociation = new ArrayList<AssociationEnd>();

        for (Property a : association.getMemberEnds()) {
            try {
                String id = getModelHelper().getXmiId(a.getType());
                arquitetura.representation.Element c = architecture.findElementById(id);

                elementsOfAssociation.add(associationEndBuilder.create(a, c));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return elementsOfAssociation;

    }
}
