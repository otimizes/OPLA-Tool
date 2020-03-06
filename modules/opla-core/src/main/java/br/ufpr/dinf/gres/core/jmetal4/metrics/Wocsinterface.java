package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Method;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;
import br.ufpr.dinf.gres.core.persistence.IPersistentDto;
import br.ufpr.dinf.gres.domain.entity.metric.WocsinterfaceMetric;

public class Wocsinterface extends Metrics implements IPersistentDto<WocsinterfaceMetric> {

    private double wocsinterface;
    private float results;

    public Wocsinterface(Architecture architecture) {

        this.results = 0;
        float valorwocsi = 0;
        float tcomplexidade = 0;
        float numclass = architecture.getAllInterfaces().size();

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
        this.results = valorwocsi;

    }

    public float getResults() {

        return results;
    }

    public Wocsinterface(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getWocsInterface() {
        return wocsinterface;
    }

    public void setWocsInterface(double wocsinterface) {
        this.wocsinterface = wocsinterface;
    }

    @Override
    public WocsinterfaceMetric newPersistentInstance() {
        WocsinterfaceMetric metric = new WocsinterfaceMetric();
        metric.setExecution(this.getExecutionResults().newPersistentInstance());
        metric.setExperiment(this.getExperiement().newPersistentInstance());
        metric.setId(Long.valueOf(this.getIdSolution()));
        metric.setIsAll(this.getIsAll());
        metric.setWocsinterface(String.valueOf(this.getWocsInterface()));
        return metric;
    }


}
