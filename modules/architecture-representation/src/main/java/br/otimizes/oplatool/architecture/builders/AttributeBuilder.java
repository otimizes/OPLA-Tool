package br.otimizes.oplatool.architecture.builders;


import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Attribute;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * Builder responsible for the attributes.
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AttributeBuilder extends ElementBuilder<Attribute> {
    public AttributeBuilder(Architecture architecture) {
        super(architecture);
    }

    /**
     * Constructs an element of type attribute.
     */
    @Override
    protected Attribute buildElement(NamedElement modelElement) {
        Type attributeType = ((Property) modelElement).getType();
        String type = attributeType != null ? attributeType.getName() : "";
        String visibilityKind = modelElement.getVisibility() != null ? modelElement.getVisibility().getName() : "";
        boolean shouldGenerateVisualAttribute = modelElement.getRelationships().size() == 0;
        return new Attribute(name, visibilityKind, variantType, type, modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement), shouldGenerateVisualAttribute);

    }

}