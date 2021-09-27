package br.otimizes.oplatool.architecture.helpers;

import br.otimizes.oplatool.architecture.base.Base;
import br.otimizes.oplatool.architecture.exceptions.*;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.resource.UMLResource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Uml2Helper extends Base {

    private static final boolean PRINT_LOGS = false;
    private static final ThreadLocal<Package> profile = new ThreadLocal<>();
    public static ThreadLocal<Uml2Helper> instance = ThreadLocal.withInitial(Uml2Helper::new);

    private static boolean fileExists(File file) {
        return file.exists();
    }

    private static boolean isCompleteResources(FilenameFilter filter, File dir, String resourcesName) {
        return !filter.accept(dir, resourcesName);
    }

    public Profile createProfile(String name) {
        Profile profile = UMLFactory.eINSTANCE.createProfile();
        profile.setName(name);
        printLog("Profile '" + profile.getQualifiedName() + "' created.");
        return profile;
    }

    public void saveResources(org.eclipse.uml2.uml.Package package_, URI uri) throws IOException {
        ArrayList<EObject> contents = new ArrayList<>();
        contents.add(package_);
        save(contents, uri);
    }

    private void save(Collection<EObject> contents, URI uri) throws IOException {
        URI finalUri = uri.appendFileExtension(UMLResource.FILE_EXTENSION);
        Resource resource = getResources().createResource(finalUri);
        resource.getContents().addAll(contents);
        resource.save(null);
    }

    public void createGeneralization(Classifier child, Classifier parent) {
        child.createGeneralization(parent);
    }

    public org.eclipse.uml2.uml.Class createClass(org.eclipse.uml2.uml.Package nestingPackage, String name,
                                                  boolean... isAbstract) {
        boolean abstractClass = false;
        if (isAbstract.length > 0) {
            abstractClass = isAbstract[0];
        }
        return nestingPackage.createOwnedClass(name, abstractClass);
    }

    public org.eclipse.uml2.uml.Package createPackage(org.eclipse.uml2.uml.Package nestingPackage, String name) {
        org.eclipse.uml2.uml.Package createdPackage = nestingPackage.createNestedPackage(name);
        printLog("Package '" + createdPackage.getQualifiedName() + "' created.");
        return createdPackage;
    }

    public Model createModel(String name) {
        Model model = UMLFactory.eINSTANCE.createModel();
        model.setName(name);
        printLog("Model '" + model.getQualifiedName() + "' created.");
        return model;
    }

    public void err(String error) {
        System.out.println(error);
    }

    public org.eclipse.uml2.uml.Class referenceMetaClass(Profile profile, String name) {
        Model umlMetamodel = (Model) getInternalResources(URI.createURI(UMLResource.UML_METAMODEL_URI));
        org.eclipse.uml2.uml.Class metaClass = (org.eclipse.uml2.uml.Class) umlMetamodel.getOwnedType(name);
        profile.createMetaclassReference(metaClass);
        printLog("MetaClass '" + metaClass.getQualifiedName() + "' referenced.");
        return metaClass;
    }

    public void createExtension(Class metaClass, Stereotype stereotype, boolean required) {
        Extension extension = stereotype.createExtension(metaClass, required);
        printLog((required ? "Required extension '" : "Extension '") + extension.getQualifiedName() + "' created.");
    }

    public Association createAssociation(Type type1, boolean end1IsNavigable, AggregationKind end1Aggregation,
                                         String end1Name, int end1LowerBound, int end1UpperBound, Type type2, boolean end2IsNavigable,
                                         AggregationKind end2Aggregation, String end2Name, int end2LowerBound, int end2UpperBound) {

        Association association = type1.createAssociation(end1IsNavigable, end1Aggregation, end1Name, end1LowerBound,
                end1UpperBound, type2, end2IsNavigable, end2Aggregation, end2Name, end2LowerBound, end2UpperBound);
        if (PRINT_LOGS) {
            StringBuffer sb = new StringBuffer();
            sb.append("Association ");
            appendEnd(type1, end1Name, end1LowerBound, end1UpperBound, sb);
            sb.append("] ");
            sb.append(end2IsNavigable ? '<' : '-');
            sb.append('-');
            sb.append(end1IsNavigable ? '>' : '-');
            sb.append(' ');
            appendEnd(type2, end2Name, end2LowerBound, end2UpperBound, sb);
            sb.append("]");
            sb.append(" created.");
            printLog(sb.toString());
        }
        return association;
    }

    private void appendEnd(Type type2, String end2Name, int end2LowerBound, int end2UpperBound, StringBuffer sb) {
        if (null == end2Name || 0 == end2Name.length()) {
            sb.append('{');
            sb.append(type2.getQualifiedName());
            sb.append('}');
        } else {
            sb.append("'");
            sb.append(type2.getQualifiedName());
            sb.append(NamedElement.SEPARATOR);
            sb.append(end2Name);
            sb.append("'");
        }
        sb.append(" [");
        sb.append(end2LowerBound);
        sb.append("..");
        sb.append(LiteralUnlimitedNatural.UNLIMITED == end2UpperBound ? "*" : String.valueOf(end2UpperBound));
    }

    public void createAttribute(Class class_, String name, Type type, int lowerBound,
                                int upperBound) {
        Property attribute = class_.createOwnedAttribute(name, type, lowerBound, upperBound);
        String sb = "Attribute '" +
                attribute.getQualifiedName() +
                "' : " +
                type.getQualifiedName() +
                " [" +
                lowerBound +
                ".." +
                (LiteralUnlimitedNatural.UNLIMITED == upperBound ? "*" : String.valueOf(upperBound)) +
                "]" +
                " created.";
        printLog(sb);
    }

    public Enumeration createEnumeration(org.eclipse.uml2.uml.Package pkg, String name) {
        Enumeration enumeration = pkg.createOwnedEnumeration(name);
        printLog("Enumeration '" + enumeration.getQualifiedName() + "' created.");
        return enumeration;
    }

    public void createEnumerationLiteral(Enumeration enumeration, String name) {
        EnumerationLiteral enumerationLiteral = enumeration.createOwnedLiteral(name);
        printLog("Enumeration literal '" + enumerationLiteral.getQualifiedName() + "' created.");
    }

    public org.eclipse.uml2.uml.Package load(String pathAbsolute)
            throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelException {
        File file = new File(pathAbsolute);
        FilenameFilter filter = new OnlyCompleteResources();
        if (fileExists(file)) {
            File dir = file.getParentFile();
            String resourcesName = file.getName().substring(0, file.getName().lastIndexOf("."));
            if (isCompleteResources(filter, dir, resourcesName))
                throw new ModelIncompleteException("Incomplete model");
            Package model = getExternalResources(pathAbsolute);
            if (model.eClass().equals(UMLPackage.Literals.PROFILE)) {
                if (((Profile) model).isDefined())
                    ((Profile) model).define();
                return model;
            }
            return model;
        }

        throw new ModelNotFoundException("Model " + pathAbsolute + " not found.");
    }

    public PackageableElement getEnumerationByName(Profile profile, String name) throws EnumerationNotFoundException {
        EList<PackageableElement> a = profile.getPackagedElements();
        for (PackageableElement packageableElement : a) {
            if (packageableElement.eClass().equals(UMLPackage.Literals.ENUMERATION)
                    && packageableElement.getName().equalsIgnoreCase(name))
                return packageableElement;
        }

        throw new EnumerationNotFoundException(name);
    }

    public Type getPrimitiveType(String typeName) throws ModelNotFoundException {
        Package umlPrimitiveTypes = getInternalResources(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
        return umlPrimitiveTypes.getOwnedType(UtilResources.capitalize(typeName));
    }

    public Stereotype createStereotype(Profile prof, String name, boolean isAbstract) {
        return prof.createOwnedStereotype(name, isAbstract);
    }

    public PackageableElement getStereotypeByName(Profile prof, String name) throws StereotypeNotFoundException {
        EList<PackageableElement> a = prof.getPackagedElements();
        for (PackageableElement packageableElement : a)
            if (packageableElement instanceof Stereotype && packageableElement.getName().equals(name))
                return packageableElement;

        throw new StereotypeNotFoundException(name);
    }

    public void applyProfile(org.eclipse.uml2.uml.Package pkg, Profile profile) {
        try {
            pkg.applyProfile(profile);
            printLog("Profile '" + profile.getQualifiedName() + "' applied to package '" + pkg.getQualifiedName()
                    + "'.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printLog(String message) {
        if (PRINT_LOGS)
            System.out.println(message);
    }

    private Package getInternalResources(URI createURI) {
        Resource resource = getResources().getResource(createURI, true);

        return (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(),
                UMLPackage.Literals.PACKAGE);
    }

    public org.eclipse.uml2.uml.Package getExternalResources(String uri) {
        org.eclipse.uml2.uml.Package package_;

        Resource resource = getResources().getResource(URI.createFileURI(uri), true);
        package_ = (org.eclipse.uml2.uml.Package) EcoreUtil.getObjectByType(resource.getContents(),
                UMLPackage.Literals.PACKAGE);
        return package_;
    }

    public Profile loadSMartyProfile() {
        return (Profile) getExternalResources(ApplicationFileConfigThreadScope.getPathToProfile());
    }

    public Profile loadConcernProfile() {
        return (Profile) getExternalResources(ApplicationFileConfigThreadScope.getPathToProfileConcern());
    }

    public Profile getSMartyProfile() {
        return (Profile) profile.get();
    }

    public void setSMartyProfile() {
        profile.set(loadSMartyProfile());
    }

    public EnumerationLiteral getLiteralEnumeration(String name) throws EnumerationNotFoundException {
        Enumeration a = (Enumeration) getEnumerationByName((Profile) profile.get(), "BindingTime");
        return a.getOwnedLiteral(name);
    }

    public Operation createOperation(Classifier klass, String methodName, EList<String> parameterNames,
                                     EList<Type> parameterTypes, Type returnType) {
        org.eclipse.uml2.uml.Class k = (org.eclipse.uml2.uml.Class) klass;
        return k.createOwnedOperation(methodName, parameterNames, parameterTypes, returnType);
    }
}