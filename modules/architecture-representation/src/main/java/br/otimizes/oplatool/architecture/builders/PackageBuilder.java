package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.helpers.ModelElementHelper;
import br.otimizes.oplatool.architecture.helpers.ModelHelper;
import br.otimizes.oplatool.architecture.helpers.ModelHelperFactory;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import org.eclipse.uml2.uml.NamedElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder responsible for creating Package type elements.
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class PackageBuilder extends ElementBuilder<Package> {

    private static final ModelHelper modelHelper;

    static {
        modelHelper = ModelHelperFactory.getModelHelper();
    }

    private final ClassBuilder classBuilder;
    private final InterfaceBuilder interfaceBuilder;

    public PackageBuilder(Architecture architecture, ClassBuilder classBuilder, InterfaceBuilder interfaceBuilder) {
        super(architecture);
        this.classBuilder = classBuilder;
        this.interfaceBuilder = interfaceBuilder;
    }

    @Override
    public Package buildElement(NamedElement modelElement) {
        Package pkg = new Package(architecture.getRelationshipHolder(), name, variantType,
                modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement));
        pkg.getNestedPackages().addAll(getNestedPackages(modelElement));
        XmiHelper.setRecursiveOwnedComments(modelElement, pkg);
        for (Class klass : getClasses(modelElement)) {
            pkg.addExternalClass(klass);
        }
        for (Interface itf : getInterfaces(modelElement))
            pkg.addExternalInterface(itf);

        return pkg;
    }

    private List<Package> getNestedPackages(NamedElement modelElement) {
        List<Package> listOfPackes = new ArrayList<>();
        List<org.eclipse.uml2.uml.Package> paks = modelHelper.getAllPackages(modelElement);

        for (NamedElement element : paks)
            listOfPackes.add(this.create(element));

        return listOfPackes;
    }

    private List<Class> getClasses(NamedElement modelElement) {
        List<Class> listOfClasses = new ArrayList<>();
        List<org.eclipse.uml2.uml.Class> classes = modelHelper.getAllClasses(modelElement);

        for (NamedElement element : classes) {
            if (!ModelElementHelper.isInterface(element)) {
                Class klass = classBuilder.create(element);
                listOfClasses.add(klass);
            }
        }
        return listOfClasses;
    }

    private List<Interface> getInterfaces(NamedElement modelElement) {
        List<Interface> allInterfaces = new ArrayList<>();
        List<org.eclipse.uml2.uml.Class> classes = modelHelper.getAllClasses(modelElement);

        for (NamedElement element : classes) {
            if (ModelElementHelper.isInterface(element)) {
                Interface klass = interfaceBuilder.create(element);
                allInterfaces.add(klass);
            }
        }
        return allInterfaces;
    }
}