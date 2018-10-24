package arquitetura.builders;

import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Method;
import arquitetura.representation.ParameterMethod;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder resposável por criar element do tipo Método.
 *
 * @author edipofederle<edipofederle@gmail.com>
 */
public class MethodBuilder extends ElementBuilder<Method> {

    public MethodBuilder(Architecture architecture) {
        super(architecture);
    }

    /**
     * Cria um elemento do tipo {@link Method}
     */
    @Override
    protected Method buildElement(NamedElement modelElement) {
        try {
            Operation method = ((Operation) modelElement);
            Type methodReturnType = method.getType();
            String type = methodReturnType != null ? methodReturnType.getName() : "";
            boolean isAbstract = false;

            List<ParameterMethod> parameterMethodReceives = new ArrayList<ParameterMethod>();

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

            Method m = new Method(name, variantType, type, isAbstract, parameterMethodReceives, namespace,
                    XmiHelper.getXmiId(modelElement));
            return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}