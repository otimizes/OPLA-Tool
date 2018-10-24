package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.*;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceUtil {

    private InterfaceUtil() {
    }

    public static Interface createInterfaceForSetOfElements(String interfaceName, List<Element> participants) {
        Interface anInterface = null;
        if (participants != null && !participants.isEmpty()) {
            arquitetura.representation.Package aPackage = null;
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
                anInterface.setName(name + Integer.toString(count));
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
