package br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions;

import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionImplementation;
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
public class CS extends ObjectiveFunctionImplementation {

    public CS(Architecture architecture) {
        super(architecture);
        double valorwocsi;
        double tcomplexidade = 0;
        double numclass = architecture.getAllInterfaces().size();

        for (Package pacote : architecture.getAllPackages()) {

            for (Interface interfa : pacote.getAllInterfaces()) {
                int cantparame = 0;
                int complexidade = 0;

                for (Method metodo : interfa.getMethods()) {

                    cantparame = metodo.getParameters().size() + 1;
                    complexidade += cantparame;
                }

                tcomplexidade = complexidade;

            }

        }
        valorwocsi = tcomplexidade / numclass;
        this.setResults(valorwocsi);
    }

}
