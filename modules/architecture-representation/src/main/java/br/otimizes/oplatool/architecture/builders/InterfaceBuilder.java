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

    private MethodBuilder methodBuilder;

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

        Interface interfacee = new Interface(architecture.getRelationshipHolder(), name, variantType, modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement));

        List<Operation> elements = ((org.eclipse.uml2.uml.Class) modelElement).getOperations();

        XmiHelper.setRecursiveOwnedComments(modelElement, interfacee);
        for (Operation operation : elements)
            interfacee.addExternalOperation(methodBuilder.create(operation));

        interfacee.setPatternOperations(new PatternsOperations(StereotypeHelper.getAllPatternsStereotypes(modelElement)));
        return interfacee;
    }

}
