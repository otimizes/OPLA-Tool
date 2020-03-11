package br.ufpr.dinf.gres.core.jmetal4.metrics.classical;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Method;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class WOCSClass extends BaseMetricResults {

    public WOCSClass(Architecture architecture) {
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
