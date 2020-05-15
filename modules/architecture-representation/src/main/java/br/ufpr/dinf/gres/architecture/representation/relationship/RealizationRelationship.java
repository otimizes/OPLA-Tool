package br.ufpr.dinf.gres.architecture.representation.relationship;

import br.ufpr.dinf.gres.architecture.helpers.ElementsTypes;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Package;

/**
 * Realization relationship class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class RealizationRelationship extends Relationship {

    private Element client;
    private Element supplier;


    public RealizationRelationship(Element client, Element supplier, String name, String id) {
        setClient(client);
        setSupplier(supplier);
        setId(id);
        super.setName(name);
        super.setType(ElementsTypes.REALIZATION);

        if ((client instanceof Package) && (supplier instanceof Interface)) {
            ((Package) client).addImplementedInterface((Interface) supplier);
        }
        if ((client instanceof Class) && (supplier instanceof Interface)) {
            ((Class) client).addImplementedInterface((Interface) supplier);
        }
    }

    public RealizationRelationship() {
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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((client == null) ? 0 : client.hashCode());
        result = prime * result
                + ((supplier == null) ? 0 : supplier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RealizationRelationship other = (RealizationRelationship) obj;
        if (this.supplier != other.supplier && (this.supplier == null || !this.supplier.equals(other.supplier))) {
            return false;
        }
        return this.client == other.client || (this.client != null && this.client.equals(other.client));
    }

}