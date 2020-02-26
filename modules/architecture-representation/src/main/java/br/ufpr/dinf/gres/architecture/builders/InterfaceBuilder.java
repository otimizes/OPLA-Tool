package br.ufpr.dinf.gres.architecture.builders;

import br.ufpr.dinf.gres.architecture.helpers.StereotypeHelper;
import br.ufpr.dinf.gres.architecture.helpers.XmiHelper;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.PatternsOperations;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;

import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class InterfaceBuilder extends ElementBuilder<br.ufpr.dinf.gres.architecture.representation.Interface> {

    private MethodBuilder methodBuilder;

    public InterfaceBuilder(Architecture architecture) {
        super(architecture);
        this.methodBuilder = new MethodBuilder(architecture);
    }

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
