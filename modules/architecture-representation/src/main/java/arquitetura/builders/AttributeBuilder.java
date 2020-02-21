package arquitetura.builders;


import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * Builder responsável pelos atributos.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class AttributeBuilder extends ElementBuilder<Attribute> {


    public AttributeBuilder(Architecture architecture) {
        super(architecture);
    }

    /**
     * constrói um elemento do tipo atributo.
     */
    @Override
    protected Attribute buildElement(NamedElement modelElement) {
        Type attributeType = ((Property) modelElement).getType();
        String type = attributeType != null ? attributeType.getName() : "";
        String visibilityKind = modelElement.getVisibility() != null ? modelElement.getVisibility().getName() : "";
        boolean shouldGenerateVisualAttribute = true;
        if (modelElement.getRelationships().size() != 0) // para não criar atributos que são de relacionametos
            shouldGenerateVisualAttribute = false;
        return new Attribute(name, visibilityKind, variantType, type, modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement), shouldGenerateVisualAttribute);

    }

}