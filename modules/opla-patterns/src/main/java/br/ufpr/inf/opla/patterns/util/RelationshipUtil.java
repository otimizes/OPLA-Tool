package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.*;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;

import java.util.UUID;

public class RelationshipUtil {

    private RelationshipUtil() {
    }

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

    public static Interface getImplementedInterface(Relationship relationship) {
        if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            return realization.getSupplier() instanceof Interface ? (Interface) realization.getSupplier() : null;
        }
        return null;
    }

    public static Element getSuperElement(Relationship relationship) {
        if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            return generalization.getParent();
        }
        return null;
    }

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

    public static RealizationRelationship createNewRealizationRelationship(String relationshipName, Element client, Element supplier) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        RealizationRelationship realizationRelationship = new RealizationRelationship(client, supplier, relationshipName, UUID.randomUUID().toString());
        architecture.addRelationship(realizationRelationship);
        ElementUtil.addImplementedInterface(client, supplier);
        return realizationRelationship;
    }

    public static GeneralizationRelationship createNewGeneralizationRelationship(Element child, Element parent) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        GeneralizationRelationship generalizationRelationship = new GeneralizationRelationship(parent, child, architecture.getRelationshipHolder(), UUID.randomUUID().toString());
        architecture.addRelationship(generalizationRelationship);
        ElementUtil.addImplementedInterface(child, parent);
        return generalizationRelationship;
    }

    public static UsageRelationship createNewUsageRelationship(String relationshipName, Element client, Element supplier) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        UsageRelationship usage = new UsageRelationship(relationshipName, supplier, client, UUID.randomUUID().toString());
        architecture.addRelationship(usage);
        ElementUtil.addRequiredInterface(client, supplier);
        return usage;
    }

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
