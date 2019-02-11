package jmetal4.experiments;

import java.util.List;

public class OPLAConfigs {

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
