package br.otimizes.oplatool.patterns.util;

import java.util.UUID;

import br.otimizes.oplatool.patterns.repositories.ArchitectureRepository;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.representation.relationship.UsageRelationship;

/**
 * The Class RelationshipUtil.
 */
public class RelationshipUtil {

    /**
     * Instantiates a new relationship util.
     */
    private RelationshipUtil() {
    }

    /**
     * Gets the used element from relationship.
     *
     * @param relationship the relationship
     * @return the used element from relationship
     */
    public static Element getUsedElementFromRelationship(Relationship relationship) {
        Element supplier = null;
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;
            supplier = usage.getSupplier();
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;
            supplier = dependency.getSupplier();
        }
        return supplier;
    }

    /**
     * Gets the client element from relationship.
     *
     * @param relationship the relationship
     * @return the client element from relationship
     */
    public static Element getClientElementFromRelationship(Relationship relationship) {
        Element client = null;
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;
            client = usage.getClient();
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;
            client = dependency.getClient();
        }
        return client;
    }

    /**
     * Gets the implemented interface.
     *
     * @param relationship the relationship
     * @return the implemented interface
     */
    public static Interface getImplementedInterface(Relationship relationship) {
        if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            return realization.getSupplier() instanceof Interface ? (Interface) realization.getSupplier() : null;
        }
        return null;
    }

    /**
     * Gets the super element.
     *
     * @param relationship the relationship
     * @return the super element
     */
    public static Element getSuperElement(Relationship relationship) {
        if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            return generalization.getParent();
        }
        return null;
    }

    /**
     * Gets the sub element.
     *
     * @param relationship the relationship
     * @return the sub element
     */
    public static Element getSubElement(Relationship relationship) {
        if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            return generalization.getChild();
        } else if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            return realization.getClient();
        }
        return null;
    }

    /**
     * Move relationship.
     *
     * @param relationship the relationship
     * @param client the client
     * @param supplier the supplier
     */
    public static void moveRelationship(Relationship relationship, Element client, Element supplier) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;

            architecture.removeRelationship(usage);
            ElementUtil.verifyAndRemoveRequiredInterface(usage.getClient(), usage.getSupplier());

            usage.setSupplier(supplier);
            usage.setClient(client);
            architecture.addRelationship(usage);
            ElementUtil.addRequiredInterface(client, supplier);
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;

            architecture.removeRelationship(dependency);
            ElementUtil.verifyAndRemoveRequiredInterface(dependency.getClient(), dependency.getSupplier());

            dependency.setSupplier(supplier);
            dependency.setClient(client);
            architecture.addRelationship(dependency);
            ElementUtil.addRequiredInterface(client, supplier);
        }
    }

    /**
     * Creates the new realization relationship.
     *
     * @param relationshipName the relationship name
     * @param client the client
     * @param supplier the supplier
     * @return the realization relationship
     */
    public static RealizationRelationship createNewRealizationRelationship(String relationshipName, Element client, Element supplier) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        RealizationRelationship realizationRelationship = new RealizationRelationship(client, supplier, relationshipName, UUID.randomUUID().toString());
        architecture.addRelationship(realizationRelationship);
        ElementUtil.addImplementedInterface(client, supplier);
        return realizationRelationship;
    }

    /**
     * Creates the new generalization relationship.
     *
     * @param child the child
     * @param parent the parent
     * @return the generalization relationship
     */
    public static GeneralizationRelationship createNewGeneralizationRelationship(Element child, Element parent) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        GeneralizationRelationship generalizationRelationship = new GeneralizationRelationship(parent, child, architecture.getRelationshipHolder(), UUID.randomUUID().toString());
        architecture.addRelationship(generalizationRelationship);
        ElementUtil.addImplementedInterface(child, parent);
        return generalizationRelationship;
    }

    /**
     * Creates the new usage relationship.
     *
     * @param relationshipName the relationship name
     * @param client the client
     * @param supplier the supplier
     * @return the usage relationship
     */
    public static UsageRelationship createNewUsageRelationship(String relationshipName, Element client, Element supplier) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        UsageRelationship usage = new UsageRelationship(relationshipName, supplier, client, UUID.randomUUID().toString());
        architecture.addRelationship(usage);
        ElementUtil.addRequiredInterface(client, supplier);
        return usage;
    }

    /**
     * Creates the new aggregation relationship.
     *
     * @param name the name
     * @param aggregator the aggregator
     * @param aggregated the aggregated
     * @return the association relationship
     */
    public static AssociationRelationship createNewAggregationRelationship(String name, Element aggregator, Element aggregated) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        AssociationRelationship associationRelationship = new AssociationRelationship(aggregator, aggregated);
        associationRelationship.setName(name);
        associationRelationship.getParticipants().get(1).setAggregation("shared");
        architecture.addRelationship(associationRelationship);
        ElementUtil.addRequiredInterface(aggregator, aggregated);
        return associationRelationship;
    }

}
