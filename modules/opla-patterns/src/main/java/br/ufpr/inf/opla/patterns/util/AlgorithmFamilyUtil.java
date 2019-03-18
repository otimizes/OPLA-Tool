package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import arquitetura.representation.Method;
import br.ufpr.inf.opla.patterns.list.MethodArrayList;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.Scope;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmFamilyUtil {

    private AlgorithmFamilyUtil() {
    }

    public static List<AlgorithmFamily> getFamiliesFromScope(Scope scope) {
        List<AlgorithmFamily> familiesInScope = new ArrayList<>();
        addFamiliesWithSuffixAndPreffix(scope, familiesInScope);
        addFamiliesWithSameMethod(scope, familiesInScope);
        return familiesInScope;
    }

    private static void addFamiliesWithSuffixAndPreffix(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element iElement = scope.getElements().get(i);
            if (ElementUtil.isClassOrInterface(iElement)) {
                List<String> suffixes = new ArrayList<>();
                List<String> prefixes = new ArrayList<>();

                final String iElementName = iElement.getName();
                for (int k = 3; k <= iElementName.length(); k++) {
                    suffixes.add(iElementName.substring(k - 3, iElementName.length()));
                    prefixes.add(iElementName.substring(0, iElementName.length() - k + 3));
                }

                for (int j = i + 1; j < scope.getElements().size(); j++) {
                    Element jElement = scope.getElements().get(j);
                    if (ElementUtil.isClassOrInterface(jElement)) {
                        final String jElementName = jElement.getName();

                        for (String suffix : suffixes) {
                            if (jElementName.length() >= suffix.length()) {
                                if (jElementName.substring(jElementName.length() - suffix.length()).equals(suffix)) {
                                    AlgorithmFamily algorithmFamily = new AlgorithmFamily(suffix, AlgorithmFamily.SUFFIX);
                                    addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                                }
                            }
                        }

                        for (String prefix : prefixes) {
                            if (jElementName.length() >= prefix.length()) {
                                if (jElementName.substring(0, prefix.length()).equals(prefix)) {
                                    AlgorithmFamily algorithmFamily = new AlgorithmFamily(prefix, AlgorithmFamily.PREFIX);
                                    addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void addFamiliesWithSameMethod(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element iElement = scope.getElements().get(i);
            if (ElementUtil.isClassOrInterface(iElement)) {

                MethodArrayList iMethods = new MethodArrayList(MethodUtil.getAllMethodsFromElement(iElement));
                if (iMethods.isEmpty()) {
                    continue;
                }

                for (int j = i + 1; j < scope.getElements().size(); j++) {
                    Element jElement = scope.getElements().get(j);
                    if (ElementUtil.isClassOrInterface(jElement)) {
                        MethodArrayList jMethods = new MethodArrayList(MethodUtil.getAllMethodsFromElement(jElement));
                        if (jMethods.isEmpty()) {
                            continue;
                        }
                        for (Method iMethod : iMethods) {
                            if (jMethods.contains(iMethod)) {
                                AlgorithmFamily algorithmFamily = new AlgorithmFamily(iMethod.getName(), AlgorithmFamily.METHOD);
                                addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void addElementsToAlgorithmFamily(AlgorithmFamily algorithmFamily, List<AlgorithmFamily> familiesInScope, Element iElement, Element jElement) {
        if (!familiesInScope.contains(algorithmFamily)) {
            algorithmFamily.getParticipants().add(iElement);
            algorithmFamily.getParticipants().add(jElement);
            familiesInScope.add(algorithmFamily);
        } else {
            algorithmFamily = familiesInScope.get(familiesInScope.indexOf(algorithmFamily));
            if (!algorithmFamily.getParticipants().contains(iElement)) {
                algorithmFamily.getParticipants().add(iElement);
            }
            if (!algorithmFamily.getParticipants().contains(jElement)) {
                algorithmFamily.getParticipants().add(jElement);
            }
        }
    }
}
