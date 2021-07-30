package br.otimizes.oplatool.architecture.helpers;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model element utils
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ModelElementHelper {

    /**
     * Returns all stereotypes of a given Element. <br />
     * <p>
     * If no stereotype is found returns an empty list.
     *
     * @param element element
     * @return List<{ @ Stereotype link }>
     */
    public static <T> List<Stereotype> getAllStereotypes(NamedElement element) {
        List<Stereotype> stereotypes = new ArrayList<>(element.getAppliedStereotypes());
        if (element instanceof Class) {
            EList<Comment> comments = ((Class) element).getPackage().getOwnedComments();
            for (Comment comment : comments)
                stereotypes.addAll(comment.getAppliedStereotypes());
        }
        if (stereotypes.isEmpty()) return Collections.emptyList();
        return stereotypes;
    }

    /**
     * Checks if a class is an interface.
     *
     * @param element element
     * @return boolean
     */
    public static boolean isInterface(NamedElement element) {
        return StereotypeHelper.hasStereotype(element, StereotypesTypes.INTERFACE);
    }
}