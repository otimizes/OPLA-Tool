package br.otimizes.oplatool.patterns.util;

import java.util.ArrayList;
import java.util.List;

import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.patterns.list.MethodArrayList;

/**
 * The Class AlgorithmFamilyUtil.
 */
public class AlgorithmFamilyUtil {

    /**
     * Instantiates a new algorithm family util.
     */
    private AlgorithmFamilyUtil() {
    }

    /**
     * Gets the families from scope.
     *
     * @param scope the scope
     * @return the list of families from scope
     */
    public static List<AlgorithmFamily> getFamiliesFromScope(Scope scope) {
        List<AlgorithmFamily> familiesInScope = new ArrayList<>();
        addFamiliesWithSuffixAndPreffix(scope, familiesInScope);
        addFamiliesWithSameMethod(scope, familiesInScope);
        return familiesInScope;
    }

    /**
     * Adds the families with suffix and preffix.
     *
     * @param scope the scope
     * @param familiesInScope the families in scope
     */
    private static void addFamiliesWithSuffixAndPreffix(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element iElement = scope.getElements().get(i);
            if (ElementUtil.isClassOrInterface(iElement)) {
                List<String> suffixes = new ArrayList<>();
                List<String> prefixes = new ArrayList<>();

                final String iElementName = iElement.getName();
                for (int k = 3; k <= iElementName.length(); k++) {
                    suffixes.add(iElementName.substring(k - 3));
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

    /**
     * Adds the families with same method.
     *
     * @param scope the scope
     * @param familiesInScope the families in scope
     */
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

    /**
     * Adds the elements to algorithm family.
     *
     * @param algorithmFamily the algorithm family
     * @param familiesInScope the families in scope
     * @param iElement the i element
     * @param jElement the j element
     */
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
