package br.otimizes.oplatool.patterns.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.otimizes.oplatool.patterns.designpatterns.Adapter;
import org.apache.commons.collections4.CollectionUtils;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.patterns.comparators.SubElementsComparator;
import br.otimizes.oplatool.patterns.list.MethodArrayList;

/**
 * The Class ElementUtil.
 */
public class ElementUtil {

    /**
     * Instantiates a new element util.
     */
    private ElementUtil() {
    }

    /**
     * Gets the relationships.
     *
     * @param element the element
     * @return the relationships
     */
    public static List<Relationship> getRelationships(Element element) {
        ArrayList<Relationship> relationships = new ArrayList<>();
        if (element instanceof Class) {
            relationships.addAll(((Class) element).getRelationships());
        } else if (element instanceof Interface) {
            relationships.addAll(((Interface) element).getRelationships());
        }
        return relationships;
    }

    /**
     * Gets the relationships.
     *
     * @param elements the elements
     * @return the relationships
     */
    public static List<Relationship> getRelationships(List<? extends Element> elements) {
        ArrayList<Relationship> relationships = new ArrayList<>();
        for (Element element : elements) {
            Set<Relationship> tempRelationships;
            if (element instanceof Class) {
                tempRelationships = ((Class) element).getRelationships();
            } else {
                tempRelationships = ((Interface) element).getRelationships();
            }
            for (Relationship relationship : tempRelationships) {
                if (!relationships.contains(relationship)) {
                    relationships.add(relationship);
                }
            }
        }
        return relationships;
    }

    /**
     * Checks if is type of.
     *
     * @param child the child
     * @param parent the parent
     * @return true, if is type of
     */
    public static boolean isTypeOf(Element child, Element parent) {
        boolean isType = false;
        for (Relationship relationship : ElementUtil.getRelationships(child)) {
            Element tempParent = RelationshipUtil.getImplementedInterface(relationship);
            if (tempParent == null) {
                tempParent = RelationshipUtil.getSuperElement(relationship);
                if (tempParent == null) {
                    continue;
                }
            }
            if (!tempParent.equals(child)) {
                if (tempParent.equals(parent)) {
                    isType = true;
                } else {
                    isType = isTypeOf(tempParent, parent);
                }
            }
        }
        return isType;
    }

    /**
     * Gets the all super interfaces.
     *
     * @param child the child
     * @return the list of all super interfaces
     */
    public static List<Interface> getAllSuperInterfaces(Element child) {
        List<Interface> implementedInterfaces = new ArrayList<>();
        for (Relationship relationship : ElementUtil.getRelationships(child)) {
            Interface tempInterface = RelationshipUtil.getImplementedInterface(relationship);
            if (tempInterface != null && !tempInterface.equals(child)) {
                implementedInterfaces.add(tempInterface);
                List<Interface> parentInterfaces = getAllSuperInterfaces(tempInterface);
                for (Interface parentInterface : parentInterfaces) {
                    if (!implementedInterfaces.contains(parentInterface)) {
                        implementedInterfaces.add(parentInterface);
                    }
                }
                List<Element> parentSuperTypes = getAllExtendedElements(tempInterface);
                for (Element parentSuperType : parentSuperTypes) {
                    if (parentSuperType instanceof Interface && !implementedInterfaces.contains(parentSuperType)) {
                        implementedInterfaces.add((Interface) parentSuperType);
                    }
                }
            }
        }
        List<Element> allExtendedElements = getAllExtendedElements(child);
        for (Element extendedElement : allExtendedElements) {
            List<Interface> parentInterfaces = getAllSuperInterfaces(extendedElement);
            for (Interface parentInterface : parentInterfaces) {
                if (!implementedInterfaces.contains(parentInterface)) {
                    implementedInterfaces.add(parentInterface);
                }
            }
            if (child instanceof Interface && extendedElement instanceof Interface) {
                if (!implementedInterfaces.contains(extendedElement)) {
                    implementedInterfaces.add((Interface) extendedElement);
                }
            }
        }
        return implementedInterfaces;
    }

    /**
     * Gets the all super interfaces.
     *
     * @param elements the elements
     * @return the list of all super interfaces
     */
    public static List<Interface> getAllSuperInterfaces(List<Element> elements) {
        List<Interface> interfaces = new ArrayList<>();
        for (Element element : elements) {
            for (Interface anInterface : getAllSuperInterfaces(element)) {
                if (!interfaces.contains(anInterface)) {
                    interfaces.add(anInterface);
                }
            }
        }
        return interfaces;
    }

    /**
     * Gets the all common interfaces.
     *
     * @param participants the participants
     * @return the list of all common interfaces
     */
    public static List<Interface> getAllCommonInterfaces(List<Element> participants) {
        List<Interface> interfaces = new ArrayList<>();
        boolean first = true;
        for (Element participant : participants) {
            List<Interface> elementInterfaces = ElementUtil.getAllSuperInterfaces(participant);
            if (first) {
                interfaces.addAll(CollectionUtils.union(elementInterfaces, AdapterUtil.getAllTargetInterfaces(participant)));
                if (participant instanceof Interface) {
                    interfaces.add((Interface) participant);
                }
                first = false;
            } else {
                elementInterfaces = new ArrayList<>(CollectionUtils.union(elementInterfaces, AdapterUtil.getAllTargetInterfaces(participant)));
                if (participant instanceof Interface) {
                    elementInterfaces.add((Interface) participant);
                }
                interfaces = new ArrayList<>(CollectionUtils.intersection(interfaces, elementInterfaces));
            }
        }
        return interfaces;
    }

    /**
     * Gets the all common super interfaces.
     *
     * @param participants the participants
     * @return the list of all common super interfaces
     */
    public static List<Interface> getAllCommonSuperInterfaces(List<Element> participants) {
        List<Interface> interfaces = new ArrayList<>();
        for (Element participant : participants) {
            List<Interface> elementInterfaces = ElementUtil.getAllSuperInterfaces(participant);
            if (interfaces.isEmpty()) {
                interfaces.addAll(elementInterfaces);
            } else {
                interfaces = new ArrayList<>(CollectionUtils.intersection(interfaces, elementInterfaces));
            }
        }
        return interfaces;
    }

    /**
     * Gets the all extended elements.
     *
     * @param child the child
     * @return the list of all extended elements
     */
    public static List<Element> getAllExtendedElements(Element child) {
        List<Element> extendedElements = new ArrayList<>();
        for (Relationship relationship : ElementUtil.getRelationships(child)) {
            Element tempParent = RelationshipUtil.getSuperElement(relationship);
            if (tempParent != null && !tempParent.equals(child)) {
                extendedElements.add(tempParent);
                List<Element> parentSuperTypes = getAllExtendedElements(tempParent);
                for (Element parentSuperType : parentSuperTypes) {
                    if (!extendedElements.contains(parentSuperType)) {
                        extendedElements.add(parentSuperType);
                    }
                }
            }
        }
        return extendedElements;
    }

    /**
     * Gets the all sub elements.
     *
     * @param parent the parent
     * @return the list of all sub elements
     */
    public static List<Element> getAllSubElements(Element parent) {
        List<Element> subElements = new ArrayList<>();
        for (Relationship relationship : ElementUtil.getRelationships(parent)) {
            Element tempChild = RelationshipUtil.getSubElement(relationship);
            if (tempChild != null && !tempChild.equals(parent)) {
                subElements.add(tempChild);
                List<Element> subElementSubTypes = getAllSubElements(tempChild);
                for (Element subElementSubType : subElementSubTypes) {
                    if (!subElements.contains(subElementSubType)) {
                        subElements.add(subElementSubType);
                    }
                }
            }
        }
        return subElements;
    }

    /**
     * Checks if is class or interface.
     *
     * @param element the element
     * @return true, if is class or interface
     */
    public static boolean isClassOrInterface(Element element) {
        return (element instanceof Class || element instanceof Interface);
    }

    /**
     * Gets the own and methods common concerns.
     *
     * @param elements the elements
     * @return the set of own and methods common concerns
     */
    public static Set<Concern> getOwnAndMethodsCommonConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(getOwnAndMethodsConcerns(elements.get(0)));
        for (Element participant : elements) {
            commonConcerns = new HashSet<>(CollectionUtils.intersection(commonConcerns, getOwnAndMethodsConcerns(participant)));
        }
        return commonConcerns;
    }

    /**
     * Gets the own and methods common concerns of at least two elements.
     *
     * @param elements the elements
     * @return the set of own and methods common concerns of at least two elements
     */
    public static Set<Concern> getOwnAndMethodsCommonConcernsOfAtLeastTwoElements(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element iElement : elements) {
            concernLoop:
            for (Concern concern : getOwnAndMethodsConcerns(iElement)) {
                if (!commonConcerns.contains(concern)) {
                    for (Element jElement : elements) {
                        if (!jElement.equals(iElement)) {
                            if (getOwnAndMethodsConcerns(jElement).contains(concern)) {
                                commonConcerns.add(concern);
                                continue concernLoop;
                            }
                        }
                    }
                }
            }
        }
        return commonConcerns;
    }

    /**
     * Gets the own and methods concerns.
     *
     * @param elements the elements
     * @return the set of own and methods concerns
     */
    public static Set<Concern> getOwnAndMethodsConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element participant : elements) {
            commonConcerns.addAll(getOwnAndMethodsConcerns(participant));
        }
        return commonConcerns;
    }

    /**
     * Gets the own and methods concerns.
     *
     * @param element the element
     * @return the set of own and methods concerns
     */
    public static Set<Concern> getOwnAndMethodsConcerns(Element element) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(element.getOwnConcerns());
        for (Method method : MethodUtil.getAllMethodsFromElement(element)) {
            commonConcerns.addAll(method.getOwnConcerns());
        }
        return commonConcerns;
    }

    /**
     * Gets the all aggregated elements.
     *
     * @param element the element
     * @return the set of all aggregated elements
     */
    public static Set<Element> getAllAggregatedElements(Element element) {
        Set<Element> aggregatedElements = new HashSet<>();
        for (Relationship relationship : ElementUtil.getRelationships(element)) {
            if (relationship instanceof AssociationRelationship) {
                AssociationRelationship association = (AssociationRelationship) relationship;
                for (AssociationEnd end : association.getParticipants()) {
                    if (end.isAggregation() && !end.getCLSClass().equals(element)) {
                        aggregatedElements.add(end.getCLSClass());
                    }
                }
            }
        }
        return aggregatedElements;
    }

    /**
     * Group elements by concern.
     *
     * @param elements the elements
     * @return the elements by concern hash map
     */
    public static HashMap<Concern, List<Element>> groupElementsByConcern(List<Element> elements) {
        HashMap<Concern, List<Element>> groupedElements = new HashMap<>();
        Set<Concern> ownAndMethodsCommonConcerns = getOwnAndMethodsConcerns(elements);
        for (Concern concern : ownAndMethodsCommonConcerns) {
            List<Element> concernElements = new ArrayList<>();
            for (Element element : elements) {
                Set<Concern> elementConcerns = getOwnAndMethodsConcerns(element);
                if (elementConcerns.contains(concern)) {
                    concernElements.add(element);
                }
            }
            groupedElements.put(concern, concernElements);
        }
        ArrayList<Element> nullList = ElementUtil.getElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns(elements);
        if (!nullList.isEmpty()) {
            groupedElements.put(null, nullList);
        }
        return groupedElements;
    }

    /**
     * Gets the elements with no own concerns and with at least one method with no concerns.
     *
     * @param elements the elements
     * @return the elements with no own concerns and with at least one method with no concerns
     */
    public static ArrayList<Element> getElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns(Iterable<Element> elements) {
        ArrayList<Element> nullArrayList = new ArrayList<>();
        elementLoop:
        for (Element element : elements) {
            if (element.getOwnConcerns().isEmpty()) {
                for (Method method : MethodUtil.getAllMethodsFromElement(element)) {
                    if (method.getOwnConcerns().isEmpty()) {
                        nullArrayList.add(element);
                        continue elementLoop;
                    }
                }
            }
        }
        return nullArrayList;
    }

    /**
     * Gets the name space.
     *
     * @param elements the elements
     * @return the name space
     */
    public static String getNameSpace(List<Element> elements) {
        HashMap<String, Integer> namespaceList = new HashMap<>();
        for (Element element : elements) {
            Integer namespaceCount = namespaceList.get(element.getNamespace());
            namespaceList.put(element.getNamespace(), namespaceCount == null ? 1 : namespaceCount + 1);
        }

        Integer max = -1;
        String namespace = "";
        for (Map.Entry<String, Integer> entry : namespaceList.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value > max) {
                max = value;
                namespace = key;
            }
        }
        return namespace;
    }

    /**
     * Implement interface.
     *
     * @param elements the elements
     * @param anInterface the an interface
     * @param adapterList the adapter list
     * @param adapteeList the adaptee list
     */
    public static void implementInterface(List<Element> elements, Interface anInterface, List<Element> adapterList, List<Element> adapteeList) {
        Collections.sort(elements, SubElementsComparator.getDescendingOrderer());
        for (Element participant : elements) {
            Class adapterClass = implementInterface(participant, anInterface);
            if (adapterClass != null) {
                if (!adapterList.contains(adapterClass)) {
                    adapterList.add(adapterClass);
                }
                if (!adapteeList.contains(participant)) {
                    adapteeList.add(participant);
                }
            }
        }
    }

    /**
     * Implement interface.
     *
     * @param child the child
     * @param anInterface the an interface
     * @return representation class
     */
    public static Class implementInterface(Element child, Interface anInterface) {
        if (!ElementUtil.isTypeOf(child, anInterface) && !AdapterUtil.getAllTargetInterfaces(child).contains(anInterface)) {
            if (child instanceof Class) {
                RelationshipUtil.createNewRealizationRelationship("implements", child, anInterface);
            } else if (child instanceof Interface) {
                return Adapter.getInstance().applyAdapter(anInterface, child);
            }
        }

        if (ElementUtil.isTypeOf(child, anInterface) && child instanceof Class) {
            Class childClass = (Class) child;
            if (!childClass.isAbstract()) {
                MethodArrayList childMethods = new MethodArrayList(new ArrayList<>(childClass.getAllMethods()));
                for (Method interfaceMethod : anInterface.getMethods()) {
                    if (!childMethods.containsSameSignature(interfaceMethod)) {
                        childClass.addExternalMethod(MethodUtil.cloneMethod(interfaceMethod));
                    }
                }
            }
        } else if (AdapterUtil.getAllTargetInterfaces(child).contains(anInterface)) {
            return AdapterUtil.getAdapterClass(anInterface, child);
        }
        return null;
    }

    /**
     * Extend class.
     *
     * @param child the child
     * @param aClass the a class
     * @return representation class
     */
    public static Class extendClass(Element child, Class aClass) {
        if (!ElementUtil.isTypeOf(child, aClass)) {
            if (child instanceof Class) {
                RelationshipUtil.createNewGeneralizationRelationship(child, aClass);
            } else if (child instanceof Interface) {
                Class adapterClass = Adapter.getInstance().applyAdapter(aClass, child);
                return adapterClass;
            }
        }
        if (child instanceof Class) {
            Class childClass = (Class) child;
            if (!childClass.isAbstract()) {
                MethodArrayList childMethods = new MethodArrayList(new ArrayList<>(childClass.getAllMethods()));
                for (Method classMethod : aClass.getAllMethods()) {
                    if (!childMethods.containsSameSignature(classMethod)) {
                        Method cloneMethod = MethodUtil.cloneMethod(classMethod);
                        cloneMethod.setAbstract(false);
                        childClass.addExternalMethod(cloneMethod);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Verify and remove required interface.
     *
     * @param client the client
     * @param supplier the supplier
     */
    public static void verifyAndRemoveRequiredInterface(Element client, Element supplier) {
        if (client instanceof Class && supplier instanceof Interface) {
            for (Relationship relationship : ElementUtil.getRelationships(client)) {
                Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
                if (supplier.equals(usedElementFromRelationship)) {
                    return;
                }
            }
            ((Class) client).removeRequiredInterface((Interface) supplier);
        }
    }

    /**
     * Verify and remove implemented interface.
     *
     * @param client the client
     * @param supplier the supplier
     */
    public static void verifyAndRemoveImplementedInterface(Element client, Element supplier) {
        if (client instanceof Class && supplier instanceof Interface) {
            if (!getAllSuperInterfaces(client).contains(supplier)) {
                ((Class) client).removeImplementedInterface((Interface) supplier);
            }
        }
    }

    /**
     * Adds the required interface.
     *
     * @param client the client
     * @param supplier the supplier
     */
    public static void addRequiredInterface(Element client, Element supplier) {
        if (client instanceof Class && supplier instanceof Interface) {
            ((Class) client).addRequiredInterface((Interface) supplier);
        }
    }

    /**
     * Adds the implemented interface.
     *
     * @param client the client
     * @param supplier the supplier
     */
    public static void addImplementedInterface(Element client, Element supplier) {
        if (client instanceof Class && supplier instanceof Interface) {
            ((Class) client).addImplementedInterface((Interface) supplier);
        }
    }

    /**
     * Gets the all super elements.
     *
     * @param element the element
     * @return the list with all super elements
     */
    public static List<Element> getAllSuperElements(Element element) {
        return new ArrayList<>(CollectionUtils.union(getAllExtendedElements(element), getAllSuperInterfaces(element)));
    }

    /**
     * Gets the chain of related elements with same concern.
     *
     * @param mainElements the main elements
     * @param concern the concern
     * @return the chain of related elements with same concern
     */
    public static List<Element> getChainOfRelatedElementsWithSameConcern(List<Element> mainElements, Concern concern) {
        List<Element> elements = new ArrayList<>();
        for (Element element : mainElements) {
            if ((element instanceof Class || element instanceof Interface)
                    && !elements.contains(element)) {
                List<Element> tempElements = new ArrayList<>();
                if (!tempElements.contains(element) && element.getAllConcerns().contains(concern)) {
                    tempElements.add(element);
                    getChainOfRelatedElementsWithSameConcern(element, concern, tempElements, mainElements);
                }

                if (tempElements.size() > elements.size()) {
                    elements = tempElements;
                }
            }
        }
        return elements;
    }

    /**
     * Gets the chain of related elements with same concern.
     *
     * @param element the element
     * @param concern the concern
     * @param elements the elements
     * @param mainElements the main elements
     * @return the chain of related elements with same concern
     */
    private static void getChainOfRelatedElementsWithSameConcern(Element element, Concern concern, List<Element> elements, List<Element> mainElements) {
        List<Relationship> relationships = getRelationships(element);
        for (Relationship relationship : relationships) {
            Element chainedElement = RelationshipUtil.getUsedElementFromRelationship(relationship);
            if (element.equals(chainedElement)) {
                chainedElement = RelationshipUtil.getClientElementFromRelationship(relationship);
            } else if (chainedElement == null) {
                chainedElement = RelationshipUtil.getSubElement(relationship);
                if (element.equals(chainedElement)) {
                    chainedElement = RelationshipUtil.getSuperElement(relationship);
                    if (chainedElement == null || element.equals(chainedElement)) {
                        chainedElement = RelationshipUtil.getImplementedInterface(relationship);
                    }
                }
            }
            if (chainedElement != null
                    && (chainedElement instanceof Class || chainedElement instanceof Interface)
                    && !chainedElement.equals(element)
                    && chainedElement.getAllConcerns().contains(concern)
                    && !elements.contains(chainedElement)
                    && mainElements.contains(chainedElement)) {
                elements.add(chainedElement);
                getChainOfRelatedElementsWithSameConcern(chainedElement, concern, elements, mainElements);
            }

        }
    }

    /**
     * Gets the applied design patterns.
     *
     * @param element the element
     * @return the set of applied design patterns
     */
    public static Set<String> getAppliedDesignPatterns(Element element) {
        if (element instanceof Class) {
            Class elementClass = (Class) element;
            return elementClass.getPatternsOperations().getAllPatterns();
        } else if (element instanceof Interface) {
            Interface elementInterface = (Interface) element;
            return elementInterface.getPatternsOperations().getAllPatterns();
        } else {
            return new HashSet<>();
        }
    }

}
