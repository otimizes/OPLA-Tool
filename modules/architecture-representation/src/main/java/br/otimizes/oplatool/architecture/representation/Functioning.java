package br.otimizes.oplatool.architecture.representation;

import java.util.Set;

public interface Functioning extends Identifiable {
    boolean addExternalMethod(Method method);

    boolean removeMethod(Method method);

    Set<Method> getModifiableMethods();
}
