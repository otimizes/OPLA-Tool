package br.ufpr.dinf.gres.architecture.base;

import br.ufpr.dinf.gres.architecture.builders.AssociationEndBuilder;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.relationship.AssociationEnd;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contain assist association methods
 */
public class AssociationHelper extends ArchitectureHelper {

    private AssociationEndBuilder associationEndBuilder;
    private Architecture architecture;

    public AssociationHelper(AssociationEndBuilder associationEndBuilder, Architecture architecture) {
        this.associationEndBuilder = associationEndBuilder;
        this.architecture = architecture;
    }

    /**
     * Get association ends from association
     *
     * @param association association
     * @return association ends
     */
    public List<? extends AssociationEnd> getParticipants(Association association) {
        List<AssociationEnd> elementsOfAssociation = new ArrayList<AssociationEnd>();

        for (Property a : association.getMemberEnds()) {
            try {
                String id = getModelHelper().getXmiId(a.getType());
                br.ufpr.dinf.gres.architecture.representation.Element c = architecture.findElementById(id);

                elementsOfAssociation.add(associationEndBuilder.create(a, c));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return elementsOfAssociation;

    }
}
