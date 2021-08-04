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
public class Interface extends Element implements Linkable, Functioning {

    private static final long serialVersionUID = -1779316062511432020L;

    static Logger LOGGER = LogManager.getLogger(Interface.class.getName());
    private final Set<Method> methods = new HashSet<>();
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

    public Method findMethodById(String id) {
        for (Method m : getMethods()) {
            if (m.getId().equalsIgnoreCase(id)) {
                return m;
            }
        }
        return null;
    }

    public Interface(RelationshipsHolder relationshipHolder, String name, Package packagee) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(),
                packagee.getName()), UtilResources.getRandomUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name),
                UtilResources.getRandomUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name, String id) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), id);
        this.setPatternOperations(new PatternsOperations());
    }

    public Interface(RelationshipsHolder relationshipHolder, String name, String id, Package packageOfInterface) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(),
                packageOfInterface.getName()), id);
        this.setPatternOperations(new PatternsOperations());
    }

    public Set<Method> getMethods() {
        return Collections.unmodifiableSet(methods);
    }

    public Set<Method> getModifiableMethods() {
        return methods;
    }

    public boolean removeMethod(Method method) {
        if (methods.remove(method)) {
            LOGGER.info("Removed method '" + method + "', from interface: " + this.getName());
            return true;
        } else {
            LOGGER.info("Tried to remove method '" + method + "', from interface: " + this.getName() + " but couldn't");
            return false;
        }
    }

    public void removeMethodByID(String id) {
        for (Method m : this.methods) {
            if (m.getId().equalsIgnoreCase(id)) {
                this.methods.remove(m);
                return;
            }
        }
    }

    public Method createMethod(String methodName) throws Exception {
        Method method = new Method(methodName, "void",
                false, null, "", "");
        methods.add(method);
        return method;
    }

    public boolean moveMethodToInterface(Method method, Interface interfaceToMove) {
        if (!interfaceToMove.addExternalMethod(method))
            return false;

        if (!removeMethod(method)) {
            interfaceToMove.removeMethod(method);
            return false;
        }
        method.setNamespace(ArchitectureHolder.getName() + "::" + interfaceToMove.getName());
        LOGGER.info("Moved method: " + method.getName() + " from " + this.getName() + " to " + interfaceToMove.getName());
        return true;
    }

    public Set<Element> getImplementors() {
        Set<Element> implementors = new HashSet<>();
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

    public Set<Element> getDependents() {
        Set<Element> dependents = new HashSet<>();
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
        Set<Concern> concerns = new HashSet<>(getOwnConcerns());
        for (Method method : getMethods())
            concerns.addAll(method.getAllConcerns());
        concerns.addAll(this.getOwnConcerns());
        return Collections.unmodifiableSet(concerns);
    }

    public List<DependencyRelationship> getDependencies() {
        List<DependencyRelationship> dependencies = new ArrayList<>();
        for (DependencyRelationship dependency : getRelationshipHolder().getAllDependencies()) {
            if (dependency.getSupplier().equals(this))
                dependencies.add(dependency);
        }
        return dependencies;
    }

    public boolean addExternalMethod(Method method) {
        if (methods.add(method)) {
            LOGGER.info("Method " + method.getName() + " added in interface " + this.getName());
            return true;
        } else {
            LOGGER.info("Tried to remove the method: " + method.getName() + " from interface: " + this.getName() + " but couldn't");
            return false;
        }
    }

    public void removeInterfaceFromRequiredOrImplemented() {
        for (Relationship relationship : getRelationshipHolder().getRelationships()) {
            if (relationship instanceof RealizationRelationship) {
                RealizationRelationship realization = (RealizationRelationship) relationship;
                if (realization.getSupplier().equals(this)) {
                    if (realization.getClient() instanceof Package) {
                        ((Package) realization.getClient()).removeImplementedInterface(this);
                    }
                    if (realization.getClient() instanceof Class) {
                        ((Class) realization.getClient()).removeImplementedInterface(this);
                    }

                }
            }
            if (relationship instanceof DependencyRelationship) {
                DependencyRelationship dependency = (DependencyRelationship) relationship;
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
        List<RealizationRelationship> realization = new ArrayList<>();
        for (RealizationRelationship realizationRelationship : getRelationshipHolder().getAllRealizations()) {
            if (realizationRelationship.getSupplier().equals(this))
                realization.add(realizationRelationship);
        }
        return realization;
    }

    public void copyDependencyRelationship(Interface source, Interface targetInterface, Concern concern) {
        RelationshipsHolder newRelation = new RelationshipsHolder();
        newRelation.setRelationships(targetInterface.getRelationships());
        for (DependencyRelationship dependencyRelationship : source.getDependencies()) {
            boolean thereExists = false;
            for (DependencyRelationship target : targetInterface.getDependencies()) {
                if (dependencyRelationship.getSupplier().equals(target.getSupplier())) {
                    thereExists = true;
                    break;
                }
            }
            if (!thereExists) {
                if (dependencyRelationship.getSupplier().containsConcern(concern)) {
                    DependencyRelationship newDependence = new DependencyRelationship(dependencyRelationship.getSupplier(),
                            targetInterface, dependencyRelationship.getName());
                    newRelation.addRelationship(newDependence);
                }
            }

        }
        targetInterface.setRelationshipHolder(newRelation);
    }

    public void copyRealizationRelationship(Interface source, Interface targetInterface, Concern concern) {
        RelationshipsHolder newRelation = new RelationshipsHolder();
        newRelation.setRelationships(targetInterface.getRelationships());
        for (RealizationRelationship realizationRelationship : source.getRealizationImplementors()) {
            boolean thereExists = false;
            for (RealizationRelationship target : targetInterface.getRealizationImplementors()) {
                if (realizationRelationship.getSupplier().equals(target.getSupplier())) {
                    thereExists = true;
                    break;
                }
            }
            if (!thereExists) {
                if (realizationRelationship.getSupplier().containsConcern(concern)) {
                    RealizationRelationship newImplementor = new RealizationRelationship(realizationRelationship.getSupplier(),
                            targetInterface, realizationRelationship.getName(), realizationRelationship.getId());
                    newRelation.addRelationship(newImplementor);
                }
            }
            targetInterface.setRelationshipHolder(newRelation);
        }
    }

    public void copyAllDependenciesSuppliers(Interface source, Interface targetInterface) {
        RelationshipsHolder newRelation = new RelationshipsHolder();
        newRelation.setRelationships(targetInterface.getRelationships());
        for (DependencyRelationship dependencyRelationship : source.getDependencies()) {
            boolean thereExists = false;
            for (DependencyRelationship target : targetInterface.getDependencies()) {
                if (dependencyRelationship.getSupplier().equals(target.getSupplier())) {
                    thereExists = true;
                    break;
                }
            }
            if (!thereExists) {
                DependencyRelationship newDependency = new DependencyRelationship(dependencyRelationship.getSupplier(),
                        targetInterface, dependencyRelationship.getName());
                newRelation.addRelationship(newDependency);
            }
        }
        targetInterface.setRelationshipHolder(newRelation);
    }
}
