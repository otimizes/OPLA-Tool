package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Method;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;


public class Wocsclass extends Metrics {

    private float wocsclass;

    private float results;

    public Wocsclass(Architecture architecture) {

        this.results = 0;
        float valorwocsc = 0;
        float tcomplexidade = 0;
        float numclass =  architecture.getAllClasses().size();

        for(Package pacote : architecture.getAllPackages()){

            for(Class classes : pacote.getAllClasses()){
                int cantparame = 0;
                int complexidade = 0;


                for(Method metodo : classes.getAllMethods()){

                    cantparame = metodo.getParameters().size() + 1;
                    complexidade += cantparame;
                }

                tcomplexidade = complexidade;
            }
        }
        valorwocsc = tcomplexidade / numclass;
        this.results = valorwocsc;

    }

    public float getResults() {
        return results;
    }

    public Wocsclass(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public float getWocsClass() {
        return wocsclass;
    }

    public void setWocsClass(float wocsclass) {
        this.wocsclass = wocsclass;
    }


}
