package br.otimizes.oplatool.patterns.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.repositories.ArchitectureRepository;
import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Multiplicity;
import br.otimizes.oplatool.patterns.comparators.SubElementsComparator;
import br.otimizes.oplatool.patterns.list.MethodArrayList;

/**
 * The Class BridgeUtil.
 */
public class BridgeUtil {

    /**
     * Instantiates a new bridge util.
     */
    private BridgeUtil() {
    }

    /**
     * Gets the implementation interfaces.
     *
     * @param algorithmFamily the algorithm family
     * @return the list of implementation interfaces
     */
    public static HashMap<Concern, List<Interface>> getImplementationInterfaces(AlgorithmFamily algorithmFamily) {
        List<Element> elements = algorithmFamily.getParticipants();
        HashMap<Concern, List<Interface>> implementationInterfaces = new HashMap<>();
        HashMap<Concern, List<Element>> groupedElements = ElementUtil.groupElementsByConcern(elements);
        for (Map.Entry<Concern, List<Element>> entry : groupedElements.entrySet()) {
            Concern concern = entry.getKey();
            List<Element> elementsByConcerns = entry.getValue();
            List<Interface> allSuperInterfaces = ElementUtil.getAllSuperInterfaces(elementsByConcerns);
            Collections.sort(allSuperInterfaces, SubElementsComparator.getDescendingOrderer());
            List<Method> allMethods = MethodUtil.getAllCommonMethodsFromSetOfElementsByConcern(elementsByConcerns, concern);
            if (allMethods.isEmpty()) {
                allMethods = new MethodArrayList(MethodUtil.getAllMethodsFromSetOfElementsByConcern(elementsByConcerns, concern));
            }
            for (int i = 0; i < allSuperInterfaces.size(); i++) {
                Interface anInterface = allSuperInterfaces.get(i);
                MethodArrayList anInterfaceMethods = new MethodArrayList(MethodUtil.getAllMethodsFromElement(anInterface));
                if (concern != null ? !anInterface.getAllConcerns().contains(concern) && !anInterfaceMethods.containsAll(allMethods) : !anInterfaceMethods.containsAll(allMethods)) {
                    allSuperInterfaces.remove(anInterface);
                    i--;
                }
            }
            if (allSuperInterfaces.isEmpty()) {
                String preffix = concern == null ? "Default" : Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1);
                Interface anInterface = ArchitectureRepository.getCurrentArchitecture().findInterfaceByName(preffix + "Implementation");
                if (anInterface != null) {
                    allSuperInterfaces.add(anInterface);
                }
            }
            implementationInterfaces.put(concern, allSuperInterfaces);
        }
        return implementationInterfaces;
    }

    /**
     * Gets the abstraction classes.
     *
     * @param algorithmFamily the algorithm family
     * @return the list of abstraction classes
     */
    public static List<Element> getAbstractionClasses(AlgorithmFamily algorithmFamily) {
        List<Element> abstractionClasses = new ArrayList<>();
        Set<Class> elements = ArchitectureRepository.getCurrentArchitecture().getAllClasses();
        root:
        for (Class classElement : elements) {
            if (classElement.isAbstract()) {
                Set<Concern> allConcernsFromSetOfElements = ElementUtil.getOwnAndMethodsConcerns(algorithmFamily.getParticipants());
                MethodArrayList methodArrayList = new MethodArrayList(MethodUtil.getAllMethodsFromElement(classElement));
                MethodArrayList allMethods = new MethodArrayList(MethodUtil.getAllCommonMethodsFromSetOfElements(algorithmFamily.getParticipants()));
                if (allMethods.isEmpty()) {
                    allMethods = new MethodArrayList(MethodUtil.getAllMethodsFromSetOfElements(algorithmFamily.getParticipants()));
                }
                if (methodArrayList.containsAll(allMethods)) {
                    if (ElementUtil.getOwnAndMethodsConcerns(classElement).containsAll(allConcernsFromSetOfElements)) {
                        Set<Element> aggregatedElements = ElementUtil.getAllAggregatedElements(classElement);
                        boolean contains = false;
                        for (Element participant : algorithmFamily.getParticipants()) {
                            if (!participant.equals(classElement) && !ElementUtil.isTypeOf(participant, classElement)) {
                                List<Interface> allSuperInterfaces = ElementUtil.getAllSuperInterfaces(participant);
                                if (participant instanceof Interface) {
                                    allSuperInterfaces.add((Interface) participant);
                                }
                                for (Interface participantInterface : allSuperInterfaces) {
                                    if (ElementUtil.isTypeOf(classElement, participantInterface)) {
                                        continue root;
                                    } else if (!contains && aggregatedElements.contains(participantInterface)) {
                                        contains = true;
                                    }
                                }
                            }
                        }
                        if (contains) {
                            abstractionClasses.add(classElement);
                            abstractionClasses.addAll(ElementUtil.getAllSubElements(classElement));
                            break;
                        }
                    }
                }
            }
        }
        if (abstractionClasses.isEmpty()) {
            Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
            List<Class> classByName = architecture.findClassByName(algorithmFamily.getNameCapitalized() + "Abstraction");
            if (classByName != null) {
                abstractionClasses.add(classByName.get(0));
                abstractionClasses.addAll(ElementUtil.getAllSubElements(classByName.get(0)));
            }
        }
        return abstractionClasses;
    }

    /**
     * Creates the abstraction classes.
     *
     * @param algorithmFamily the algorithm family
     * @return the of abstraction classes created
     */
    public static List<Element> createAbstractionClasses(AlgorithmFamily algorithmFamily) {
        List<Element> abstractionClasses = new ArrayList<>();
        List<Element> participants = algorithmFamily.getParticipants();
        if (participants != null && !participants.isEmpty()) {
            try {
                Package aPackage = null;
                Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

                Class abstractClass;
                Class concreteClass;
                List<Element> elements;

                String namespace = ElementUtil.getNameSpace(participants);
                String packageName = UtilResources.extractPackageName(namespace);

                boolean inArchitecture = packageName.equalsIgnoreCase("model");
                if (inArchitecture) {
                    abstractClass = architecture.createClass(algorithmFamily.getNameCapitalized() + "Abstraction", true);
                    concreteClass = architecture.createClass(algorithmFamily.getNameCapitalized() + "AbstractionImpl", false);

                    architecture.removeClass(abstractClass);
                    architecture.removeClass(concreteClass);

                    elements = Collections.unmodifiableList(new ArrayList<>(architecture.getElements()));
                } else {
                    aPackage = architecture.findPackageByName(packageName);

                    abstractClass = aPackage.createClass(algorithmFamily.getNameCapitalized() + "Abstraction", true);
                    concreteClass = aPackage.createClass(algorithmFamily.getNameCapitalized() + "AbstractionImpl", false);

                    aPackage.removeClass(abstractClass);
                    aPackage.removeClass(concreteClass);

                    elements = Collections.unmodifiableList(new ArrayList<>(aPackage.getElements()));
                }

                RelationshipUtil.createNewGeneralizationRelationship(concreteClass, abstractClass);

                List<Method> abstractMethods = MethodUtil.createMethodsFromSetOfElements(participants);
                List<Method> concreteMethods = MethodUtil.createMethodsFromSetOfElements(participants);
                for (int i = 0; i < abstractMethods.size(); i++) {
                    Method abstractMethod = abstractMethods.get(i);
                    abstractMethod.setAbstract(true);
                    abstractClass.addExternalMethod(abstractMethod);

                    Method concreteMethod = concreteMethods.get(i);
                    concreteClass.addExternalMethod(concreteMethod);
                }

                for (Concern concern : ElementUtil.getOwnAndMethodsConcerns(participants)) {
                    if (!abstractClass.containsConcern(concern)) {
                        try {
                            abstractClass.addConcern(concern.getName());
                            concreteClass.addConcern(concern.getName());
                        } catch (ConcernNotFoundException ex) {
                            Logger.getLogger(BridgeUtil.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                abstractClass.setNamespace(namespace);
                concreteClass.setNamespace(namespace);

                int count = 1;
                String abstractName = abstractClass.getName();
                while (elements.contains(abstractClass)) {
                    count++;
                    abstractClass.setName(abstractName + count);
                }

                count = 1;
                String concreteName = concreteClass.getName();
                while (elements.contains(concreteClass)) {
                    count++;
                    concreteClass.setName(concreteName + count);
                }

                if (inArchitecture) {
                    architecture.addExternalClass(abstractClass);
                    architecture.addExternalClass(concreteClass);
                } else if (aPackage != null) {
                    aPackage.addExternalClass(abstractClass);
                    aPackage.addExternalClass(concreteClass);
                }
                abstractionClasses.add(abstractClass);
                abstractionClasses.add(concreteClass);
            } catch (Exception ex) {
                Logger.getLogger(BridgeUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return abstractionClasses;
    }

    /**
     * Creates the implementation interface.
     *
     * @param concern the concern
     * @param elements the elements
     * @return the interface created
     */
    public static Interface createImplementationInterface(Concern concern, List<Element> elements) {
        Interface anInterface = null;
        if (elements != null && !elements.isEmpty()) {
            Package aPackage = null;
            Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
            List<Element> tempElements;
            String namespace = ElementUtil.getNameSpace(elements);
            String packageName = UtilResources.extractPackageName(namespace);
            boolean inArchitecture = packageName.equalsIgnoreCase("model");
            if (inArchitecture) {
                anInterface = architecture.createInterface((concern != null ? Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1) : "Default") + "Implementation");
                architecture.removeInterface(anInterface);

                tempElements = Collections.unmodifiableList(new ArrayList<>(architecture.getElements()));
            } else {
                aPackage = architecture.findPackageByName(UtilResources.extractPackageName(namespace));

                anInterface = aPackage.createInterface((concern != null ? Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1) : "Default") + "Implementation");
                aPackage.removeInterface(anInterface);

                tempElements = Collections.unmodifiableList(new ArrayList<>(aPackage.getElements()));
            }
            List<Method> methodsFromSetOfElements = MethodUtil.createMethodsFromSetOfElementsByConcern(elements, concern);
            for (Method method : methodsFromSetOfElements) {
                anInterface.addExternalOperation(method);
            }
            if (concern != null) {
                try {
                    anInterface.addConcern(concern.getName());
                } catch (ConcernNotFoundException ex) {
                    Logger.getLogger(BridgeUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            anInterface.setNamespace(namespace);
            int count = 1;
            String name = anInterface.getName();
            while (tempElements.contains(anInterface)) {
                count++;
                anInterface.setName(name + count);
            }
            if (inArchitecture) {
                architecture.addExternalInterface(anInterface);
            } else if (aPackage != null) {
                aPackage.addExternalInterface(anInterface);
            }
        }
        return anInterface;
    }

    /**
     * Aggregate abstraction with implementation.
     *
     * @param abstractClass the abstract class
     * @param concernInterface the concern interface
     */
    public static void aggregateAbstractionWithImplementation(Element abstractClass, Interface concernInterface) {
        if (!ElementUtil.getAllAggregatedElements(abstractClass).contains(concernInterface)) {
            AssociationRelationship aggregation = RelationshipUtil.createNewAggregationRelationship("aggregatedImplementation", abstractClass, concernInterface);
            AssociationEnd end1 = aggregation.getParticipants().get(0);
            end1.setMultiplicity(new Multiplicity("0", "1"));
            AssociationEnd end2 = aggregation.getParticipants().get(0);
            end2.setMultiplicity(new Multiplicity("1", "1"));
        }
    }

}
