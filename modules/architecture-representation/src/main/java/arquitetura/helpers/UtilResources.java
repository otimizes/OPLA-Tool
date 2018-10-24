package arquitetura.helpers;

import arquitetura.representation.Element;
import arquitetura.representation.relationship.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class UtilResources {

    public static String getUrlToModel(String modelName) {
        return "src/test/java/resources/" + modelName + ".uml";
    }

    public static String capitalize(String original) {
        original = original.trim().toLowerCase();
        if (original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends T> List<U> filter(Set<T> target, Predicate<T> predicate) {
        List<U> result = new ArrayList<U>();
        for (T element : target)
            if (predicate.apply(element))
                result.add((U) element);
        return result;
    }

    /**
     * Retorna somente o nome do elemento dado o namesapce
     * <p>
     * Ex: model::Package1, reotnra Package1
     *
     * @param klass
     * @return
     */
    public static String extractPackageName(String namespace) {
        if (namespace == null) return "";
        if (namespace.equalsIgnoreCase("model")) return namespace;
        String name = namespace.substring(namespace.lastIndexOf("::") + 2, namespace.length());
        return name != null ? name : "";
    }

    public static String getRandonUUID() {
        return UUID.randomUUID().toString();
    }

    public static String createNamespace(String architectureName, String name) {
        String nsp = architectureName + "::" + name;
        return nsp != null ? nsp : "";
    }

    public static void clearConsole() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows"))
                Runtime.getRuntime().exec("cls");
            else
                Runtime.getRuntime().exec("clear");
        } catch (Exception exception) {
        }
    }

    public static String detailLogRelationship(Relationship r) {
        String strLog = "";

        if (r instanceof GeneralizationRelationship) {
            strLog += "Superclasse:" + ((GeneralizationRelationship) r).getParent().getName() + " subclasses: " + childreenToStr(((GeneralizationRelationship) r).getAllChildrenForGeneralClass());
        }
        if (r instanceof AssociationRelationship) {
            String participantsStr = participantsToStr(((AssociationRelationship) r).getParticipants());
            strLog += "Participants: " + participantsStr;
        }

        if (r instanceof AssociationClassRelationship) {
            strLog += "AssociationClass: " + ((AssociationClassRelationship) r).getAssociationClass().getName() + ". MemebersEnd: " + memebersEndToStr(((AssociationClassRelationship) r).getMemebersEnd());
        }

        return strLog;
    }

    private static String memebersEndToStr(List<MemberEnd> memebersEnd) {
        String membersEnd = "";
        for (MemberEnd member : memebersEnd)
            membersEnd += member.getType().getName() + " ";
        return membersEnd.trim();
    }

    private static String participantsToStr(List<AssociationEnd> participants) {
        String participantsStr = "";
        for (AssociationEnd element : participants)
            participantsStr += element.getCLSClass().getName() + " ";
        return participantsStr.trim();
    }

    private static String childreenToStr(Set<Element> allChildrenForGeneralClass) {
        String childreen = "";
        for (Element element : allChildrenForGeneralClass)
            childreen += element.getName() + " ";
        return childreen.trim();
    }

}