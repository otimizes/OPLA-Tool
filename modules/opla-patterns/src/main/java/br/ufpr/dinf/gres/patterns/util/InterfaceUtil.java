package br.ufpr.dinf.gres.patterns.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ufpr.dinf.gres.architecture.exceptions.ConcernNotFoundException;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Method;
import br.ufpr.dinf.gres.patterns.repositories.ArchitectureRepository;

/**
 * The Class InterfaceUtil.
 */
public class InterfaceUtil {

    /**
     * Instantiates a new interface util.
     */
    private InterfaceUtil() {
    }

    /**
     * Creates the interface for set of elements.
     *
     * @param interfaceName the interface name
     * @param participants the participants
     * @return the interface
     */
    public static Interface createInterfaceForSetOfElements(String interfaceName, List<Element> participants) {
        Interface anInterface = null;
        if (participants != null && !participants.isEmpty()) {
            br.ufpr.dinf.gres.architecture.representation.Package aPackage = null;
            Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
            List<Element> tempElements;
            String namespace = ElementUtil.getNameSpace(participants);
            String packageName = UtilResources.extractPackageName(namespace);
            boolean naArquitetura = packageName.equalsIgnoreCase("model");
            if (naArquitetura) {
                anInterface = architecture.createInterface(interfaceName);
                architecture.removeInterface(anInterface);

                tempElements = Collections.unmodifiableList(new ArrayList<>(architecture.getElements()));
            } else {
                aPackage = architecture.findPackageByName(UtilResources.extractPackageName(namespace));

                anInterface = aPackage.createInterface(interfaceName);
                aPackage.removeInterface(anInterface);

                tempElements = Collections.unmodifiableList(new ArrayList<>(aPackage.getElements()));
            }
            List<Method> methodsFromSetOfElements = MethodUtil.createMethodsFromSetOfElements(participants);
            for (Method method : methodsFromSetOfElements) {
                anInterface.addExternalOperation(method);
            }
            for (Concern concern : ElementUtil.getOwnAndMethodsConcerns(participants)) {
                if (!anInterface.containsConcern(concern)) {
                    try {
                        anInterface.addConcern(concern.getName());
                    } catch (ConcernNotFoundException ex) {
                        Logger.getLogger(InterfaceUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            anInterface.setNamespace(namespace);
            int count = 1;
            String name = anInterface.getName();
            while (tempElements.contains(anInterface)) {
                count++;
                anInterface.setName(name + count);
            }
            if (naArquitetura) {
                architecture.addExternalInterface(anInterface);
            } else if (aPackage != null) {
                aPackage.addExternalInterface(anInterface);
            }
        }
        return anInterface;
    }
}
