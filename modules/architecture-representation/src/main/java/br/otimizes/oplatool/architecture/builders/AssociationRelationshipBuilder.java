package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.base.ArchitectureHelper;
import br.otimizes.oplatool.architecture.base.AssociationHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import org.eclipse.uml2.uml.Association;

/**
 * Create association relationship builder
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AssociationRelationshipBuilder extends ArchitectureHelper {

    private final AssociationHelper associationHelper;

    public AssociationRelationshipBuilder(Architecture architecture) {
        AssociationEndBuilder associationEndBuilder = new AssociationEndBuilder();
        associationHelper = new AssociationHelper(associationEndBuilder, architecture);
    }

    public AssociationRelationship create(Association association) {
        AssociationRelationship associationRelationship = new AssociationRelationship(getModelHelper().getXmiId(association));
        associationRelationship.getParticipants().addAll(associationHelper.getParticipants(association));
        associationRelationship.setType("association");
        associationRelationship.setName(association.getName());
        return associationRelationship;
    }

}