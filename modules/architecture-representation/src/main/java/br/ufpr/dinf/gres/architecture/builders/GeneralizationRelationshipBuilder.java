package br.ufpr.dinf.gres.architecture.builders;


import br.ufpr.dinf.gres.architecture.base.ArchitectureHelper;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.relationship.GeneralizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import org.eclipse.uml2.uml.Generalization;

/**
 * Generalization relationship builder
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class GeneralizationRelationshipBuilder extends ArchitectureHelper {

    private Architecture architecture;

    public GeneralizationRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    /**
     * Creates the relationship by generalization
     *
     * @param generalization generalization
     * @return relationship
     */
    public Relationship create(Generalization generalization) {
        String generalKlassId = getModelHelper().getXmiId(generalization.getGeneral());
        String specificKlassId = getModelHelper().getXmiId(generalization.getSpecific());

        br.ufpr.dinf.gres.architecture.representation.Element general = architecture.findElementById(generalKlassId);
        br.ufpr.dinf.gres.architecture.representation.Element specific = architecture.findElementById(specificKlassId);
        GeneralizationRelationship generalizationRelation = new GeneralizationRelationship(general, specific, architecture.getRelationshipHolder(), getModelHelper().getXmiId(generalization));
        general.setBelongsToGeneralization(true);
        specific.setBelongsToGeneralization(true);
        //generalizationRelation.getParent().addRelationship(generalizationRelation);
        //generalizationRelation.getChild().addRelationship(generalizationRelation);
        return generalizationRelation;
    }

}
