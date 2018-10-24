package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Class;
import arquitetura.representation.*;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.comparators.SubElementsComparator;
import br.ufpr.inf.opla.patterns.designpatterns.Adapter;
import br.ufpr.inf.opla.patterns.list.MethodArrayList;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class ElementUtil {

    private ElementUtil() {
    }

    public static List<Relationship> getRelationships(Element element) {
        ArrayList<Relationship> relationships = new ArrayList<>();
        if (element instanceof arquitetura.representation.Class) {
            relationships.addAll(((arquitetura.representation.Class) element).getRelationships());
        } else if (element instanceof Interface) {
            relationships.addAll(((Interface) element).getRelationships());
        }
        return relationships;
    }

    public static List<Relationship> getRelationships(List<? extends Element> elements) {
        ArrayList<Relationship> relationships = new ArrayList<>();
        for (Element element : elements) {
            Set<Relationship> tempRelationships;
            if (element instanceof arquitetura.representation.Class) {
                tempRelationships = ((arquitetura.representation.Class) element).getRelationships();
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

    public static boolean isClassOrInterface(Element element) {
        return (element instanceof arquitetura.representation.Class || element instanceof Interface);
    }

    public static Set<Concern> getOwnAndMethodsCommonConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(getOwnAndMethodsConcerns(elements.get(0)));
        for (Element participant : elements) {
            commonConcerns = new HashSet<>(CollectionUtils.intersection(commonConcerns, getOwnAndMethodsConcerns(participant)));
        }
        return commonConcerns;
    }

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

    public static Set<Concern> getOwnAndMethodsConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element participant : elements) {
            commonConcerns.addAll(getOwnAndMethodsConcerns(participant));
        }
        return commonConcerns;
    }

    public static Set<Concern> getOwnAndMethodsConcerns(Element element) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(element.getOwnConcerns());
        for (Method method : MethodUtil.getAllMethodsFromElement(element)) {
            commonConcerns.addAll(method.getOwnConcerns());
        }
        return commonConcerns;
    }

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

    public static arquitetura.representation.Class implementInterface(Element child, Interface anInterface) {
        if (!ElementUtil.isTypeOf(child, anInterface) && !AdapterUtil.getAllTargetInterfaces(child).contains(anInterface)) {
            if (child instanceof arquitetura.representation.Class) {
                RelationshipUtil.createNewRealizationRelationship("implements", child, anInterface);
            } else if (child instanceof Interface) {
                return Adapter.getInstance().applyAdapter(anInterface, child);
            }
        }

        if (ElementUtil.isTypeOf(child, anInterface) && child instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class childClass = (arquitetura.representation.Class) child;
            if (!childClass.isAbstract()) {
                MethodArrayList childMethods = new MethodArrayList(new ArrayList<>(childClass.getAllMethods()));
                for (Method interfaceMethod : anInterface.getOperations()) {
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

    public static arquitetura.representation.Class extendClass(Element child, arquitetura.representation.Class aClass) {
        if (!ElementUtil.isTypeOf(child, aClass)) {
            if (child instanceof arquitetura.representation.Class) {
                RelationshipUtil.createNewGeneralizationRelationship(child, aClass);
            } else if (child instanceof Interface) {
                arquitetura.representation.Class adapterClass = Adapter.getInstance().applyAdapter(aClass, child);
                return adapterClass;
            }
        }
        if (child instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class childClass = (arquitetura.representation.Class) child;
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

    public static void verifyAndRemoveRequiredInterface(Element client, Element supplier) {
        if (client instanceof arquitetura.representation.Class && supplier instanceof Interface) {
            for (Relationship relationship : ElementUtil.getRelationships(client)) {
                Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
                if (supplier.equals(usedElementFromRelationship)) {
                    return;
                }
            }
            ((arquitetura.representation.Class) client).removeRequiredInterface((Interface) supplier);
        }
    }

    public static void verifyAndRemoveImplementedInterface(Element client, Element supplier) {
        if (client instanceof arquitetura.representation.Class && supplier instanceof Interface) {
            if (!getAllSuperInterfaces(client).contains(supplier)) {
                ((arquitetura.representation.Class) client).removeImplementedInterface((Interface) supplier);
            }
        }
    }

    public static void addRequiredInterface(Element client, Element supplier) {
        if (client instanceof arquitetura.representation.Class && supplier instanceof Interface) {
            ((arquitetura.representation.Class) client).addRequiredInterface((Interface) supplier);
        }
    }

    public static void addImplementedInterface(Element client, Element supplier) {
        if (client instanceof arquitetura.representation.Class && supplier instanceof Interface) {
            ((arquitetura.representation.Class) client).addImplementedInterface((Interface) supplier);
        }
    }

    public static List<Element> getAllSuperElements(Element element) {
        return new ArrayList<>(CollectionUtils.union(getAllExtendedElements(element), getAllSuperInterfaces(element)));
    }

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

    public static Set<String> getAppliedDesignPatterns(Element element) {
        if (element instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class elementClass = (arquitetura.representation.Class) element;
            return elementClass.getPatternsOperations().getAllPatterns();
        } else if (element instanceof Interface) {
            Interface elementInterface = (Interface) element;
            return elementInterface.getPatternsOperations().getAllPatterns();
        } else {
            return new HashSet<>();
        }
    }

}
