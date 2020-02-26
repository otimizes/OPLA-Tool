package br.ufpr.dinf.gres.core.jmetal4.experiments;

import java.util.List;

public class OPLAConfigs {

    public OPLAConfigs() {
    }

    public OPLAConfigs(List<String> selectedObjectiveFunctions) {
        this.selectedObjectiveFunctions = selectedObjectiveFunctions;
    }

    private List<String> selectedObjectiveFunctions;

    public Integer getNumberOfObjectives() {
        return selectedObjectiveFunctions.size();
    }

    public List<String> getSelectedObjectiveFunctions() {
        return selectedObjectiveFunctions;
    }

    public void setSelectedObjectiveFunctions(List<String> selectedMetrics) {
        this.selectedObjectiveFunctions = selectedMetrics;
    }

}
