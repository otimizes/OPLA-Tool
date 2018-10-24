package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class OperationsOverRelationships {

    static Logger LOGGER = LogManager.getLogger(OperationsOverRelationships.class.getName());

    private Architecture architecture;

    public OperationsOverRelationships(Architecture architecture) {
        this.architecture = architecture;
    }

    public void moveAssociation(AssociationRelationship association, Class class1, Class class2) {
        association.getParticipants().get(0).setCLSClass(class1);
        association.getParticipants().get(1).setCLSClass(class2);
    }

    public void moveAssociationClass(AssociationClassRelationship association, Class member1, Class member2) {
        association.getMemebersEnd().clear();
        association.getMemebersEnd().add(new MemberEnd("none", null, "public", member1));
        association.getMemebersEnd().add(new MemberEnd("none", null, "public", member2));
    }

    public void moveDependency(DependencyRelationship dependency, Class client, Class supplier) {
        dependency.setClient(client);
        dependency.setSupplier(supplier);
    }


    public void removeAssociationRelationship(AssociationRelationship as) {
        if (!removeRelationship(as))
            LOGGER.info("Cannot remove Association " + as + ".\n");
    }

    public void removeDependencyRelationship(DependencyRelationship dp) {
        if (!removeRelationship(dp))
            LOGGER.info("Cannot remove Dependency " + dp + ".\n");
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

    public void moveAssociationEnd(AssociationEnd associationEnd, Class idclass8) {
        associationEnd.setCLSClass(idclass8);
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

    /**
     * Move o client de uma {@link RealizationRelationship} para outro elemento (Classe ou Package).
     *
     * @param realization
     * @param newClient
     */
    public void moveRealizationClient(RealizationRelationship realization, Element newClient) {
        realization.setClient(newClient);
    }

    /**
     * Move o supplier de uma {@link RealizationRelationship} para outro elemento (Classe ou Package).
     *
     * @param realization
     * @param newSupplier
     */
    public void moveRealizationSupplier(RealizationRelationship realization, Element newSupplier) {
        realization.setSupplier(newSupplier);
    }

    /**
     * Move uma realizacão inteira.
     *
     * @param realization - Realização a ser movida
     * @param client      - Novo Cliente
     * @param supplier    - Novo Supplier
     */
    public void moveRealization(RealizationRelationship realization, Element client, Element supplier) {
        realization.setClient(client);
        realization.setSupplier(supplier);
    }

    public void createNewRealization(Element client, Element supplier) {
        String id = UtilResources.getRandonUUID();
        RealizationRelationship realization = new RealizationRelationship(client, supplier, "", id);
        this.architecture.addRelationship(realization);
    }

}
