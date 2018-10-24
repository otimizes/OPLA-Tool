package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.GeneralizationRelationship;

public class OperationsOverGeneralization {

    private Architecture architecture;

    public OperationsOverGeneralization(Architecture architecture) {
        this.architecture = architecture;
    }

    public void moveGeneralizationParent(GeneralizationRelationship gene, Class targetClass) {
        gene.setParent(targetClass);
    }

    public void moveGeneralizationSubClass(GeneralizationRelationship gene, Class class1) {
        gene.setChild(class1);
    }

    /**
     * @param gene
     * @param parent
     * @param child
     */
    public void moveGeneralization(GeneralizationRelationship gene, Class parent, Class child) {
        gene.setParent(parent);
        gene.setChild(child);
    }

    public void addChildToGeneralization(GeneralizationRelationship generalizationRelationship, Element newChild) {
        GeneralizationRelationship g = new GeneralizationRelationship(generalizationRelationship.getParent(), newChild, this.architecture.getRelationshipHolder(), UtilResources.getRandonUUID());
        this.architecture.addRelationship(g);
    }

    /**
     * Cria um relacionamento de generalização e o adiciona na arquitetura<br/><br/>
     * <p>
     * NOTA: usando este método você não precisa chamar explicitamente algo como<br/><br/> {@code architecture.addRelationship(relationship)}.
     *
     * @param parent
     * @param child
     * @return
     */
    public GeneralizationRelationship createGeneralization(Element parent, Element child) {
        GeneralizationRelationship g = new GeneralizationRelationship(parent, child, this.architecture.getRelationshipHolder(), UtilResources.getRandonUUID());
        this.architecture.addRelationship(g);
        return g;
    }

    /**
     * Dada uma generalização {@link GeneralizationRelationship} move a mesma o pacote {@link Package} destino.<br/><br/>
     * <p>
     * Este método irá pegar o pai (parent) e os filhos (childreen) da generalização passada como paramêtros
     * e mover para o pacote destino.
     *
     * @param generalization - Generalização a ser movida
     * @param targetPackage  - Pacote destino
     */
    public void moveGeneralizationToPackage(GeneralizationRelationship generalization, Package targetPackage) {
        architecture.moveElementToPackage(generalization.getParent(), targetPackage);
        for (Element element : generalization.getAllChildrenForGeneralClass())
            architecture.moveElementToPackage(element, targetPackage);
    }

}