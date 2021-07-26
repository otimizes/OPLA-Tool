package br.otimizes.oplatool.architecture.representation.relationship;

import br.otimizes.oplatool.architecture.helpers.ElementsTypes;
import br.otimizes.oplatool.architecture.representation.Element;

import java.util.Objects;

/**
 * Abstraction relationship class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AbstractionRelationship extends Relationship {

    private Element client;
    private Element supplier;

    public AbstractionRelationship(Element client, Element supplier, String id) {
        setClient(client);
        setSupplier(supplier);
        setId(id);
        super.setType(ElementsTypes.ABSTRACTION);
    }

    public AbstractionRelationship() {
    }

    /**
     * @return the client
     */
    public Element getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Element client) {
        this.client = client;
    }

    /**
     * @return the supplier
     */
    public Element getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(Element supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractionRelationship other = (AbstractionRelationship) obj;
        if (!Objects.equals(this.supplier, other.supplier)) {
            return false;
        }
        return Objects.equals(this.client, other.client);
    }
}