package arquitetura.helpers;

import arquitetura.base.Base;
import arquitetura.exceptions.*;
import arquitetura.io.ReaderConfig;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.resource.UMLResource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Uml2Helper extends Base {

    private static final boolean PRINT_LOGS = false;
    private static Package profile;
    private static Uml2Helper instance;

    public static Uml2Helper getInstance() {
        if (instance == null)
            instance = new Uml2Helper();
        return instance;
    }

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
        ArrayList<EObject> contents = new ArrayList<EObject>();
        contents.add(package_);
        save(contents, uri);
    }

    private void save(Collection<EObject> contents, URI uri) throws IOException {
        URI finalUri = uri.appendFileExtension(UMLResource.FILE_EXTENSION);
        Resource resource = getResources().createResource(finalUri);
        resource.getContents().addAll(contents);

        resource.save(null);
    }

    public org.eclipse.uml2.uml.Generalization createGeneralization(Classifier child, Classifier parent) {
        return child.createGeneralization(parent);
    }

    /**
     * Cria uma Classe. Por padrão uma class não é abastrata.
     *
     * @param nestingPackage
     * @param name
     * @param isAbstract     Opcional.
     * @return
     */
    public org.eclipse.uml2.uml.Class createClass(org.eclipse.uml2.uml.Package nestingPackage, String name,
                                                  boolean... isAbstract) {
        boolean abstractClass = false;
        if (isAbstract.length > 0) {
            abstractClass = isAbstract[0];
        }
        org.eclipse.uml2.uml.Class createdClass = nestingPackage.createOwnedClass(name, abstractClass);

        return createdClass;
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

    public org.eclipse.uml2.uml.Class referenceMetaclass(Profile profile, String name) throws ModelNotFoundException {
        Model umlMetamodel = (Model) getInternalResources(URI.createURI(UMLResource.UML_METAMODEL_URI));

        org.eclipse.uml2.uml.Class metaclass = (org.eclipse.uml2.uml.Class) umlMetamodel.getOwnedType(name);

        profile.createMetaclassReference(metaclass);

        printLog("Metaclass '" + metaclass.getQualifiedName() + "' referenced.");

        return metaclass;
    }

    public Extension createExtension(org.eclipse.uml2.uml.Class metaclass, Stereotype stereotype, boolean required) {
        Extension extension = stereotype.createExtension(metaclass, required);

        printLog((required ? "Required extension '" : "Extension '") + extension.getQualifiedName() + "' created.");

        return extension;
    }

    public Association createAssociation(Type type1, boolean end1IsNavigable, AggregationKind end1Aggregation,
                                         String end1Name, int end1LowerBound, int end1UpperBound, Type type2, boolean end2IsNavigable,
                                         AggregationKind end2Aggregation, String end2Name, int end2LowerBound, int end2UpperBound) {

        Association association = type1.createAssociation(end1IsNavigable, end1Aggregation, end1Name, end1LowerBound,
                end1UpperBound, type2, end2IsNavigable, end2Aggregation, end2Name, end2LowerBound, end2UpperBound);

        if (PRINT_LOGS) {
            StringBuffer sb = new StringBuffer();

            sb.append("Association ");

            if (null == end1Name || 0 == end1Name.length()) {
                sb.append('{');
                sb.append(type1.getQualifiedName());
                sb.append('}');
            } else {
                sb.append("'");
                sb.append(type1.getQualifiedName());
                sb.append(NamedElement.SEPARATOR);
                sb.append(end1Name);
                sb.append("'");
            }

            sb.append(" [");
            sb.append(end1LowerBound);
            sb.append("..");
            sb.append(LiteralUnlimitedNatural.UNLIMITED == end1UpperBound ? "*" : String.valueOf(end1UpperBound));
            sb.append("] ");

            sb.append(end2IsNavigable ? '<' : '-');
            sb.append('-');
            sb.append(end1IsNavigable ? '>' : '-');
            sb.append(' ');

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
            sb.append("]");

            sb.append(" created.");

            printLog(sb.toString());
        }

        return association;
    }

    public Property createAttribute(org.eclipse.uml2.uml.Class class_, String name, Type type, int lowerBound,
                                    int upperBound) {
        Property attribute = class_.createOwnedAttribute(name, type, lowerBound, upperBound);

        StringBuffer sb = new StringBuffer();

        sb.append("Attribute '");

        sb.append(attribute.getQualifiedName());

        sb.append("' : ");

        sb.append(type.getQualifiedName());

        sb.append(" [");
        sb.append(lowerBound);
        sb.append("..");
        sb.append(LiteralUnlimitedNatural.UNLIMITED == upperBound ? "*" : String.valueOf(upperBound));
        sb.append("]");

        sb.append(" created.");

        printLog(sb.toString());

        return attribute;
    }

    public Enumeration createEnumeration(org.eclipse.uml2.uml.Package pkg, String name) {
        Enumeration enumeration = (Enumeration) pkg.createOwnedEnumeration(name);
        printLog("Enumeration '" + enumeration.getQualifiedName() + "' created.");
        return enumeration;
    }

    public EnumerationLiteral createEnumerationLiteral(Enumeration enumeration, String name) {
        EnumerationLiteral enumerationLiteral = enumeration.createOwnedLiteral(name);

        printLog("Enumeration literal '" + enumerationLiteral.getQualifiedName() + "' created.");

        return enumerationLiteral;
    }

    public org.eclipse.uml2.uml.Package load(String pathAbsolute)
            throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {

        File file = new File(pathAbsolute);
        FilenameFilter filter = new OnlyCompleteResources();
        if (fileExists(file)) {
            File dir = file.getParentFile();
            String resourcesName = file.getName().substring(0, file.getName().lastIndexOf("."));

            if (isCompleteResources(filter, dir, resourcesName))
                throw new ModelIncompleteException("Modelo Incompleto");

            Package model = getExternalResources(pathAbsolute);
            if (model.eClass().equals(UMLPackage.Literals.PROFILE)) {
                if (((Profile) model).isDefined())
                    ((Profile) model).define();
                return model;
            }

            return model;

        }

        throw new ModelNotFoundException("Model " + pathAbsolute + " não encontrado.");
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
        Stereotype stereotype = prof.createOwnedStereotype(name, isAbstract);
        return stereotype;
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

    /**
     * Carrega um recurso interno. Recurso interno pode ser entendido como por
     * exemplo o meta modelo uml, tipos primitivos etc.
     *
     * @param createURI
     * @return
     */
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

    /**
     * Retorno o Profile SMarty. O Path para esse arquivo deve ser configurado
     * dentro do arquivo <b>application.yml</b>
     *
     * @return Profile
     */
    public Profile loadSMartyProfile() {
        return (Profile) getExternalResources(ReaderConfig.getPathToProfileSMarty());
    }

    /**
     * Retorno o Profile Concern. O Path para esse arquivo deve ser configurado
     * dentro do arquivo <b>application.yml</b>
     *
     * @return Profile
     */
    public Profile loadConcernProfile() {
        return (Profile) getExternalResources(ReaderConfig.getPathToProfileConcerns());
    }

    public Profile getSMartyProfile() {
        return (Profile) profile;
    }

    public void setSMartyProfile() {
        profile = loadSMartyProfile();
    }

    public EnumerationLiteral getLiteralEnumeration(String name) throws EnumerationNotFoundException {
        Enumeration a = (Enumeration) getEnumerationByName((Profile) profile, "BindingTime");
        return a.getOwnedLiteral(name);
    }

    public Operation createOperation(Classifier klass, String methodName, EList<String> parameterNames,
                                     EList<Type> parameterTypes, Type returnType) {
        org.eclipse.uml2.uml.Class k = (org.eclipse.uml2.uml.Class) klass;
        return k.createOwnedOperation(methodName, parameterNames, parameterTypes, returnType);
    }
}