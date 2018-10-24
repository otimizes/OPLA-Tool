package arquitetura.builders;


import arquitetura.base.ArchitectureHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;
import org.eclipse.uml2.uml.Generalization;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class GeneralizationRelationshipBuilder extends ArchitectureHelper {

    private Architecture architecture;

    public GeneralizationRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    public Relationship create(Generalization generalization) {
        String generalKlassId = getModelHelper().getXmiId(generalization.getGeneral());
        String specificKlassId = getModelHelper().getXmiId(generalization.getSpecific());

        arquitetura.representation.Element general = architecture.findElementById(generalKlassId);
        arquitetura.representation.Element specific = architecture.findElementById(specificKlassId);
        GeneralizationRelationship generalizationRelation = new GeneralizationRelationship(general, specific, architecture.getRelationshipHolder(), getModelHelper().getXmiId(generalization));
        general.setBelongsToGeneralization(true);
        specific.setBelongsToGeneralization(true);
        //generalizationRelation.getParent().addRelationship(generalizationRelation);
        //generalizationRelation.getChild().addRelationship(generalizationRelation);
        return generalizationRelation;
    }

}
