package arquitetura.builders;

import arquitetura.helpers.ModelHelper;
import arquitetura.helpers.ModelHelperFactory;
import arquitetura.helpers.StereotypeHelper;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.*;
import arquitetura.representation.Class;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder resposável por criar element do tipo Classe.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class ClassBuilder extends ElementBuilder<arquitetura.representation.Class> {

    private AttributeBuilder attributeBuilder;
    private MethodBuilder methodBuilder;
    private ModelHelper modelHelper;

    /**
     * Recebe como paramêtro {@link Architecture}. <br/>
     * Initializa helper {@link ModelHelper}
     *
     * @param architecture
     */
    public ClassBuilder(Architecture architecture) {
        super(architecture);
        attributeBuilder = new AttributeBuilder(architecture);
        methodBuilder = new MethodBuilder(architecture);

        modelHelper = ModelHelperFactory.getModelHelper();
    }

    @Override
    protected arquitetura.representation.Class buildElement(NamedElement modelElement) {
        arquitetura.representation.Class klass = null;

        boolean isAbstract = false;

        if (modelElement instanceof ClassImpl)
            isAbstract = ((org.eclipse.uml2.uml.Class) modelElement).isAbstract();

        String packageName = ((NamedElement) modelElement).getNamespace().getQualifiedName();
        packageName = packageName != null ? packageName : "";

        klass = new Class(architecture.getRelationshipHolder(), name, variantType, isAbstract, packageName, XmiHelper.getXmiId(modelElement));
        for (Attribute a : getAttributes(modelElement)) {
            klass.addExternalAttribute(a);
        }

        for (Method m : getMethods(modelElement, klass)) {
            klass.addExternalMethod(m);
        }

        klass.setPatternOperations(new PatternsOperations(StereotypeHelper.getAllPatternsStereotypes(modelElement)));

        return klass;
    }

    /**
     * Retorna todos atributos de uma Class.
     *
     * @param modelElement
     * @return List
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
     * Retorna todos os método de uma classe
     *
     * @param modelElement
     * @return List
     */
    private List<Method> getMethods(NamedElement modelElement, Class parent) {
        List<Method> methods = new ArrayList<Method>();
        List<Operation> elements = modelHelper.getAllMethods(modelElement);

        for (Operation classifier : elements)
            methods.add(methodBuilder.create(classifier));

        return methods;
    }

}