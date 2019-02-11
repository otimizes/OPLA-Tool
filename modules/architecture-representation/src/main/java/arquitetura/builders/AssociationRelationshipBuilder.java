package arquitetura.builders;

import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.AssociationHelper;
import arquitetura.representation.relationship.AssociationRelationship;
import org.eclipse.uml2.uml.Association;

/**
 * @author edipofederle<edipofederle@gmail.com>
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