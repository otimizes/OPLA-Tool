package br.ufpr.dinf.gres.architecture.base;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Classe abstrata responsável por fornecer os recursos necessários para usar a UML2.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public abstract class Base {

    private static ThreadLocal<InitializeResources> resources = ThreadLocal.withInitial(() -> InitializeResources.initializeResources.get());

    public Base() {
    }

    public static ResourceSet getResources() {
        return resources.get().getResources();
    }

}