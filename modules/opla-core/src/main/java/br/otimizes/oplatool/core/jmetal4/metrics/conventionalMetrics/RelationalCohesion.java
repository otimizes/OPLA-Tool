package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Relational cohesion.
 * <p>
 * This is the average number of internal relationships per class/interface,
 * and is calculated as the ratio of R+1 to the number of classes and
 * interfaces in the package.
 * <p>
 * R is the number of relationships between classes and interfaces in the
 * package. There is a dependency from class or interface C to class or
 * interface D if
 * C has an attribute of type D
 * C has an operation with a parameter of type D
 * C has an association, aggregation, or composition with navigability to D
 * C has a UML dependency or usage dependency to D
 * UML dependencies are shown as dashed arrows in the diagrams (usage with
 * stereotype 'use').
 * C is a child of D
 * C implements interface D
 * The metric counts all such dependencies between classes and interfaces in
 * the package. Bidirectional associations are counted twice, because C
 * knows D and vice versa. By convention, associations that indicate no
 * navigability at either end are considered to be bidirectional.
 */
public class RelationalCohesion extends ObjectiveFunctionImplementation {

    public Double evaluateICohesion() {
        Double results = this.getResults();
        return results == 0 ? 1.0 : 1 / results;
    }

    public RelationalCohesion(Architecture architecture) {
        super(architecture);

        this.setResults(0.0);
        int totalClassesAndInterfaces = 0;
        int R = 0;
        double H = 0;

        for (Package component : architecture.getAllPackages()) {

            for (Interface itf : component.getImplementedInterfaces()) {
                R += searchOperationInterfaceDependencies(itf, component);
                R += searchImplementationDependencies(itf, component);
            }
            for (Class cls : component.getAllClasses()) {
                R += searchAttributeClassDependencies(cls, component);
                R += searchOperationClassDependencies(cls, component);
                R += searchAssociationClassDependencies(cls, component);
            }
            totalClassesAndInterfaces = component.getAllClasses().size() + component.getImplementedInterfaces().size();
            if (totalClassesAndInterfaces != 0) {
                H = (R + 1) / totalClassesAndInterfaces;
                this.addToResults(H); // soma de H para a br.otimizes.oplatool.arquitetura
            }

            R = 0;
            H = 0;
        }

    }

    // C has an attribute of type D
    private int searchAttributeClassDependencies(Class source, Package comp) {
        int attribDependencies;
        List<Class> classes = new ArrayList<>(comp.getAllClasses());
        List<Interface> interfaces = new ArrayList<>(comp.getImplementedInterfaces());
        List<Class> attribDepClasses = new ArrayList<>();
        List<Interface> attribDepInterfaces = new ArrayList<>();
        for (Class c : classes) {
            List<Attribute> allAttributes = new ArrayList<>(source.getAllAttributes());
            for (Attribute attribute : allAttributes) {
                if (c.getName().equals(attribute.getType())) {
                    if (!(attribDepClasses.contains(c)))
                        attribDepClasses.add(c);
                }
            }
        }

        for (Interface itf : interfaces) {
            List<Attribute> allAttributes = new ArrayList<Attribute>(source.getAllAttributes());
            for (Attribute attribute : allAttributes) {
                if (itf.getName().equals(attribute.getType())) {
                    if (!(attribDepInterfaces.contains(itf)))
                        attribDepInterfaces.add(itf);
                }
            }
        }
        attribDependencies = attribDepClasses.size() + attribDepInterfaces.size();
        return attribDependencies;
    }


    private int searchOperationClassDependencies(Class source, Package comp) {
        int operationDependencies;
        List<Class> classes = new ArrayList<>(comp.getAllClasses());
        List<Class> operationDepClasses = new ArrayList<>();
        List<Interface> operationDepInterfaces = new ArrayList<>();
        for (Class c : classes) {
            List<Method> allOperations = new ArrayList<>(source.getAllMethods());
            for (Method method : allOperations) {
                Collection<ParameterMethod> parameters = method.getParameters();
                for (ParameterMethod parameter : parameters) {
                    if ((parameter.getName().contains(c.getName())) && (!(operationDepClasses.contains(c))))
                        operationDepClasses.add(c);
                }
            }
        }

        // for (Interface itf: interfaces){
        // List<Method> allOperations = new ArrayList<Method>
        // (source.getAllMethods());
        // for (Method method: allOperations){
        // Collection<ParameterMethod> parameters = method.getParameters();
        // for (ParameterMethod parameter: parameters) {
        // if ((parameter.getName().contains(itf.getName())) &&
        // (!(operationDepInterfaces.contains(itf))))
        // operationDepInterfaces.add(itf);
        // }
        // }
        // }
        operationDependencies = operationDepClasses.size() + operationDepInterfaces.size();
        return operationDependencies;
    }

    private int searchOperationInterfaceDependencies(Interface source, Package comp) {
        int operationDependencies;
        List<Class> classes = new ArrayList<Class>(comp.getAllClasses());
        List<Interface> interfaces = new ArrayList<Interface>(comp.getImplementedInterfaces());
        List<Class> operationDepClasses = new ArrayList<Class>();
        List<Interface> operationDepInterfaces = new ArrayList<Interface>();
        for (Class c : classes) {

            List<Method> allOperations = new ArrayList<Method>(source.getMethods());
            for (Method operation : allOperations) {

                Collection<ParameterMethod> parameters = operation.getParameters();
                for (ParameterMethod parameter : parameters) {
                    if ((parameter.getName().contains(c.getName())) && (!(operationDepClasses.contains(c)))) {
                        operationDepClasses.add(c);
                    }
                }
            }
        }

        for (Interface itf : interfaces) {
            List<Method> allOperations = new ArrayList<Method>(source.getMethods());

            for (Method operation : allOperations) {

                Collection<ParameterMethod> parameters = operation.getParameters();
                for (ParameterMethod parameter : parameters) {
                    if ((parameter.getName().contains(itf.getName())) && (!(operationDepInterfaces.contains(itf)))) {
                        operationDepInterfaces.add(itf);
                    }
                }
            }
        }
        operationDependencies = operationDepClasses.size() + operationDepInterfaces.size();
        return operationDependencies;
    }

    private int searchAssociationClassDependencies(Class source, Package comp) {
        int associationDependencies;
        List<Class> associationDepClasses = new ArrayList<Class>();
        List<Interface> associationDepInterfaces = new ArrayList<Interface>();

        for (Class c : comp.getAllClasses()) {
            List<Relationship> relationships = new ArrayList<Relationship>(source.getRelationships());
            for (Relationship relationship : relationships) {
                if (relationship instanceof GeneralizationRelationship) {
                    GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                    if (generalization.getParent() != null && generalization.getParent().equals(c.getName()) && (!(associationDepClasses.contains(c))))
                        associationDepClasses.add(c);

                }

                if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getClient() != null && dependency.getClient().equals(c.getName()) && (!(associationDepClasses.contains(c))))
                        associationDepClasses.add(c);
                }

                if (relationship instanceof AssociationRelationship) {
                    AssociationRelationship association = (AssociationRelationship) relationship;
                    for (AssociationEnd associationEnd : association.getParticipants()) {
                        if (associationEnd.getCLSClass() != null && associationEnd.getCLSClass().equals(c.getName()) && (!(associationDepClasses.contains(c)))) {
                            associationDepClasses.add(c);
                        }
                    }
                }
            }
        }// end for classes

        for (Interface itf : comp.getImplementedInterfaces()) {
            List<Relationship> relationships = new ArrayList<Relationship>(source.getRelationships());
            for (Relationship relationship : relationships) {
                if (relationship instanceof GeneralizationRelationship) {
                    GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                    if (generalization.getParent() != null && generalization.getParent().equals(itf.getName()) && (!(associationDepInterfaces.contains(itf))))
                        associationDepInterfaces.add(itf);

                }

                if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getClient().equals(itf.getName()) && (!(associationDepInterfaces.contains(itf))))
                        associationDepInterfaces.add(itf);
                }

                if (relationship instanceof AssociationRelationship) {
                    AssociationRelationship association = (AssociationRelationship) relationship;
                    for (AssociationEnd associationEnd : association.getParticipants()) {
                        if (associationEnd.getCLSClass() != null && associationEnd.getCLSClass().equals(itf.getName())
                                && (!(associationDepInterfaces.contains(itf)))) {
                            associationDepInterfaces.add(itf);
                        }
                    }
                }
            }
        }// end for interfaces
        associationDependencies = associationDepClasses.size() + associationDepInterfaces.size();
        return associationDependencies;
    }

    private int searchImplementationDependencies(Interface itf, Package comp) {
        List<Element> depComponents = new ArrayList<Element>();
        for (Element c : itf.getImplementors())
            if (!(depComponents.contains(c))) {
                depComponents.add(c);
            }

        return depComponents.size();
    }
}
