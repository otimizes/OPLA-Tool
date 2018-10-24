package arquitetura.base;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Classe abstrata responsável por fornecer os recursos necessários para usar a UML2.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public abstract class Base {

    static InitializeResources resources;

    public Base() {
        if (resources == null) {
            //System.out.println("resources nulo");
            resources = InitializeResources.getInstance();
        }
    }

    public static ResourceSet getResources() {
        return resources.getResources();
    }

}