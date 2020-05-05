package br.ufpr.dinf.gres.architecture.representation.relationship;

import br.ufpr.dinf.gres.architecture.exceptions.NotFoundException;
import br.ufpr.dinf.gres.architecture.helpers.ElementsTypes;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Package;

public class RequiresRelationship extends Relationship {
    String name;
    private Element client;
    private Element supplier;

    public RequiresRelationship() {
    }

    /**
     * @param supplier
     * @param client
     * @param name
     * @param id
     */
    public RequiresRelationship(Element supplier, Element client, String name, String id) {
        setSupplier(supplier);
        setClient(client);
        setName(name);
        setId(id);
        super.setType(ElementsTypes.REQUIRES);

        setRequiredInterfaces(supplier, client);

    }

    public RequiresRelationship(Element supplier, Element client, String string) {
        setSupplier(supplier);
        setClient(client);
        setName(name);
        setId(UtilResources.getRandonUUID());
        super.setType(ElementsTypes.DEPENDENCY);

        setRequiredInterfaces(supplier, client);
    }

    /**
     * Retorna o {@link Package}
     *
     * @return Package se existir.
     * @throws NotFoundException caso não exista pacote envolvido na dependencia.
     */
    public Package getPackageOfDependency() throws NotFoundException {
        if (this.client instanceof Package)
            return (Package) this.client;
        else if (this.supplier instanceof Package)
            return (Package) this.supplier;

        throw new NotFoundException("There is no Package in this dependency.");
    }

    /**
     * Retorna a {@link Interface}
     *
     * @return Interface se existir.
     * @throws NotFoundException caso não exista interface envolvido na dependencia.
     */
    public Interface getInterfaceOfDependency() throws NotFoundException {
        if (this.client instanceof Interface)
            return (Interface) this.client;
        else if (this.supplier instanceof Interface)
            return (Interface) this.supplier;

        throw new NotFoundException("There is no Interface in this dependency.");
    }

    public Element getClient() {
        return client;
    }

    public void setClient(Element client) {
        this.client = client;
    }

    public Element getSupplier() {
        return supplier;
    }

    public void setSupplier(Element supplier) {
        this.supplier = supplier;
    }

    public void replaceSupplier(Element supplier) {
        setSupplier(supplier);
    }

    public void replaceClient(Element client) {
        setClient(client);
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setRequiredInterfaces(Element supplier, Element client) {
        if ((client instanceof Package) && (supplier instanceof Interface)) {
            ((Package) client).addRequiredInterface((Interface) supplier);
        }

        if ((client instanceof Class) && (supplier instanceof Interface)) {
            ((Class) client).addRequiredInterface((Interface) supplier);
        }
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
        final RequiresRelationship other = (RequiresRelationship) obj;
        if (this.supplier != other.supplier && (this.supplier == null || !this.supplier.equals(other.supplier))) {
            return false;
        }
        return this.client == other.client || (this.client != null && this.client.equals(other.client));
    }
}
