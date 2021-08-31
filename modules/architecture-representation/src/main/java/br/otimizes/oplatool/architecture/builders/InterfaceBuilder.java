package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.helpers.StereotypeHelper;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.PatternsOperations;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;

import java.util.List;

/**
 * Interface builder class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class InterfaceBuilder extends ElementBuilder<Interface> {

    private final MethodBuilder methodBuilder;

    public InterfaceBuilder(Architecture architecture) {
        super(architecture);
        this.methodBuilder = new MethodBuilder(architecture);
    }

    /**
     * Builder for the interface
     *
     * @param modelElement element
     * @return created interface
     */
    @Override
    protected Interface buildElement(NamedElement modelElement) {
        Interface interfaces = new Interface(architecture.getRelationshipHolder(), name, variantType, modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement));
        List<Operation> elements = ((org.eclipse.uml2.uml.Class) modelElement).getOperations();
        XmiHelper.setRecursiveOwnedComments(modelElement, interfaces);
        for (Operation operation : elements)
            interfaces.addExternalMethod(methodBuilder.create(operation));
        interfaces.setPatternOperations(new PatternsOperations(StereotypeHelper.getAllPatternsStereotypes(modelElement)));
        return interfaces;
    }

}
