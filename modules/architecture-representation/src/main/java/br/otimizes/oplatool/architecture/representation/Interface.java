package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.representation.relationship.RelationshipCommons;
import com.rits.cloning.Cloner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Interface representation
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Interface extends Element implements Linkable {


    private static final long serialVersionUID = -1779316062511432020L;

    static Logger LOGGER = LogManager.getLogger(Interface.class.getName());
    private final Set<Method> methods = new HashSet<Method>();
    private RelationshipsHolder relationshipHolder;
    private PatternsOperations patternsOperations;


    public Interface(RelationshipsHolder relationshipHolder, String name, Variant variantType, String namespace, String id) {
        super(name, variantType, "interface", namespace, id);
        setRelationshipHolder(relationshipHolder);
    }

    public Interface deepCopy() {
        try {
            return this.deepClone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Interface deepClone() throws CloneNotSupportedException {
        Cloner cloner = new Cloner();
        Interface anInterface = cloner.deepClone(this);
        cloner = null;
        return anInterface;
    }

    public Method findOperationById(String id) {
        for (Method m : getMethods()) {
            if (m.getId().equalsIgnoreCase(id)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Use este construtor quando você deseja criar uma interface.<br /><br />
     * <p>
     * OBS 1: O ID para esta interface será gerado automaticamente.<br/>
     * OBS 2: Esse construtor NAO adicionar a interface na br.otimizes.oplatool.arquitetura<br/>
     *
     * @param architecture       Architecture em questão
     * @param name               - Nome da interface
     * @param relationshipHolder
     */
    public Interface(RelationshipsHolder relationshipHolder, String name, Package packagee) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), packagee.getName()), UtilResources.getRandomUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), UtilResources.getRandomUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name, String id) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), id);
        this.setPatternOperations(new PatternsOperations());
    }

    /**
     * Use este construtor quando você deseja criar uma interface usando algum ID passado por você<br /><br />
     * <p>
     * OBS 1: Esse construtor NAO adicionar a interface na br.otimizes.oplatool.arquitetura<br/>
     *
     * @param name - Nome da interface
     * @param id   -  ID para a interface
     */
    public Interface(RelationshipsHolder relationshipHolder, String name, String id, Package packagee) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), packagee.getName()), id);
        this.setPatternOperations(new PatternsOperations());
    }

    public Set<Method> getMethods() {
        return Collections.unmodifiableSet(methods);
    }

    public boolean removeOperation(Method operation) {
        if (methods.remove(operation)) {
            LOGGER.info("Removeu operação '" + operation + "', da interface: " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU removeu operação '" + operation + "', da interface: " + this.getName() + " porém não conseguiu");
            return false;
        }
    }

    public void removeOperationByID(String id) {
        //Method operation = null;
        for (Method m : this.methods) {
            if (m.getId().equalsIgnoreCase(id)) {
                //operation = m;
                this.methods.remove(m);
                return;
            }
        }
    }

    public Method createOperation(String operationName) throws Exception {
        Method operation = new Method(operationName, false, null, "void", false, null, "", ""); //Receber id
        methods.add(operation);
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
        if (methods.add(operation)) {
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

    public Set<Method> getModifiableOperations() {
        return methods;
    }

    @Override
    public Set<Concern> getAllConcerns() {
        Set<Concern> concerns = new HashSet<Concern>(getOwnConcerns());
        for (Method operation : getMethods())
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

    public boolean addExternalMethod(Method method) {
        if (methods.add(method)) {
            LOGGER.info("Operação " + method.getName() + " adicionado na interface " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover a operação: " + method.getName() + " da interface: " + this.getName() + " porém não consegiu");
            return false;
        }
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
        return Collections.unmodifiableSet(RelationshipCommons.getRelationships(relationshipHolder.getRelationships(), this));
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
