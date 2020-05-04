package arquitetura.representation;

import arquitetura.helpers.UtilResources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Method extends Element {

    private static final long serialVersionUID = -2564107958435833184L;
    private final List<ParameterMethod> parameters = new ArrayList<ParameterMethod>();
    private String returnType;
    private boolean isAbstract;
    private boolean isConstructor;
    private String visibility;

    public Method(String name, Variant variantType, String returnType, boolean isAbstract, List<ParameterMethod> paramsMethod, String namespace, String id) {
        super(name, variantType, "method", namespace, id);
        setReturnType(returnType);
        setAbstract(isAbstract);
        setParams(paramsMethod);
    }

    public Method(String name, Boolean isVariationPoint, VariantType variantType, String returnType, boolean isAbstract, List<ParameterMethod> paramsMethod, String namespace, String id) {
        this(name, null, returnType, isAbstract, paramsMethod, namespace, id);
    }

    public Method(String name, String type, String className, boolean isAbstract, String id) {
        super(name, null, "method", UtilResources.createNamespace(ArchitectureHolder.getName(), className), id);
        setReturnType(type);
        setAbstract(isAbstract);
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    private void setParams(List<ParameterMethod> paramsMethod) {
        if (paramsMethod != null)
            parameters.addAll(paramsMethod);
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public String getReturnType() {
        return returnType;
    }

    private void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<ParameterMethod> getParameters() {
        return parameters;
    }

    @Override
    public Collection<Concern> getAllConcerns() {
        return new ArrayList<Concern>(getOwnConcerns());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            final Method other = (Method) obj;
            if ((this.getId() == null) ? (other.getId() == null) : this
                    .getId().equals(other.getId())) {
                if ((this.getName() == null) ? (other.getName() == null) : this
                        .getName().equals(other.getName())) {
                    if ((this.returnType == null) ? (other.returnType == null)
                            : this.returnType.equals(other.returnType)) {
                        return this.parameters == other.parameters
                                || (this.parameters != null && this.parameters
                                .equals(other.parameters));
                    }
                }
            }
        }
        return false;
    }
}