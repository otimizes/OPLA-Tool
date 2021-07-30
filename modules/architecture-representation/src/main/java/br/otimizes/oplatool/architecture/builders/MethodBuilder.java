package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.ParameterMethod;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder responsible for creating element of type Method.
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class MethodBuilder extends ElementBuilder<Method> {

    public MethodBuilder(Architecture architecture) {
        super(architecture);
    }

    /**
     * Creates new method
     */
    @Override
    protected Method buildElement(NamedElement modelElement) {
        try {
            Operation method = ((Operation) modelElement);
            Type methodReturnType = method.getType();
            String type = methodReturnType != null ? methodReturnType.getName() : "";
            boolean isAbstract;
            List<ParameterMethod> parameterMethodReceives = new ArrayList<>();
            isAbstract = method.isAbstract();
            EList<Parameter> params = method.getOwnedParameters();
            try {
                for (Parameter parameter : params) {
                    if (parameter.getDirection().getName().equals("out"))
                        type = parameter.getType().getName();
                    parameterMethodReceives.add(new ParameterMethod(parameter.getName(), parameter.getType().getName(),
                            parameter.getDirection().getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String namespace = modelElement.getNamespace().getQualifiedName();

            return new Method(name, variantType, type, isAbstract, parameterMethodReceives, namespace,
                    XmiHelper.getXmiId(modelElement));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}