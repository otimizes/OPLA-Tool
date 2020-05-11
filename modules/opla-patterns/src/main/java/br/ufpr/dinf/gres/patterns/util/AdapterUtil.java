/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.dinf.gres.patterns.util;

import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.patterns.repositories.ArchitectureRepository;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author giovaniguizzo
 */
public class AdapterUtil {

    private AdapterUtil() {
    }

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

    public static br.ufpr.dinf.gres.architecture.representation.Class createAdapterClass(Element adaptee) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
        br.ufpr.dinf.gres.architecture.representation.Class adapterClass;
        br.ufpr.dinf.gres.architecture.representation.Package aPackage = null;

        List<Element> tempElements;

        String namespace = adaptee.getNamespace();
        String packageName = UtilResources.extractPackageName(namespace);

        boolean naArquitetura = packageName.equalsIgnoreCase("model");
        if (naArquitetura) {
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

        if (naArquitetura) {
            architecture.addExternalClass(adapterClass);
        } else if (aPackage != null) {
            aPackage.addExternalClass(adapterClass);
        }

        return adapterClass;
    }

}
