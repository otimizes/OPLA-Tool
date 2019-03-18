package arquitetura.helpers;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class ModelElementHelper {

    /**
     * Retorna todos os estereótipo de uma dada ELemento. <br />
     * <p>
     * Se nenhum esterótipo for encontrado retorna uma lista vazia.
     *
     * @param element
     * @return List<{@link Stereotype}>
     */
    public static <T> List<Stereotype> getAllStereotypes(NamedElement element) {
        List<Stereotype> stereotypes = new ArrayList<Stereotype>();

        for (Stereotype stereotype : element.getAppliedStereotypes())
            stereotypes.add(stereotype);

        if (element instanceof Class) {
            EList<Comment> comments = ((Class) element).getPackage().getOwnedComments();

            for (Comment comment : comments)
                for (Stereotype stereotype : comment.getAppliedStereotypes())
                    stereotypes.add(stereotype);
        }

        if (stereotypes.isEmpty()) return Collections.emptyList();

        return stereotypes;
    }

    /**
     * Verifica se uma classe é uma interface.
     *
     * @param klass
     * @return boolean
     */
    public static boolean isInterface(NamedElement klass) {
        return StereotypeHelper.hasStereotype(klass, StereotypesTypes.INTERFACE);
    }

    /**
     * Verifica se elemento é uma classe
     *
     * @param klass
     * @return {@link boolean}
     */
    public static boolean isClass(NamedElement klass) {
        if (isInterface(klass)) return false;
        return true;
    }

}