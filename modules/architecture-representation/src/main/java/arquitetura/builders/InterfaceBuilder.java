package arquitetura.builders;

import arquitetura.helpers.StereotypeHelper;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import arquitetura.representation.PatternsOperations;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;

import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class InterfaceBuilder extends ElementBuilder<arquitetura.representation.Interface> {

    private MethodBuilder methodBuilder;

    public InterfaceBuilder(Architecture architecture) {
        super(architecture);
        this.methodBuilder = new MethodBuilder(architecture);
    }

    @Override
    protected Interface buildElement(NamedElement modelElement) {

        Interface interfacee = new Interface(architecture.getRelationshipHolder(), name, variantType, modelElement.getNamespace().getQualifiedName(), XmiHelper.getXmiId(modelElement));

        List<Operation> elements = ((org.eclipse.uml2.uml.Class) modelElement).getOperations();

        for (Operation operation : elements)
            interfacee.addExternalOperation(methodBuilder.create(operation));

        interfacee.setPatternOperations(new PatternsOperations(StereotypeHelper.getAllPatternsStereotypes(modelElement)));
        return interfacee;
    }

}
