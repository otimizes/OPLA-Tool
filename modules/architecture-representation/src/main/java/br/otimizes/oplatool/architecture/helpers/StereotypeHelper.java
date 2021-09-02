package br.otimizes.oplatool.architecture.helpers;

import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.ModelIncompleteException;
import br.otimizes.oplatool.architecture.exceptions.ModelNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.SMartyProfileNotAppliedToModelException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.EnumerationLiteralImpl;
import org.eclipse.uml2.uml.internal.impl.StereotypeImpl;

import java.util.*;

/**
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class StereotypeHelper {

    public static boolean hasStereotype(NamedElement elt, String stereotypeName) {
        boolean has = false;
        if (elt != null) {
            Iterator<Stereotype> i = elt.getAppliedStereotypes().iterator();
            Stereotype currentStereotype;
            while (i.hasNext() && !has) {
                currentStereotype = i.next();
                if (currentStereotype.getName().equalsIgnoreCase(stereotypeName))
                    has = true;
            }
        }
        return has;
    }

    public static boolean isVariationPoint(NamedElement element) {
        return element.getName().equalsIgnoreCase(StereotypesTypes.VARIATION_POINT);
    }

    public static List<Comment> getCommentVariability(NamedElement element) {
        List<Comment> belongsComments = new ArrayList<>();
        EList<Comment> comments = ((Class) element).getPackage().getOwnedComments();
        for (Comment comment : comments) {
            if (comment.getAnnotatedElements().contains(element)) {
                for (Stereotype stereotype : comment.getAppliedStereotypes())
                    if (stereotype.getName().equalsIgnoreCase("variability")) {
                        belongsComments.add(comment);
                    }
            }
        }

        return belongsComments;
    }

    public static boolean isVariability(NamedElement element) {
        int s = getCommentVariability(element).size();
        return s > 0;
    }

    public static boolean hasConcern(NamedElement element) {
        try {
            if (element instanceof ClassImpl) if (searchForConcernsStereotypes(element) != null) return true;
            if (element instanceof StereotypeImpl)
                if (((Classifier) element).getGeneralizations().get(0).getGeneral().getName().equalsIgnoreCase(StereotypesTypes.CONCERN))
                    return true;
        } catch (Exception e) {
            return hasStereotype(element, StereotypesTypes.CONCERN);
        }
        return false;
    }

    public static List<String> getAllRelationshipStereotypes(org.eclipse.uml2.uml.Relationship element) {
        List<String> stereotypes = new ArrayList<>();
        EList<Stereotype> stes = element.getAppliedStereotypes();
        for (Stereotype stereotype : stes) {
            if (stereotype instanceof StereotypeImpl)
                if (!stereotype.getAllExtendedMetaclasses().isEmpty())
                    if (stereotype.getAllExtendedMetaclasses().get(0).getName().equalsIgnoreCase(StereotypesTypes.RELATIONSHIP))
                        stereotypes.add(stereotype.getName());

        }
        return stereotypes;
    }

    public static Set<String> getAllPatternsStereotypes(NamedElement element) {
        Set<String> stereotypes = new HashSet<>();
        EList<Stereotype> appliedStereotypes = element.getAppliedStereotypes();
        for (Stereotype stereotype : appliedStereotypes) {
            if (isPatternStereotype(stereotype))
                stereotypes.add(stereotype.getName());
        }

        return stereotypes;
    }

    private static boolean isPatternStereotype(Stereotype stereotype) {
        if (!stereotype.getGeneralizations().isEmpty()) {
            return stereotype.getGeneralizations().get(0).getGeneral().getName().equalsIgnoreCase(StereotypesTypes.PATTERN);
        }

        return false;

    }

    public static String getValueOfAttribute(Element element, Stereotype variability, String attrName) {
        Object attr = element.getValue(variability, attrName);
        if (attr != null) {
            if (attr instanceof EnumerationLiteralImpl) {
                EnumerationLiteral e = (EnumerationLiteral) element.getValue(variability, attrName);
                return e.getName().trim();
            } else if (attr instanceof ClassImpl) {
                return ((Class) attr).getName().trim();
            }
            return element.getValue(variability, attrName).toString().trim();
        } else {
            return "";
        }

    }

    public static String getConcernName(NamedElement namedElement) throws ConcernNotFoundException {
        if (hasConcern(namedElement))
            if (searchForConcernsStereotypes(namedElement) != null)
                return Objects.requireNonNull(searchForConcernsStereotypes(namedElement)).getName();

        throw new ConcernNotFoundException("There is not concern in element " + namedElement);
    }

    public static Map<String, String> getVariabilityAttributes(NamedElement namedElement, Comment comment)
            throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelException {
        if (comment != null) {
            Stereotype variability = getStereotypeByName(namedElement, "variability");
            Map<String, String> variabilityProps = new HashMap<>();

            String name = getValueOfAttribute(comment, variability, "name");
            String bidingTime = getValueOfAttribute(comment, variability, "bindingTime");
            String maxSelection = getValueOfAttribute(comment, variability, "maxSelection");
            String minSelection = getValueOfAttribute(comment, variability, "minSelection");
            String variants = getValueOfAttribute(comment, variability, "variants");
            String allowAddingVar = getValueOfAttribute(comment, variability, "allowAddingVar");

            variabilityProps.put("name", name);
            variabilityProps.put("bindingTime", bidingTime);
            variabilityProps.put("maxSelection", maxSelection);
            variabilityProps.put("minSelection", minSelection);
            variabilityProps.put("variants", variants);
            variabilityProps.put("allowAddingVar", allowAddingVar);
            if (!comment.getNearestPackage().getName().equalsIgnoreCase("model"))
                variabilityProps.put("idOwnerPackage", XmiHelper.getXmiId(comment.getOwner()));
            return variabilityProps;
        }
        return Collections.emptyMap();
    }

    public static Stereotype getStereotypeByName(NamedElement element, String stereotypeName) {
        List<Stereotype> stereotypes = ModelElementHelper.getAllStereotypes(element);
        for (Stereotype stereotype : stereotypes) {
            if (stereotypeName.equalsIgnoreCase(stereotype.getName()))
                return stereotype;
        }
        return null;
    }

    private static Stereotype searchForConcernsStereotypes(NamedElement element) {
        EList<Stereotype> appliedStereotypes = element.getAppliedStereotypes();
        for (Stereotype stereotype : appliedStereotypes) {
            if (stereotype instanceof StereotypeImpl)
                if (!stereotype.getGeneralizations().isEmpty())
                    if (stereotype.getGeneralizations().get(0).getGeneral()
                            .getName().equalsIgnoreCase(StereotypesTypes.CONCERN))
                        return stereotype;

        }
        return null;
    }

    public static Stereotype getVariantType(Classifier klass) {
        List<String> possibleVariants = Arrays.asList("mandatory", "optional", "alternative_OR", "alternative_XOR");
        List<Stereotype> stereotypes = ModelElementHelper.getAllStereotypes(klass);
        for (Stereotype stereotype : stereotypes) {
            if (possibleVariants.contains(stereotype.getName())) {
                return stereotype;
            }
        }
        return null;
    }
}