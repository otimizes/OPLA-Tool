package br.otimizes.oplatool.architecture.helpers;

import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * @author edipofederle<edipofederle @ gmail.com>
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

    public static String extractPackageName(String namespace) {
        if (namespace == null) return "";
        if (namespace.equalsIgnoreCase("model")) return namespace;
        return namespace.substring(namespace.lastIndexOf("::") + 2);
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String createNamespace(String architectureName, String name) {
        return architectureName + "::" + name;
    }

    public static void clearConsole() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows"))
                Runtime.getRuntime().exec("cls");
            else
                Runtime.getRuntime().exec("clear");
        } catch (Exception ignored) {
        }
    }

    public static String detailLogRelationship(Relationship r) {
        String strLog = "";

        if (r instanceof GeneralizationRelationship) {
            strLog += "Superclass:" + ((GeneralizationRelationship) r).getParent().getName() + " subclasses: "
                    + childrenToStr(((GeneralizationRelationship) r).getAllChildrenForGeneralClass());
        }
        if (r instanceof AssociationRelationship) {
            String participantsStr = participantsToStr(((AssociationRelationship) r).getParticipants());
            strLog += "Participants: " + participantsStr;
        }

        if (r instanceof AssociationClassRelationship) {
            strLog += "AssociationClass: " + ((AssociationClassRelationship) r).getAssociationClass().getName()
                    + ". MembersEnd: " + membersEndToStr(((AssociationClassRelationship) r).getMembersEnd());
        }

        return strLog;
    }

    private static String membersEndToStr(List<MemberEnd> membersEnd) {
        StringBuilder membersEndStrBuilder = new StringBuilder();
        for (MemberEnd member : membersEnd)
            membersEndStrBuilder.append(member.getType().getName()).append(" ");
        return membersEndStrBuilder.toString().trim();
    }

    private static String participantsToStr(List<AssociationEnd> participants) {
        StringBuilder participantsStr = new StringBuilder();
        for (AssociationEnd element : participants)
            participantsStr.append(element.getCLSClass().getName()).append(" ");
        return participantsStr.toString().trim();
    }

    private static String childrenToStr(Set<Element> allChildrenForGeneralClass) {
        StringBuilder children = new StringBuilder();
        for (Element element : allChildrenForGeneralClass)
            children.append(element.getName()).append(" ");
        return children.toString().trim();
    }
}