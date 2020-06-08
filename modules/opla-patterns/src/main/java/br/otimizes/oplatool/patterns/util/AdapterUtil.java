/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.patterns.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.patterns.repositories.ArchitectureRepository;
import org.apache.commons.collections4.CollectionUtils;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;

/**
 * The Class AdapterUtil.
 *
 * @author giovaniguizzo
 */
public class AdapterUtil {

    /**
     * Instantiates a new adapter util.
     */
    private AdapterUtil() {
    }

    /**
     * Gets the adapter class.
     *
     * @param target the target
     * @param adaptee the adaptee
     * @return the adapter class
     */
    public static Class getAdapterClass(Element target, Element adaptee) {
        Class adapterClass = null;
        List<Element> allTargetSubElements = ElementUtil.getAllSubElements(target);
        List<Element> allAdapteeSuperElements = ElementUtil.getAllSuperElements(adaptee);
        rootFor:
        for (Element element : allTargetSubElements) {
            if (element instanceof Class && !((Class) element).isAbstract()) {
                List<Relationship> elementRelationships = ElementUtil.getRelationships(element);
                for (Relationship relationship : elementRelationships) {
                    Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
                    if (usedElementFromRelationship != null
                            && (usedElementFromRelationship.equals(adaptee) || allAdapteeSuperElements.contains(usedElementFromRelationship))) {
                        adapterClass = (Class) element;
                        break rootFor;
                    }
                }
            }
        }
        if (adapterClass == null) {
            Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
            List<Class> classByName = architecture.findClassByName(adaptee.getName() + "Adapter");
            if (classByName != null) {
                adapterClass = classByName.get(0);
            }
        }
        return adapterClass;
    }

    /**
     * Gets the all target interfaces.
     *
     * @param adaptee the adaptee
     * @return the list of all target interfaces
     */
    public static List<Interface> getAllTargetInterfaces(Element adaptee) {
        List<Interface> targetInterfaces = new ArrayList<>();

        List<Element> adapteeSuperElements = ElementUtil.getAllSuperElements(adaptee);
        adapteeSuperElements.add(adaptee);
        List<Relationship> allRelationships = ElementUtil.getRelationships(adapteeSuperElements);
        for (Relationship relationship : allRelationships) {
            Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
            if (usedElementFromRelationship != null && adapteeSuperElements.contains(usedElementFromRelationship)) {
                Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
                if (client instanceof Class && !((Class) client).isAbstract()) {
                    List<Interface> allSuperInterfaces = ElementUtil.getAllSuperInterfaces(client);
                    allSuperInterfaces.remove(adaptee);
                    allSuperInterfaces.removeAll(adapteeSuperElements);
                    targetInterfaces = new ArrayList<>(CollectionUtils.union(targetInterfaces, allSuperInterfaces));
                }
            }
        }
        return targetInterfaces;
    }

    /**
     * Creates the adapter class.
     *
     * @param adaptee the adaptee
     * @return the representation class
     */
    public static Class createAdapterClass(Element adaptee) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
        Class adapterClass;
        Package aPackage = null;

        List<Element> tempElements;

        String namespace = adaptee.getNamespace();
        String packageName = UtilResources.extractPackageName(namespace);

        boolean inArchitecture = packageName.equalsIgnoreCase("model");
        if (inArchitecture) {
            adapterClass = architecture.createClass(adaptee.getName() + "Adapter", false);

            architecture.removeClass(adapterClass);

            tempElements = Collections.unmodifiableList(new ArrayList<>(architecture.getElements()));
        } else {
            aPackage = architecture.findPackageByName(packageName);
            adapterClass = aPackage.createClass(adaptee.getName() + "Adapter", false);

            aPackage.removeClass(adapterClass);

            tempElements = Collections.unmodifiableList(new ArrayList<>(aPackage.getElements()));
        }

        adapterClass.setNamespace(adaptee.getNamespace());

        int count = 1;
        String name = adapterClass.getName();
        while (tempElements.contains(adapterClass)) {
            count++;
            adapterClass.setName(name + count);
        }

        if (inArchitecture) {
            architecture.addExternalClass(adapterClass);
        } else if (aPackage != null) {
            aPackage.addExternalClass(adapterClass);
        }

        return adapterClass;
    }

}
