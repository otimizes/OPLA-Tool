package br.otimizes.oplatool.patterns.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.otimizes.oplatool.architecture.representation.Class;
import org.apache.commons.collections4.CollectionUtils;

import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.ParameterMethod;
import br.otimizes.oplatool.patterns.list.MethodArrayList;

/**
 * The Class MethodUtil.
 */
public class MethodUtil {

    /**
     * Instantiates a new method util.
     */
    private MethodUtil() {
    }

    /**
     * Gets the methods from element.
     *
     * @param element the element
     * @return the set of methods of the element
     */
    public static Set<Method> getMethodsFromElement(Element element) {
        Set<Method> iMethods;
        if (element instanceof Class) {
            Class iClass = (Class) element;
            iMethods = iClass.getAllMethods();
        } else if (element instanceof Interface) {
            Interface iInterface = (Interface) element;
            iMethods = iInterface.getMethods();
        } else {
            return null;
        }
        return iMethods;
    }

    /**
     * Gets the all methods from element.
     *
     * @param element the element
     * @return the list with all methods of the element
     */
    public static List<Method> getAllMethodsFromElement(Element element) {
        List<Method> iMethods = new ArrayList<>();
        if (element instanceof Class) {
            Class iClass = (Class) element;
            iMethods.addAll(iClass.getAllMethods());
        } else if (element instanceof Interface) {
            Interface iInterface = (Interface) element;
            iMethods.addAll(iInterface.getMethods());
        }
        List<Element> parents = ElementUtil.getAllExtendedElements(element);
        for (Element parent : parents) {
            if (parent.getClass().equals(element.getClass())) {
                Set<Method> parentMethods = getMethodsFromElement(parent);
                for (Method parentMethod : parentMethods) {
                    if (!iMethods.contains(parentMethod)) {
                        iMethods.add(parentMethod);
                    }
                }
            }
        }
        return iMethods;
    }

    /**
     * Gets the all methods from element by concern.
     *
     * @param element the element
     * @param concern the concern
     * @return the list with all methods of the element by concern
     */
    public static List<Method> getAllMethodsFromElementByConcern(Element element, Concern concern) {
        List<Method> methods = getAllMethodsFromElement(element);
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            if (concern == null ? !method.getAllConcerns().isEmpty() : (!method.containsConcern(concern) && !element.getOwnConcerns().contains(concern))) {
                methods.remove(i);
                i--;
            }
        }
        return methods;
    }

    /**
     * Gets the all methods from set of elements.
     *
     * @param elements the elements
     * @return the list of all methods from set of elements
     */
    public static List<Method> getAllMethodsFromSetOfElements(List<Element> elements) {
        MethodArrayList methods = new MethodArrayList();
        for (Element element : elements) {
            MethodArrayList elementMethods = new MethodArrayList(getAllMethodsFromElement(element));
            for (Method elementMethod : elementMethods) {
                if (!methods.contains(elementMethod)) {
                    methods.add(cloneMethod(elementMethod));
                } else {
                    Method tempMethod = methods.get(methods.indexOf(elementMethod));
                    mergeMethodsToMethodA(tempMethod, elementMethod);
                }
            }
        }
        return methods;
    }

    /**
     * Gets the all methods from set of elements by concern.
     *
     * @param elements the elements
     * @param concern the concern
     * @return the list with all methods from set of elements by concern
     */
    public static List<Method> getAllMethodsFromSetOfElementsByConcern(List<Element> elements, Concern concern) {
        MethodArrayList methods = new MethodArrayList();
        for (Element element : elements) {
            MethodArrayList elementMethods = new MethodArrayList(getAllMethodsFromElement(element));
            for (Method elementMethod : elementMethods) {
                if (concern == null
                        ? (element.getOwnConcerns().isEmpty() && elementMethod.getOwnConcerns().isEmpty())
                        : (element.getOwnConcerns().contains(concern) || elementMethod.getOwnConcerns().contains(concern))) {
                    if (!methods.contains(elementMethod)) {
                        methods.add(cloneMethod(elementMethod));
                    } else {
                        Method tempMethod = methods.get(methods.indexOf(elementMethod));
                        mergeMethodsToMethodA(tempMethod, elementMethod);
                    }
                }
            }
        }
        return methods;
    }

    /**
     * Creates the methods from set of elements.
     *
     * @param elements the elements
     * @return the list with all created methods
     */
    public static List<Method> createMethodsFromSetOfElements(List<Element> elements) {
        MethodArrayList methods = new MethodArrayList();
        for (Element element : elements) {
            MethodArrayList methodsFromElement = new MethodArrayList(getAllMethodsFromElement(element));
            methodFor:
            for (Method elementMethod : methodsFromElement) {
                Method clonedMethod = cloneMethod(elementMethod);
                int count = 1;
                String name = clonedMethod.getName();
                while (methods.containsSameName(clonedMethod)) {
                    if (methods.contains(clonedMethod)) {
                        Method method = methods.get(methods.indexOf(clonedMethod));
                        mergeMethodsToMethodA(method, clonedMethod);
                        continue methodFor;
                    }
                    count++;
                    clonedMethod.setName(name + count);
                }
                methods.add(clonedMethod);
            }
        }
        return methods;
    }

    /**
     * Creates the methods from set of elements by concern.
     *
     * @param elements the elements
     * @param concern the concern
     * @return the list with all methods created
     */
    public static List<Method> createMethodsFromSetOfElementsByConcern(List<Element> elements, Concern concern) {
        MethodArrayList methods = new MethodArrayList();
        MethodArrayList methodsFromElements = new MethodArrayList(getAllMethodsFromSetOfElementsByConcern(elements, concern));
        methodFor:
        for (Method elementMethod : methodsFromElements) {
            Method clonedMethod = cloneMethod(elementMethod);
            int count = 1;
            String name = clonedMethod.getName();
            while (methods.containsSameName(clonedMethod)) {
                if (methods.contains(clonedMethod)) {
                    Method method = methods.get(methods.indexOf(clonedMethod));
                    mergeMethodsToMethodA(method, clonedMethod);
                    continue methodFor;
                }
                count++;
                clonedMethod.setName(name + count);
            }
            methods.add(clonedMethod);
        }
        return methods;
    }

    /**
     * Clone method.
     *
     * @param method the method
     * @return the cloned method
     */
    public static Method cloneMethod(Method method) {
        Method newMethod = new Method(method.getName(), method.getReturnType(), "", method.isAbstract(), UUID.randomUUID().toString());
        newMethod.getParameters().addAll(method.getParameters());
        for (Concern concern : method.getOwnConcerns()) {
            try {
                newMethod.addConcern(concern.getName());
            } catch (ConcernNotFoundException ex) {
                Logger.getLogger(MethodUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        newMethod.setNamespace(method.getNamespace());
        return newMethod;
    }

    /**
     * Clone methods.
     *
     * @param methodsToBeCloned the methods to be cloned
     * @return the set with all cloned methods
     */
    public static Set<Method> cloneMethods(Set<Method> methodsToBeCloned) {
        Set<Method> methods = new HashSet<>();
        for (Method method : methodsToBeCloned) {
            methods.add(cloneMethod(method));
        }
        return methods;
    }

    /**
     * Clone methods.
     *
     * @param methodsToBeCloned the methods to be cloned
     * @return the list with all cloned methods
     */
    public static List<Method> cloneMethods(List<Method> methodsToBeCloned) {
        List<Method> methods = new ArrayList<>();
        for (Method method : methodsToBeCloned) {
            methods.add(cloneMethod(method));
        }
        return methods;
    }

    /**
     * Merge methods to new one.
     *
     * @param methodA the method A
     * @param methodB the method B
     * @return the new method
     */
    public static Method mergeMethodsToNewOne(Method methodA, Method methodB) {
        Method newMethod = cloneMethod(methodA);

        mergeMethodsToMethodA(newMethod, methodB);

        return newMethod;
    }

    /**
     * Merge methods to method A.
     *
     * @param methodA the method A
     * @param methodB the method B
     */
    public static void mergeMethodsToMethodA(Method methodA, Method methodB) {
        for (ParameterMethod bParameter : methodB.getParameters()) {
            ParameterMethod clonedParameter = ParameterMethodUtil.cloneParameter(bParameter);
            List<ParameterMethod> aParameters = methodA.getParameters();
            if (aParameters.contains(clonedParameter)) {
                ParameterMethod aParameter = aParameters.get(aParameters.indexOf(clonedParameter));
                if (!aParameter.getType().equals(clonedParameter.getType())
                        || !aParameter.getDirection().equals(clonedParameter.getDirection())) {
                    int count = 1;
                    String name = clonedParameter.getName();
                    do {
                        count++;
                        clonedParameter.setName(name + count);
                    } while (aParameters.contains(clonedParameter));
                    aParameters.add(clonedParameter);
                }
            } else {
                aParameters.add(clonedParameter);
            }
        }

        ArrayList<Concern> concerns = new ArrayList<>(methodA.getOwnConcerns());
        for (Concern concern : concerns) {
            methodA.removeConcern(concern.getName());
        }
        for (Concern concern : CollectionUtils.union(concerns, methodB.getOwnConcerns())) {
            try {
                methodA.addConcern(concern.getName());
            } catch (ConcernNotFoundException ex) {
                Logger.getLogger(MethodUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Gets the all common methods from set of elements.
     *
     * @param elements the elements
     * @return the list with all common methods from set of elements
     */
    public static List<Method> getAllCommonMethodsFromSetOfElements(List<Element> elements) {
        List<Method> methods = new ArrayList<>();
        if (!elements.isEmpty()) {
            methods.addAll(cloneMethods(getAllMethodsFromElement(elements.get(0))));
            for (int i = 1; i < elements.size(); i++) {
                Element iElement = elements.get(i);
                MethodArrayList iMethods = new MethodArrayList(getAllMethodsFromElement(iElement));
                for (int j = 0; j < methods.size(); j++) {
                    Method method = methods.get(j);
                    if (iMethods.contains(method)) {
                        mergeMethodsToMethodA(method, iMethods.get(iMethods.indexOf(method)));
                    } else {
                        methods.remove(j);
                        j--;
                    }
                }
            }
        }
        return methods;
    }

    /**
     * Gets the all common methods from set of elements by concern.
     *
     * @param elements the elements
     * @param concern the concern
     * @return the list with all common methods from set of elements by concern
     */
    public static List<Method> getAllCommonMethodsFromSetOfElementsByConcern(List<Element> elements, Concern concern) {
        List<Method> methods = new ArrayList<>();
        if (!elements.isEmpty()) {
            methods.addAll(cloneMethods(getAllMethodsFromElementByConcern(elements.get(0), concern)));
            for (int i = 1; i < elements.size(); i++) {
                Element iElement = elements.get(i);
                MethodArrayList iMethods = new MethodArrayList(getAllMethodsFromElementByConcern(iElement, concern));
                for (int j = 0; j < methods.size(); j++) {
                    Method method = methods.get(j);
                    if (iMethods.contains(method)) {
                        mergeMethodsToMethodA(method, iMethods.get(iMethods.indexOf(method)));
                    } else {
                        methods.remove(j);
                        j--;
                    }
                }
            }
        }
        return methods;
    }
}
