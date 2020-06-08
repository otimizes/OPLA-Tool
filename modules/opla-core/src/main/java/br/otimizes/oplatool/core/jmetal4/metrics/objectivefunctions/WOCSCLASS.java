package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Weighted Operations per Component or Service (Ribeiro et al., 2010)
 * <p>
 * It measures the complexity of the service or component, in terms of
 * its operations (methods) that will be needed for other services or
 * components; where service is defined as a function without a state,
 * independent, that accepts a call (s) and returns a response (s)
 * through a well-defined interface. The metric indicates that operations
 * with many formal parameters are more likely to be complex than operations
 * that require fewer parameters.
 */
public class WOCSCLASS extends ObjectiveFunctionImplementation {

    public WOCSCLASS(Architecture architecture) {
        super(architecture);
        double valorwocsc;
        double tcomplexidade = 0;
        double numclass = architecture.getAllClasses().size();

        for (Package pacote : architecture.getAllPackages()) {
            for (Class classes : pacote.getAllClasses()) {
                int cantparame = 0;
                int complexidade = 0;


                for (Method metodo : classes.getAllMethods()) {

                    cantparame = metodo.getParameters().size() + 1;
                    complexidade += cantparame;
                }

                tcomplexidade = complexidade;
            }
        }
        valorwocsc = tcomplexidade / numclass;
        this.setResults(valorwocsc);
    }

}
