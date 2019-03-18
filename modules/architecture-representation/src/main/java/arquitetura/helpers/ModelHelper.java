package arquitetura.helpers;


import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.internal.impl.DependencyImpl;
import org.eclipse.uml2.uml.internal.impl.ModelImpl;
import org.eclipse.uml2.uml.internal.impl.PackageImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static arquitetura.helpers.ElementsTypes.*;

/**
 * Helper para atuar sobre um model ( arquitetura ).
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class ModelHelper extends ElementHelper {

    private static Uml2Helper uml2Helper;

    protected ModelHelper() throws ModelNotFoundException, ModelIncompleteException {
        uml2Helper = Uml2HelperFactory.getUml2Helper();
    }

    /**
     * Recupera Classes de um pacote.
     *
     * @param model
     * @return
     */
    public List<org.eclipse.uml2.uml.Class> getClasses(NamedElement model) {
        return getAllElementsByType(model, CLASS);
    }

    /**
     * Retorna todas as classes de um modelo e seus pacotes
     *
     * @param model
     * @return {@link List}
     */
    public List<org.eclipse.uml2.uml.Class> getAllClasses(NamedElement model) {
        List<org.eclipse.uml2.uml.Class> classes = new ArrayList<org.eclipse.uml2.uml.Class>();
        List<Package> pacotes = getAllPackages(model);

        classes.addAll(getClasses(model));

        for (int i = 0; i < pacotes.size(); i++)
            classes.addAll(getAllClassesOfPackage((Package) pacotes.get(i)));

        if (classes.isEmpty()) return Collections.emptyList();

        return classes;
    }


    public List<Property> getAllAttributesForAClass(NamedElement aClass) {
        List<Property> allPropertys = new ArrayList<Property>();
        allPropertys = getAllElementsByType(aClass, ElementsTypes.PROPERTY);

        if (allPropertys.isEmpty()) return Collections.emptyList();
        return allPropertys;
    }

    public List<org.eclipse.uml2.uml.Class> getAllInterfaces(NamedElement model) {
        return getAllElementsByType(model, INTERFACE);
    }

    public List<Usage> getAllUsage(Package model) {
        return getAllElementsByType(model, USAGE);
    }

    public List<Association> getAllAssociations(NamedElement model) {
        List<Association> associations = new ArrayList<Association>();
        associations = getAllElementsByType(model, ASSOCIATION);

        List<Association> r = new ArrayList<Association>();

        List<Package> paks = getAllPackages(model);
        for (Package pack : paks) {
            EList<org.eclipse.uml2.uml.Element> a = pack.getOwnedElements();
            for (org.eclipse.uml2.uml.Element element : a)
                if (element instanceof Association)
                    r.add(((Association) element));
        }
        for (Association d : associations) r.add(d);

        return r;

    }

    public List<AssociationClass> getAllAssociationsClass(NamedElement model) {
        List<AssociationClass> relations = new ArrayList<AssociationClass>();
        relations = getAllElementsByType(model, ASSOCIATIONCLASS);

        List<AssociationClass> r = new ArrayList<AssociationClass>();

        List<Package> paks = getAllPackages(model);
        for (Package pack : paks) {
            EList<org.eclipse.uml2.uml.Element> a = pack.getOwnedElements();
            for (org.eclipse.uml2.uml.Element element : a)
                if (element instanceof AssociationClass)
                    r.add(((AssociationClass) element));
        }
        for (AssociationClass d : relations) r.add(d);

        return r;

    }


    private List<Dependency> getDependencies(NamedElement model) {

        List<Dependency> relationsDependencies = new ArrayList<Dependency>();
        relationsDependencies = getAllElementsByType(model, DEPENDENCY);
        List<Dependency> r = new ArrayList<Dependency>();

        List<Package> paks = getAllPackages(model);
        for (Package pack : paks) {
            EList<org.eclipse.uml2.uml.Element> a = pack.getOwnedElements();
            for (org.eclipse.uml2.uml.Element element : a)
                if (element instanceof Dependency)
                    r.add(((Dependency) element));
        }
        for (Dependency d : relationsDependencies) r.add(d);

        return r;
    }


    public List<Dependency> getAllDependencies(Package model) {
        List<Dependency> dependencies = getDependencies(model);
        List<Dependency> allDependencies = new ArrayList<Dependency>();
        for (Dependency dependency : dependencies) {
            if (dependency.getClass().equals(DependencyImpl.class))
                allDependencies.add(dependency);
        }
        return allDependencies;
    }


    public List<Abstraction> getAllAbstractions(Package model) {
        return getAllElementsByType(model, ABSTRACTION);
    }

    public List<Package> getAllPackages(NamedElement model) {
        List<Package> pks = new ArrayList<Package>();
        searchPeackages(model, pks);
        return pks;
    }

    private void searchPeackages(NamedElement model, List<Package> pks) {
        for (Element package1 : model.getOwnedElements()) {
            if ((package1 instanceof PackageImpl) && !(package1 instanceof ModelImpl)) {
                pks.add((Package) package1);
                searchPeackages((Package) package1, pks);
            }
        }
    }

    public List<Classifier> getAllComments(NamedElement model) {
        return getAllElementsByType(model, COMMENT);
    }

    public List<Realization> getAllRealizations(Package model) {
        List<Realization> realizations = new ArrayList<Realization>();
        List<Dependency> dependencies = getDependencies(model);

        for (Dependency dependency : dependencies)
            if (dependency instanceof Realization)
                realizations.add(((Realization) dependency));

        for (Object r : getAllElementsByType(model, REALIZATION))
            realizations.add((Realization) r);

        return realizations;
    }

    public List<EList<Generalization>> getAllGeneralizations(NamedElement model) {

        List<org.eclipse.uml2.uml.Class> allClasses = getAllClasses(model);
        List<EList<Generalization>> lista = new ArrayList<EList<Generalization>>();

        for (NamedElement classImpl : allClasses) {
            if (!((Classifier) classImpl).getGeneralizations().isEmpty()) {
                EList<Generalization> g = ((Classifier) classImpl).getGeneralizations();
                lista.add(g);
            }
        }

        return lista;
    }

    public String getName(String xmiFile) throws ModelNotFoundException {
        //System.out.println("getName");
        if (modelExists(xmiFile))
            return new File(xmiFile).getName().split("\\.")[0];

        throw new ModelNotFoundException("Model " + xmiFile + " not found");
    }

    /**
     * Retorno o modelo dado um path. Lança uma exceção caso  o mesmo não exista.
     *
     * @param xmiFile
     * @return
     * @throws ModelNotFoundException
     * @throws ModelIncompleteException
     * @throws SMartyProfileNotAppliedToModelExcepetion
     */
    public Package getModel(String xmiFile) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
        //System.out.println(xmiFile);
        if (modelExists(xmiFile))
            return uml2Helper.load(URI.createURI(xmiFile).toString());

        throw new ModelNotFoundException("Model " + xmiFile + " not found");
    }

    private boolean modelExists(String xmiFile) {
        File model = new File(xmiFile);
        if (model.exists()) return true;

        return false;
    }

    public List<Operation> getAllMethods(NamedElement model) {
        return getAllElementsByType(model, OPERATION);
    }

    /**
     * Retorna todas as variabilidades de um modelo.
     * <p>
     * Essas variabilidades estão em elementos do tipo {@link Comment}
     *
     * @param model
     * @return List<{@link Comment}>
     */
    public List<Comment> getAllVariabilities(Package model) {
        List<Comment> variabilities = new ArrayList<Comment>();
        List<org.eclipse.uml2.uml.Class> classes = new ArrayList<org.eclipse.uml2.uml.Class>();

        classes = getAllClasses(model);

        for (int i = 0; i < classes.size(); i++)
            if (StereotypeHelper.isVariability(classes.get(i)))
                variabilities.addAll(StereotypeHelper.getCommentVariability(classes.get(i)));

        if (!variabilities.isEmpty()) return variabilities;
        return Collections.emptyList();
    }

    private List<org.eclipse.uml2.uml.Class> getAllClassesOfPackage(Package pacote) {
        List<org.eclipse.uml2.uml.Class> classes = new ArrayList<org.eclipse.uml2.uml.Class>();
        classes.addAll(getClasses(pacote));
        return classes;
    }

    /**
     * Retorna o XMIID de um elemento
     *
     * @param eObject
     * @return {@link String}
     */
    public String getXmiId(EObject eObject) {
        Resource xmiResource = eObject.eResource();
        if (xmiResource == null) return null;
        return ((XMLResource) xmiResource).getID(eObject);
    }

    public Classifier getClassByName(String name, Package model) {
        List<Class> classes = getAllClasses(model);
        for (Class klass : classes) {
            if (name.equalsIgnoreCase(klass.getName())) {
                return klass;
            }
        }
        return null;
    }

    public Package loadConcernProfile() {
        return uml2Helper.loadConcernProfile();
    }

}