package br.otimizes.oplatool.architecture.base;

import br.otimizes.oplatool.architecture.builders.AssociationEndBuilder;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;
import br.otimizes.oplatool.architecture.representation.Architecture;
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
                Element c = architecture.findElementById(id);

                elementsOfAssociation.add(associationEndBuilder.create(a, c));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return elementsOfAssociation;

    }
}
