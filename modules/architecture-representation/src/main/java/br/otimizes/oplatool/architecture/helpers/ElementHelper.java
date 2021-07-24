package br.otimizes.oplatool.architecture.helpers;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

import java.util.*;

/**
 * Element utils
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public abstract class ElementHelper {

    private static final Map<String, EClass> types = new HashMap<String, EClass>();

    static {
        types.put("class", UMLPackage.Literals.CLASS);
        types.put("interface", UMLPackage.Literals.INTERFACE);
        types.put("association", UMLPackage.Literals.ASSOCIATION);
        types.put("dependency", UMLPackage.Literals.DEPENDENCY);
        types.put("comment", UMLPackage.Literals.COMMENT);
        types.put("property", UMLPackage.Literals.PROPERTY);
        types.put("operation", UMLPackage.Literals.OPERATION);
        types.put("package", UMLPackage.Literals.PACKAGE);
        types.put("realization", UMLPackage.Literals.REALIZATION);
        types.put("abstraction", UMLPackage.Literals.ABSTRACTION);
        types.put("AssociationClass", UMLPackage.Literals.ASSOCIATION_CLASS);
        types.put("usage", UMLPackage.Literals.USAGE);
        types.put("connector", UMLPackage.Literals.LINK_ACTION);
    }

    /**
     * Returns all elements of the given type.
     *
     * @param model element
     * @param type type
     * @param <T> type of element
     * @return list of element
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getAllElementsByType(NamedElement model, String type) {
        EList<Element> ownedElements = model.getOwnedElements();
        List<T> elements = new ArrayList<T>();

        for (Element e : ownedElements) {
            if (("interface".equalsIgnoreCase(type) && (e instanceof ClassImpl)))
                if (ModelElementHelper.isInterface((NamedElement) e)) elements.add((T) e);
            if (e.eClass().equals(getLiteralType(type))) elements.add((T) e);
        }

        if (elements.isEmpty()) return Collections.emptyList();
        return elements;
    }

    private static EClass getLiteralType(String type) {
        return types.get(type);
    }

}
