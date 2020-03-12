package br.ufpr.dinf.gres.core.jmetal4.metrics.all;

import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class WOCSInterface extends BaseMetricResults {

    public WOCSInterface(Architecture architecture) {
        super(architecture);
        double valorwocsi;
        double tcomplexidade = 0;
        double numclass = architecture.getAllInterfaces().size();

        for (Package pacote : architecture.getAllPackages()) {

            for (Interface interfa : pacote.getAllInterfaces()) {
                int cantparame = 0;
                int complexidade = 0;

                for (Method metodo : interfa.getOperations()) {

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
