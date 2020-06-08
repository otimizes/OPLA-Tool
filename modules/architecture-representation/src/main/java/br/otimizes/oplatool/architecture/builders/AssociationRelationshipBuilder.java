package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.base.ArchitectureHelper;
import br.otimizes.oplatool.architecture.base.AssociationHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import org.eclipse.uml2.uml.Association;

/**
 * Create association relationship builder
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AssociationRelationshipBuilder extends ArchitectureHelper {

    private final AssociationEndBuilder associationEndBuilder;
    private AssociationHelper associationHelper;


    public AssociationRelationshipBuilder(Architecture architecture) {
        associationEndBuilder = new AssociationEndBuilder();
        associationHelper = new AssociationHelper(associationEndBuilder, architecture);
    }

    public AssociationRelationship create(Association association) {

        AssociationRelationship associationRelationship = new AssociationRelationship(getModelHelper().getXmiId(association));
        associationRelationship.getParticipants().addAll(associationHelper.getParticipants(association));
        associationRelationship.setType("association");
        associationRelationship.setName(association.getName());

//		for(AssociationEnd associationEnd : associationRelationship.getParticipants()){
//			associationEnd.getCLSClass().addRelationship(associationRelationship);
//		}

        return associationRelationship;
    }

}