package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.helpers.ModelHelper;
import br.otimizes.oplatool.architecture.helpers.ModelHelperFactory;
import br.otimizes.oplatool.architecture.helpers.StereotypeHelper;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.*;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder responsible for creating element of type Class.
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ClassBuilder extends ElementBuilder<Class> {

    private AttributeBuilder attributeBuilder;
    private MethodBuilder methodBuilder;
    private ModelHelper modelHelper;

    /**
     * Receives as parameter {@link Architecture}. <br/>
     * Initializing helper {@link ModelHelper}
     *
     * @param architecture architecture
     */
    public ClassBuilder(Architecture architecture) {
        super(architecture);
        attributeBuilder = new AttributeBuilder(architecture);
        methodBuilder = new MethodBuilder(architecture);

        modelHelper = ModelHelperFactory.getModelHelper();
    }

    /**
     * Build the element
     *
     * @param modelElement element
     * @return class
     */
    @Override
    protected Class buildElement(NamedElement modelElement) {
        Class klass = null;

        boolean isAbstract = false;

        if (modelElement instanceof ClassImpl)
            isAbstract = ((org.eclipse.uml2.uml.Class) modelElement).isAbstract();

        String packageName = modelElement.getNamespace().getQualifiedName();
        packageName = packageName != null ? packageName : "";

        klass = new Class(architecture.getRelationshipHolder(), name, variantType, isAbstract, packageName, XmiHelper.getXmiId(modelElement));
        XmiHelper.setRecursiveOwnedComments(modelElement, klass);
        List<Property> allAttributesForAClass = modelHelper.getAllAttributesForAClass(modelElement);
        for (Property attrNode : allAttributesForAClass) {
            Attribute attr = attributeBuilder.create(attrNode);
            klass.addExternalAttribute(attr);
            XmiHelper.setRecursiveOwnedComments(attrNode, attr);
        }

        List<Operation> allMethods = modelHelper.getAllMethods(modelElement);
        for (Operation methodNode : allMethods) {
            Method method = methodBuilder.create(methodNode);
            klass.addExternalMethod(method);
            XmiHelper.setRecursiveOwnedComments(methodNode, method);

        }

        klass.setPatternOperations(new PatternsOperations(StereotypeHelper.getAllPatternsStereotypes(modelElement)));

        return klass;
    }

    /**
     * Returns all attributes of a Class.
     *
     * @param modelElement element
     * @return attributes
     */
    private List<Attribute> getAttributes(NamedElement modelElement) {
        List<Attribute> attrs = new ArrayList<Attribute>();

        List<Property> attributes = modelHelper.getAllAttributesForAClass(modelElement);
        for (Property property : attributes) {
            attrs.add(attributeBuilder.create(property));
        }

        return attrs;
    }

    /**
     * Returns all methods of a class
     *
     * @param modelElement element
     * @return methods
     */
    private List<Method> getMethods(NamedElement modelElement, Class parent) {
        List<Method> methods = new ArrayList<Method>();
        List<Operation> elements = modelHelper.getAllMethods(modelElement);

        for (Operation classifier : elements)
            methods.add(methodBuilder.create(classifier));

        return methods;
    }

}