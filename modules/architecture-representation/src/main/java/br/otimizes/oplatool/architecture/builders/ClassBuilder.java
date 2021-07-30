package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.helpers.ModelHelper;
import br.otimizes.oplatool.architecture.helpers.ModelHelperFactory;
import br.otimizes.oplatool.architecture.helpers.StereotypeHelper;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.*;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

import java.util.List;

/**
 * Builder responsible for creating element of type Class.
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ClassBuilder extends ElementBuilder<Class> {

    private final AttributeBuilder attributeBuilder;
    private final MethodBuilder methodBuilder;
    private final ModelHelper modelHelper;

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
        boolean isAbstract = false;

        if (modelElement instanceof ClassImpl)
            isAbstract = ((org.eclipse.uml2.uml.Class) modelElement).isAbstract();

        String packageName = modelElement.getNamespace().getQualifiedName();
        packageName = packageName != null ? packageName : "";

        Class newClass = new Class(architecture.getRelationshipHolder(), name, variantType, isAbstract, packageName, XmiHelper.getXmiId(modelElement));
        XmiHelper.setRecursiveOwnedComments(modelElement, newClass);
        List<Property> allAttributesForAClass = modelHelper.getAllAttributesForAClass(modelElement);
        for (Property attrNode : allAttributesForAClass) {
            Attribute attr = attributeBuilder.create(attrNode);
            newClass.addExternalAttribute(attr);
            XmiHelper.setRecursiveOwnedComments(attrNode, attr);
        }
        List<Operation> allMethods = modelHelper.getAllMethods(modelElement);
        for (Operation methodNode : allMethods) {
            Method method = methodBuilder.create(methodNode);
            newClass.addExternalMethod(method);
            XmiHelper.setRecursiveOwnedComments(methodNode, method);
        }
        newClass.setPatternOperations(new PatternsOperations(StereotypeHelper.getAllPatternsStereotypes(modelElement)));
        return newClass;
    }
}