package br.otimizes.oplatool.architecture.helpers;


import br.otimizes.oplatool.architecture.exceptions.ModelIncompleteException;
import br.otimizes.oplatool.architecture.exceptions.ModelNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.SMartyProfileNotAppliedToModelException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.internal.impl.DependencyImpl;
import org.eclipse.uml2.uml.internal.impl.ModelImpl;
import org.eclipse.uml2.uml.internal.impl.PackageImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static br.otimizes.oplatool.architecture.helpers.ElementsTypes.*;

/**
 * Helper to execute a model
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ModelHelper extends ElementHelper {

    private final ThreadLocal<Uml2Helper> uml2Helper = ThreadLocal.withInitial(() -> Uml2HelperFactory.instance.get());

    public List<org.eclipse.uml2.uml.Class> getClasses(NamedElement element) {
        return getAllElementsByType(element, CLASS);
    }

    public List<org.eclipse.uml2.uml.Class> getAllClasses(NamedElement element) {
        List<Package> packages = getAllPackages(element);
        List<Class> classes = new ArrayList<>(getClasses(element));
        for (Package aPackage : packages) classes.addAll(getAllClassesOfPackage(aPackage));
        if (classes.isEmpty()) return Collections.emptyList();
        return classes;
    }

    public List<Property> getAllAttributesForAClass(NamedElement aClass) {
        List<Property> allProperties = getAllElementsByType(aClass, ElementsTypes.PROPERTY);

        if (allProperties.isEmpty()) return Collections.emptyList();
        return allProperties;
    }

    public List<org.eclipse.uml2.uml.Class> getAllInterfaces(NamedElement model) {
        return getAllElementsByType(model, CONNECTOR);
    }

    public List<Usage> getAllUsage(Package model) {
        return getAllElementsByType(model, USAGE);
    }

    public List<Association> getAllAssociations(NamedElement model) {
        List<Association> associations = new ArrayList<Association>();
        associations = getAllElementsByType(model, ASSOCIATION);

        List<Association> r = new ArrayList<Association>();

        List<Package> packages = getAllPackages(model);
        for (Package pack : packages) {
            EList<org.eclipse.uml2.uml.Element> a = pack.getOwnedElements();
            for (org.eclipse.uml2.uml.Element element : a)
                if (element instanceof Association)
                    r.add(((Association) element));
        }
        r.addAll(associations);
        return r;
    }

    public List<AssociationClass> getAllAssociationsClass(NamedElement model) {
        List<AssociationClass> relations = getAllElementsByType(model, ASSOCIATIONCLASS);
        List<AssociationClass> r = new ArrayList<>();
        List<Package> packages = getAllPackages(model);
        for (Package pack : packages) {
            EList<org.eclipse.uml2.uml.Element> a = pack.getOwnedElements();
            for (org.eclipse.uml2.uml.Element element : a)
                if (element instanceof AssociationClass)
                    r.add(((AssociationClass) element));
        }
        r.addAll(relations);
        return r;
    }


    private List<Dependency> getDependencies(NamedElement model) {
        List<Dependency> relationsDependencies = getAllElementsByType(model, DEPENDENCY);
        List<Dependency> r = new ArrayList<>();
        List<Package> packages = getAllPackages(model);
        for (Package pack : packages) {
            EList<org.eclipse.uml2.uml.Element> a = pack.getOwnedElements();
            for (org.eclipse.uml2.uml.Element element : a)
                if (element instanceof Dependency)
                    r.add(((Dependency) element));
        }
        r.addAll(relationsDependencies);
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
        List<Package> pks = new ArrayList<>();
        searchPackages(model, pks);
        return pks;
    }

    private void searchPackages(NamedElement model, List<Package> pks) {
        for (Element package1 : model.getOwnedElements()) {
            if ((package1 instanceof PackageImpl) && !(package1 instanceof ModelImpl)) {
                pks.add((Package) package1);
                searchPackages((Package) package1, pks);
            }
        }
    }

    public List<Classifier> getAllComments(NamedElement model) {
        return getAllElementsByType(model, COMMENT);
    }

    public List<Realization> getAllRealizations(Package model) {
        List<Realization> realizations = new ArrayList<>();
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
        List<EList<Generalization>> list = new ArrayList<>();

        for (Classifier classImpl : allClasses) {
            if (!classImpl.getGeneralizations().isEmpty()) {
                EList<Generalization> g = classImpl.getGeneralizations();
                list.add(g);
            }
        }
        return list;
    }

    public String getName(String xmiFile) throws ModelNotFoundException {
        if (modelExists(xmiFile))
            return new File(xmiFile).getName().split("\\.")[0];

        throw new ModelNotFoundException("Model " + xmiFile + " not found");
    }

    public Package getModel(String xmiFile) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelException {
        if (modelExists(xmiFile))
            return uml2Helper.get().load(URI.createURI(xmiFile).toString());

        throw new ModelNotFoundException("Model " + xmiFile + " not found");
    }

    private boolean modelExists(String xmiFile) {
        File model = new File(xmiFile);
        return model.exists();
    }

    public List<Operation> getAllMethods(NamedElement model) {
        return getAllElementsByType(model, OPERATION);
    }

    public List<Comment> getAllVariabilities(Package model) {
        List<Comment> variabilities = new ArrayList<>();
        List<org.eclipse.uml2.uml.Class> classes = getAllClasses(model);

        for (Class aClass : classes)
            if (StereotypeHelper.isVariability(aClass))
                variabilities.addAll(StereotypeHelper.getCommentVariability(aClass));

        if (!variabilities.isEmpty()) return variabilities;
        return Collections.emptyList();
    }

    private List<org.eclipse.uml2.uml.Class> getAllClassesOfPackage(Package pkg) {
        return new ArrayList<>(getClasses(pkg));
    }

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
        return uml2Helper.get().loadConcernProfile();
    }

    public Profile loadSmarty() {
        return uml2Helper.get().loadSMartyProfile();
    }

}