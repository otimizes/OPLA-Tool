package br.otimizes.oplatool.architecture.builders;


import br.otimizes.oplatool.architecture.base.ArchitectureHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import org.eclipse.uml2.uml.Generalization;

/**
 * Generalization relationship builder
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class GeneralizationRelationshipBuilder extends ArchitectureHelper {

    private final Architecture architecture;

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
        Element general = architecture.findElementById(generalKlassId);
        Element specific = architecture.findElementById(specificKlassId);
        GeneralizationRelationship generalizationRelation = new GeneralizationRelationship(general, specific, architecture.getRelationshipHolder(), getModelHelper().getXmiId(generalization));
        general.setBelongsToGeneralization(true);
        specific.setBelongsToGeneralization(true);
        return generalizationRelation;
    }

}
