package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.UsageRelationship;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Operations over usage class
 */
public class OperationsOverUsage {

    static Logger LOGGER = LogManager.getLogger(OperationsOverUsage.class.getName());

    private final Architecture architecture;

    public OperationsOverUsage(Architecture architecture) {
        this.architecture = architecture;
    }

    public void remove(UsageRelationship usageRelationship) {
        if (!this.architecture.removeRelationship(usageRelationship))
            LOGGER.info("Cannot remove Usage " + usageRelationship + ".\n");
    }

    public void moveClient(UsageRelationship usageRelationship, Element newClient) {
        usageRelationship.setClient(newClient);
    }

    public void moveSupplier(UsageRelationship usageRelationship, Element newSupplier) {
        usageRelationship.setSupplier(newSupplier);
    }

    public UsageRelationship create(Element client, Element supplier) {
        UsageRelationship usage = new UsageRelationship("", supplier, client, UtilResources.getRandomUUID());
        this.architecture.addRelationship(usage);
        return usage;
    }

    public void move(UsageRelationship usageRelationship, Class newSupplier, Class newClient) {
        usageRelationship.setClient(newClient);
        usageRelationship.setSupplier(newSupplier);
    }
}
