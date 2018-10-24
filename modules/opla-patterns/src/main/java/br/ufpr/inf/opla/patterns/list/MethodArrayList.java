package br.ufpr.inf.opla.patterns.list;

import arquitetura.representation.Method;
import arquitetura.representation.ParameterMethod;

import java.util.ArrayList;
import java.util.List;

public class MethodArrayList extends ArrayList<Method> {

    private static final long serialVersionUID = 1L;

    public MethodArrayList(List<Method> methods) {
        super((methods != null ? methods : new ArrayList<Method>()));
    }

    public MethodArrayList() {
    }

    @Override
    public boolean contains(Object method) {
        if (method instanceof Method) {
            Method aMethod = (Method) method;
            for (int i = 0; i < this.size(); i++) {
                Method otherMethod = this.get(i);
                if (areMethodsEqual(aMethod, otherMethod)) {
                    return true;
                }
            }

        }
        return false;
    }

    public boolean containsSameName(Object method) {
        if (method instanceof Method) {
            Method aMethod = (Method) method;
            for (int i = 0; i < this.size(); i++) {
                Method otherMethod = this.get(i);
                if (otherMethod != null) {
                    if (aMethod.getName().equals(otherMethod.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean containsSameSignature(Object method) {
        if (method instanceof Method) {
            Method aMethod = (Method) method;
            rootLoop:
            for (int i = 0; i < this.size(); i++) {
                Method otherMethod = this.get(i);
                if (areMethodsEqual(aMethod, otherMethod)) {
                    int size = aMethod.getParameters().size();
                    if (size == otherMethod.getParameters().size()) {
                        for (int j = 0; j < size; j++) {
                            ParameterMethod methodParameter = aMethod.getParameters().get(j);
                            ParameterMethod otherMethodParameter = otherMethod.getParameters().get(j);
                            if (!methodParameter.equals(otherMethodParameter)) {
                                continue rootLoop;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int indexOf(Object method) {
        if (method instanceof Method) {
            for (int i = 0; i < this.size(); i++) {
                Method iMethod = this.get(i);
                if (areMethodsEqual(iMethod, (Method) method)) {
                    return i;
                }
            }
            return -1;
        } else {
            return super.indexOf(method); // To change body of generated
            // methods, choose Tools |
            // Templates.
        }
    }

    private boolean areMethodsEqual(Method method, Method otherMethod) {
        if (method == null && otherMethod == null) {
            return true;
        } else if (method == null || otherMethod == null) {
            return false;
        }
        return (method.getReturnType() == null ? otherMethod.getReturnType() == null
                : method.getReturnType().equals(otherMethod.getReturnType()))
                && (method.getName() == null ? otherMethod.getName() == null
                : method.getName().equals(otherMethod.getName()));
    }
}
