package arquitetura.helpers;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.EnumerationLiteralImpl;
import org.eclipse.uml2.uml.internal.impl.StereotypeImpl;

import java.util.*;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class StereotypeHelper {

    /**
     * Verifica se um dado elemento (elt) contém o estereótipo (stereotypeName ).
     *
     * @param elt
     * @param stereotypeName
     * @return boolean
     */
    public static boolean hasStereotype(NamedElement elt, String stereotypeName) {
        boolean has = false;

        if (elt != null) {
            Iterator<Stereotype> i = elt.getAppliedStereotypes().iterator();
            Stereotype currentStereotype;
            while (i.hasNext() && !has) {
                currentStereotype = (Stereotype) i.next();
                if (currentStereotype.getName().equalsIgnoreCase(stereotypeName))
                    has = true;
            }
        }

        return has;
    }

    /**
     * Verifica se um elemento é um ponto de variação.
     *
     * @param element
     * @return boolean
     */
    public static boolean isVariationPoint(NamedElement element) {
        return element.getName().equalsIgnoreCase(StereotypesTypes.VARIATION_POINT);
    }

    /**
     * Verifica se um elemento é uma variabilidade.
     * Caso Variability não for encontrada retorna null.
     *
     * @param element
     * @return boolean
     */
    public static List<Comment> getCommentVariability(NamedElement element) {
        /**
         * Como não é possível recuperar os elementos do tipo Comentário da UML2,
         * é preciso recuperar todos os comentários que existem na arquitetura
         * e por meio destes ver a qual classe o mesmo pertence.
         */
        List<Comment> belongsComments = new ArrayList<Comment>();
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

    /**
     * Verifica se um elemento é uma variabilidade.
     *
     * @param element
     * @return boolean
     */
    public static boolean isVariability(NamedElement element) {
        //return getCommentVariability(element) != null ? true : false;
        int s = getCommentVariability(element).size();
        if (s > 0)
            return true;
        else
            return false;
    }

    /**
     * Verifica se elemento possui interesse. Os intereses são definidos em um profile separado do Smarty.
     * Esse profile, contém um estereótipo chamado <b>concern</b>, que é abstrato e estende a metaclasse <b>Class</b>
     * Sendo assim os interesses que o usuário desejar usar na arquitetura devem estender do estereótipo <b>Concern</b>.<br/><br/>
     * <p>
     * <a href="https://dl.dropboxusercontent.com/u/6730822/Screen%20Shot%202013-05-17%20at%209.51.41%20AM.png">Exemplo</a>
     *
     * @param element
     * @return boolean
     */
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

    /**
     * @param element
     * @return
     */
    public static List<String> getAllRelationshipStereotypes(org.eclipse.uml2.uml.Relationship element) {
        List<String> stereotypes = new ArrayList<String>();
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
        Set<String> stereotypes = new HashSet<String>();
        EList<Stereotype> stes = element.getAppliedStereotypes();
        for (Stereotype stereotype : stes) {
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

    /**
     * Retorna o valor de um attributo de um dado estereótipo e EnumerationLiteral.
     * * @param <T>
     *
     * @param element
     * @param stereotype
     * @param attrName   name
     * @return
     */
    public static String getValueOfAttribute(Element element, Stereotype variability, String attrName) {
        Object attr = element.getValue(variability, attrName);
        if (attr != null) {
            if (attr instanceof EnumerationLiteralImpl) {
                EnumerationLiteral e = (EnumerationLiteral) element.getValue(variability, attrName);
                return e.getName().trim();
            } else if (attr instanceof ClassImpl) {
                return ((Class) attr).getName().trim();
            }
            return (String) element.getValue(variability, attrName).toString().trim();
        } else {
            return "";
        }

    }

    /**
     * Retorna o nome do concern, caso existir. Se element não possuir concern retorna ConcernNotFoundExpection.
     *
     * @param c
     * @return
     * @throws ConcernNotFoundException se concern não for encontrado
     */
    public static String getConcernName(NamedElement c) throws ConcernNotFoundException {
        if (hasConcern(c))
            if (searchForConcernsStereotypes(c) != null)
                return searchForConcernsStereotypes(c).getName();

        throw new ConcernNotFoundException("There is not concern in element " + c);
    }

    /**
     * Retorna um {@link Map} contendo os atributos/valores para atributos de um variabilidade.
     *
     * @param klass
     * @return {@link Map}
     * @throws ModelNotFoundException
     * @throws ModelIncompleteException
     * @throws SMartyProfileNotAppliedToModelExcepetion
     */
    public static Map<String, String> getVariabilityAttributes(NamedElement klass, Comment comment) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {

        if (comment != null) {

            Stereotype variability = getStereotypeByName(klass, "variability");
            Map<String, String> variabilityProps = new HashMap<String, String>();

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
            if (!comment.getNearestPackage().getName().equalsIgnoreCase("model")) // nao esta em nenhum pacote
                variabilityProps.put("idOwnerPackage", XmiHelper.getXmiId(comment.getOwner()));

            return variabilityProps;
        }

        return Collections.emptyMap();
    }

    /**
     * Dada um elemento e um nome de estereótipo retorna o estreótipo caso o mesmo exista no elemento.
     * Retorna null caso o estereótipo não exista.
     *
     * @param element
     * @param stereotypeName
     * @return
     */
    public static Stereotype getStereotypeByName(NamedElement element, String stereotypeName) {
        List<Stereotype> stereotypes = ModelElementHelper.getAllStereotypes(element);
        for (Stereotype stereotype : stereotypes) {
            if (stereotypeName.equalsIgnoreCase(stereotype.getName()))
                return stereotype;
        }

        return null;
    }


    /**
     * Busca por concern em todos os estereótipos aplicados em um elemento.
     * Retorna null caso náo exista.
     *
     * @param element
     * @return {@link Stereotype}
     */
    private static Stereotype searchForConcernsStereotypes(NamedElement element) {
        EList<Stereotype> stes = element.getAppliedStereotypes();
        for (Stereotype stereotype : stes) {
            if (stereotype instanceof StereotypeImpl)
                if (!stereotype.getGeneralizations().isEmpty())
                    if (stereotype.getGeneralizations().get(0).getGeneral()
                            .getName().equalsIgnoreCase(StereotypesTypes.CONCERN))
                        return stereotype;

        }

        return null;
    }

    public static Stereotype getVariantType(Classifier klass) {
        List<String> possibileVariants = Arrays.asList("mandatory", "optional", "alternative_OR", "alternative_XOR");

        List<Stereotype> stereotypes = ModelElementHelper.getAllStereotypes(klass);

        for (Stereotype stereotype : stereotypes) {
            if (possibileVariants.contains(stereotype.getName())) {
                return stereotype;
            }
        }

        return null;
    }


}