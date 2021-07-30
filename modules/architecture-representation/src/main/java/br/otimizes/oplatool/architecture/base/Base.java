package br.otimizes.oplatool.architecture.base;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Abstract class responsible for providing the necessary resources to use UML2.
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public abstract class Base {

    private static final ThreadLocal<InitializeResources> resources = ThreadLocal.withInitial(() -> InitializeResources.initializeResources.get());

    public Base() {
    }

    public static ResourceSet getResources() {
        return resources.get().getResources();
    }

}