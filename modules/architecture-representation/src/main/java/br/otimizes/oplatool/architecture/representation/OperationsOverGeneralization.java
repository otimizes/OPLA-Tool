package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;

/**
 * Operations over generalization class
 */
public class OperationsOverGeneralization {

    private final Architecture architecture;

    public OperationsOverGeneralization(Architecture architecture) {
        this.architecture = architecture;
    }

    public void moveGeneralizationParent(GeneralizationRelationship gene, Class targetClass) {
        gene.setParent(targetClass);
    }

    public void moveGeneralizationSubClass(GeneralizationRelationship generalizationRelationship, Class subClass) {
        generalizationRelationship.setChild(subClass);
    }

    public void moveGeneralization(GeneralizationRelationship generalizationRelationship, Class parent, Class child) {
        generalizationRelationship.setParent(parent);
        generalizationRelationship.setChild(child);
    }

    public void addChildToGeneralization(GeneralizationRelationship generalizationRelationship, Element newChild) {
        GeneralizationRelationship g = new GeneralizationRelationship(generalizationRelationship.getParent(), newChild,
                this.architecture.getRelationshipHolder(), UtilResources.getRandomUUID());
        this.architecture.addRelationship(g);
    }

    public GeneralizationRelationship createGeneralization(Element parent, Element child) {
        GeneralizationRelationship generalizationRelationship = new GeneralizationRelationship(parent, child,
                this.architecture.getRelationshipHolder(), UtilResources.getRandomUUID());
        this.architecture.addRelationship(generalizationRelationship);
        return generalizationRelationship;
    }

    public void moveGeneralizationToPackage(GeneralizationRelationship generalization, Package targetPackage) {
        architecture.moveElementToPackage(generalization.getParent(), targetPackage);
        for (Element element : generalization.getAllChildrenForGeneralClass())
            architecture.moveElementToPackage(element, targetPackage);
    }
}