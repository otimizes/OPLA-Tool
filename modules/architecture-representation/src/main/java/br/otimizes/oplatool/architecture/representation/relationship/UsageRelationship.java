package br.otimizes.oplatool.architecture.representation.relationship;

import br.otimizes.oplatool.architecture.helpers.ElementsTypes;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Element;

import java.util.Objects;

/**
 * Usage Relationship
 *
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
        setId(UtilResources.getRandomUUID());
        super.setType(ElementsTypes.USAGE);
    }

    public UsageRelationship() {
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
        if (!Objects.equals(this.supplier, other.supplier)) {
            return false;
        }
        return Objects.equals(this.client, other.client);
    }
}
