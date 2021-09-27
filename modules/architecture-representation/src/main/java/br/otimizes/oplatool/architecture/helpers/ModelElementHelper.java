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

    public static boolean isInterface(NamedElement element) {
        return StereotypeHelper.hasStereotype(element, StereotypesTypes.INTERFACE);
    }
}