package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.AbstractionRelationship;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class OperationsOverAbstraction {

    static Logger LOGGER = LogManager.getLogger(OperationsOverAbstraction.class.getName());

    private Architecture architecture;

    public OperationsOverAbstraction(Architecture architecture) {
        this.architecture = architecture;
    }

    public void remove(AbstractionRelationship abstractionRelationship) {
        if (abstractionRelationship != null)
            if (!this.architecture.removeRelationship(abstractionRelationship))
                LOGGER.info("Cannot remove Abstraction " + abstractionRelationship + ".\n");
    }

    public void moveClient(AbstractionRelationship abstractionRelationship, Class newClient) {
        updateRelationship(abstractionRelationship, abstractionRelationship.getClient());
        abstractionRelationship.setClient(newClient);
        architecture.addRelationship(abstractionRelationship);
    }

    public void moveSupplier(AbstractionRelationship abstractionRelationship, Class newSupplier) {
        updateRelationship(abstractionRelationship, abstractionRelationship.getSupplier());
        abstractionRelationship.setSupplier(newSupplier);
        architecture.addRelationship(abstractionRelationship);

    }

    private void updateRelationship(AbstractionRelationship abstractionRelationship, Element element) {
        if (element instanceof Class) {
            Class supplier = (Class) element;
            supplier.getRelationshipHolder().removeRelationship(abstractionRelationship);
        }
        if (element instanceof Interface) {
            Interface supplier = (Interface) element;
            supplier.getRelationshipHolder().removeRelationship(abstractionRelationship);
        }
        if (element instanceof Package) {
            Package supplier = (Package) element;
            supplier.getRelationshipHolder().removeRelationship(abstractionRelationship);
        }
    }

    public void move(AbstractionRelationship abstractionRelationship, Class newSupplier, Class newCliente) {
        updateRelationship(abstractionRelationship, abstractionRelationship.getSupplier());
        updateRelationship(abstractionRelationship, abstractionRelationship.getClient());

        abstractionRelationship.setSupplier(newSupplier);
        abstractionRelationship.setClient(newCliente);

        architecture.addRelationship(abstractionRelationship);
    }

    public AbstractionRelationship create(Element newClient, Element newSupplier) {
        String id = UtilResources.getRandonUUID();
        AbstractionRelationship abs = new AbstractionRelationship(newClient, newSupplier, id);
        architecture.addRelationship(abs);
        this.architecture.addRelationship(abs);
        return abs;
    }

}
