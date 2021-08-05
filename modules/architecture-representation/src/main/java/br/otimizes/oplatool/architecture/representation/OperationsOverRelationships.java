package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Operations over relationships class
 */
public class OperationsOverRelationships {

    static Logger LOGGER = LogManager.getLogger(OperationsOverRelationships.class.getName());

    private final Architecture architecture;

    public OperationsOverRelationships(Architecture architecture) {
        this.architecture = architecture;
    }

    public void moveAssociation(AssociationRelationship association, Class firstClass, Class secondClass) {
        association.getParticipants().get(0).setCLSClass(firstClass);
        association.getParticipants().get(1).setCLSClass(secondClass);
    }

    public void moveAssociationClass(AssociationClassRelationship association, Class firstMember, Class secondMember) {
        association.getMembersEnd().clear();
        association.getMembersEnd().add(new MemberEnd("none", null, "public", firstMember));
        association.getMembersEnd().add(new MemberEnd("none", null, "public", secondMember));
    }

    public void moveDependency(DependencyRelationship dependency, Class client, Class supplier) {
        dependency.setClient(client);
        dependency.setSupplier(supplier);
    }

    public void removeAssociationRelationship(AssociationRelationship associationRelationship) {
        if (!removeRelationship(associationRelationship))
            LOGGER.info("Cannot remove Association " + associationRelationship + ".\n");
    }

    public void removeDependencyRelationship(DependencyRelationship dependencyRelationship) {
        if (!removeRelationship(dependencyRelationship))
            LOGGER.info("Cannot remove Dependency " + dependencyRelationship + ".\n");
    }

    public void removeAssociationClass(AssociationClassRelationship associationClass) {
        if (!removeRelationship(associationClass))
            LOGGER.info("Cannot remove AssociationClass " + associationClass + ".\n");
    }

    public void removeGeneralizationRelationship(GeneralizationRelationship generalization) {
        if (!removeRelationship(generalization))
            LOGGER.info("Cannot remove Generalization " + generalization + ".\n");
    }

    private boolean removeRelationship(Relationship as) {
        if (as == null) return false;
        return this.architecture.removeRelationship(as);
    }

    public void moveAssociationEnd(AssociationEnd associationEnd, Class classId) {
        associationEnd.setCLSClass(classId);
    }

    public void moveDependencyClient(DependencyRelationship dependency, Class newClient) {
        dependency.setClient(newClient);
    }

    public void moveDependencySupplier(DependencyRelationship dependency, Class newSupplier) {
        dependency.setSupplier(newSupplier);
    }

    public void moveMemberEndOf(MemberEnd memberEnd, Class klass) {
        memberEnd.setType(klass);
    }

    public void moveRealizationClient(RealizationRelationship realization, Element newClient) {
        realization.setClient(newClient);
    }

    public void moveRealizationSupplier(RealizationRelationship realization, Element newSupplier) {
        realization.setSupplier(newSupplier);
    }

    public void moveRealization(RealizationRelationship realization, Element client, Element supplier) {
        realization.setClient(client);
        realization.setSupplier(supplier);
    }

    public void createNewRealization(Element client, Element supplier) {
        String id = UtilResources.getRandomUUID();
        RealizationRelationship realization = new RealizationRelationship(client, supplier, "", id);
        this.architecture.addRelationship(realization);
    }
}
