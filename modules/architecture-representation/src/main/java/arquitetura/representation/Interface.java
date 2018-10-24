package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.RelationshiopCommons;
import arquitetura.representation.relationship.Relationship;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Interface extends Element {


    private static final long serialVersionUID = -1779316062511432020L;

    static Logger LOGGER = LogManager.getLogger(Interface.class.getName());
    private final Set<Method> operations = new HashSet<Method>();
    private RelationshipsHolder relationshipHolder;
    private PatternsOperations patternsOperations;


    public Interface(RelationshipsHolder relationshipHolder, String name, Variant variantType, String namespace, String id) {
        super(name, variantType, "interface", namespace, id);
        setRelationshipHolder(relationshipHolder);
    }

    /**
     * Use este construtor quando você deseja criar uma interface.<br /><br />
     * <p>
     * OBS 1: O ID para esta interface será gerado automaticamente.<br/>
     * OBS 2: Esse construtor NAO adicionar a interface na arquitetura<br/>
     *
     * @param architecture       Architecture em questão
     * @param name               - Nome da interface
     * @param relationshipHolder
     */
    public Interface(RelationshipsHolder relationshipHolder, String name, Package packagee) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), packagee.getName()), UtilResources.getRandonUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), UtilResources.getRandonUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name, String id) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), id);
        this.setPatternOperations(new PatternsOperations());
    }

    /**
     * Use este construtor quando você deseja criar uma interface usando algum ID passado por você<br /><br />
     * <p>
     * OBS 1: Esse construtor NAO adicionar a interface na arquitetura<br/>
     *
     * @param name - Nome da interface
     * @param id   -  ID para a interface
     */
    public Interface(RelationshipsHolder relationshipHolder, String name, String id, Package packagee) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), packagee.getName()), id);
        this.setPatternOperations(new PatternsOperations());
    }

    public Set<Method> getOperations() {
        return Collections.unmodifiableSet(operations);
    }

    public boolean removeOperation(Method operation) {
        if (operations.remove(operation)) {
            LOGGER.info("Removeu operação '" + operation + "', da interface: " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU removeu operação '" + operation + "', da interface: " + this.getName() + " porém não conseguiu");
            return false;
        }
    }

    public Method createOperation(String operationName) throws Exception {
        Method operation = new Method(operationName, false, null, "void", false, null, "", ""); //Receber id
        operations.add(operation);
        return operation;
    }

    public boolean moveOperationToInterface(Method operation, Interface interfaceToMove) {
        if (!interfaceToMove.addExternalOperation(operation))
            return false;

        if (!removeOperation(operation)) {
            interfaceToMove.removeOperation(operation);
            return false;
        }
        operation.setNamespace(ArchitectureHolder.getName() + "::" + interfaceToMove.getName());
        LOGGER.info("Moveu operação: " + operation.getName() + " de " + this.getName() + " para " + interfaceToMove.getName());
        return true;

    }


    public boolean addExternalOperation(Method operation) {
        if (operations.add(operation)) {
            LOGGER.info("Operação " + operation.getName() + " adicionado na interface " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover a operação: " + operation.getName() + " da interface: " + this.getName() + " porém não consegiu");
            return false;
        }

    }


    public Set<Element> getImplementors() {
        Set<Element> implementors = new HashSet<Element>();

        Set<Relationship> relations = getRelationshipHolder().getRelationships();

        for (Relationship relationship : relations) {

            if (relationship instanceof RealizationRelationship) {
                RealizationRelationship realization = (RealizationRelationship) relationship;
                if (realization.getSupplier().equals(this))
                    implementors.add(realization.getClient());
            }

        }

        return Collections.unmodifiableSet(implementors);
    }

//	public Set<Element> getRealImplementors() {
//		Set<Element> implementors = new HashSet<Element>();
//		
//		for(Package p : getArchitecture().getAllPackages()){
//			for(RealizationRelationship r : getArchitecture().getAllRealizations()){
//				if(r.getClient().equals(p)){
//					implementors.add(p);
//				}
//			}
//		}
//					
//		return Collections.unmodifiableSet(implementors);
//	}

    public Set<Element> getDependents() {
        Set<Element> dependents = new HashSet<Element>();

        Set<Relationship> relations = getRelationshipHolder().getRelationships();

        for (Relationship relationship : relations) {
            if (relationship instanceof DependencyRelationship) {
                DependencyRelationship dependency = (DependencyRelationship) relationship;
                if (dependency.getSupplier().equals(this)) {
                    dependents.add(dependency.getClient());
                }
            }
        }
        return Collections.unmodifiableSet(dependents);
    }

    @Override
    public Set<Concern> getAllConcerns() {
        Set<Concern> concerns = new HashSet<Concern>(getOwnConcerns());
        for (Method operation : getOperations())
            concerns.addAll(operation.getAllConcerns());
        concerns.addAll(this.getOwnConcerns());

        return Collections.unmodifiableSet(concerns);
    }

    public List<DependencyRelationship> getDependencies() {
        List<DependencyRelationship> dependencies = new ArrayList<DependencyRelationship>();

        for (DependencyRelationship dependency : getRelationshipHolder().getAllDependencies()) {
            if (dependency.getSupplier().equals(this))
                dependencies.add(dependency);
        }

        return dependencies;
    }

    /**
     * Procura os elementos (Pacotes e Classes) que
     * implementam a interface em questão ou que a requerem a interface em questão e a remove da lista de interfaces implementadas
     * ou requeridas
     *
     * @param interfacee
     */
    public void removeInterfaceFromRequiredOrImplemented() {
        for (Iterator<Relationship> i = getRelationshipHolder().getRelationships().iterator(); i.hasNext(); ) {
            Relationship r = i.next();

            if (r instanceof RealizationRelationship) {
                RealizationRelationship realization = (RealizationRelationship) r;
                if (realization.getSupplier().equals(this)) {
                    if (realization.getClient() instanceof Package) {
                        ((Package) realization.getClient()).removeImplementedInterface(this);
                    }
                    if (realization.getClient() instanceof Class) {
                        ((Class) realization.getClient()).removeImplementedInterface(this);
                    }

                }
            }

            if (r instanceof DependencyRelationship) {
                DependencyRelationship dependency = (DependencyRelationship) r;
                if (dependency.getSupplier().equals(this)) {
                    if (dependency.getClient() instanceof Package) {
                        ((Package) dependency.getClient()).removeRequiredInterface(this);
                    }
                    if (dependency.getClient() instanceof Class) {
                        ((Class) dependency.getClient()).removeRequiredInterface(this);
                    }

                }
            }
        }
    }

    public RelationshipsHolder getRelationshipHolder() {
        return relationshipHolder;
    }

    public void setRelationshipHolder(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
    }

    public Set<Relationship> getRelationships() {
        return Collections.unmodifiableSet(RelationshiopCommons.getRelationships(relationshipHolder.getRelationships(), this));
    }

    public void setPatternOperations(PatternsOperations patternOperations) {
        this.patternsOperations = patternOperations;
    }

    public PatternsOperations getPatternsOperations() {
        return this.patternsOperations;
    }

    public List<RealizationRelationship> getRealizationImplementors() {
        List<RealizationRelationship> realization = new ArrayList<RealizationRelationship>();

        for (RealizationRelationship r : getRelationshipHolder().getAllRealizations()) {
            if (r.getSupplier().equals(this))
                realization.add(r);
        }

        return realization;
    }

    //Modificado Thais

    public void copyDependencyRelationship(Interface source, Interface targetInterface, Concern concern) {

        RelationshipsHolder newRelation = new RelationshipsHolder();
        newRelation.setRelationships(targetInterface.getRelationships());

        for (DependencyRelationship d : source.getDependencies()) {
            boolean existe = false;
            for (DependencyRelationship dAlvo : targetInterface.getDependencies()) {

                if (d.getSupplier().equals(dAlvo.getSupplier())) {
                    existe = true;
                }
            }
            if (!existe) {
                if (d.getSupplier().containsConcern(concern)) {
                    DependencyRelationship newDependence = new DependencyRelationship(d.getSupplier(), targetInterface, d.getName());
                    newRelation.addRelationship(newDependence);
                }
            }

        }
        targetInterface.setRelationshipHolder(newRelation);
    }

    //Modificado Thais

    public void copyRealizationRelationship(Interface source, Interface targetInterface, Concern concern) {

        RelationshipsHolder newRelation2 = new RelationshipsHolder();
        newRelation2.setRelationships(targetInterface.getRelationships());

        for (RealizationRelationship r : source.getRealizationImplementors()) {
            boolean existe = false;
            for (RealizationRelationship cAlvo : targetInterface.getRealizationImplementors()) {

                if (r.getSupplier().equals(cAlvo.getSupplier())) {
                    existe = true;
                }
            }
            if (!existe) {
                if (r.getSupplier().containsConcern(concern)) {
                    RealizationRelationship newImplementor = new RealizationRelationship(r.getSupplier(), targetInterface, r.getName(), r.getId());
                    newRelation2.addRelationship(newImplementor);
                }
            }

            targetInterface.setRelationshipHolder(newRelation2);

        }

    }

    //Modificado Thais

    public void copyAllDependenciesSuppliers(Interface source, Interface targetInterface) {

        RelationshipsHolder newRelation = new RelationshipsHolder();
        newRelation.setRelationships(targetInterface.getRelationships());

        for (DependencyRelationship d : source.getDependencies()) {
            boolean existe = false;
            for (DependencyRelationship dAlvo : targetInterface.getDependencies()) {

                if (d.getSupplier().equals(dAlvo.getSupplier())) {
                    existe = true;
                }
            }
            if (!existe) {
                DependencyRelationship newDependence = new DependencyRelationship(d.getSupplier(), targetInterface, d.getName());
                newRelation.addRelationship(newDependence);
            }

        }
        targetInterface.setRelationshipHolder(newRelation);
    }

}
