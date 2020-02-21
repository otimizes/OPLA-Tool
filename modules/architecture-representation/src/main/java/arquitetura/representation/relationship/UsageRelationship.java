package arquitetura.representation.relationship;

import arquitetura.helpers.ElementsTypes;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Class;
import arquitetura.representation.Element;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class UsageRelationship extends Relationship {

    private String name;
    private Element supplier;
    private Element client;

    public UsageRelationship(String name, Element supplier, Element client, String id) {
        super();
        this.name = name;
        this.supplier = supplier;
        this.client = client;
        setId(id);
        super.setType(ElementsTypes.USAGE);
    }

    public UsageRelationship(String string, Class supplier2, Class client2) {
        super();
        this.supplier = supplier2;
        this.client = client2;
        setId(UtilResources.getRandonUUID());
        super.setType(ElementsTypes.USAGE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Element getSupplier() {
        return supplier;
    }

    public void setSupplier(Element supplier) {
        this.supplier = supplier;
    }

    public Element getClient() {
        return client;
    }

    public void setClient(Element client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UsageRelationship other = (UsageRelationship) obj;
        if (this.supplier != other.supplier && (this.supplier == null || !this.supplier.equals(other.supplier))) {
            return false;
        }
        return this.client == other.client || (this.client != null && this.client.equals(other.client));
    }
}
