package br.otimizes.oplatool.core.learning.mlmodels;

import weka.core.SelectedTag;

public class KStarLearningModel {
    private boolean entropicAutoBlend;
    private int globalBlend;
    private SelectedTag missingMode;

    public KStarLearningModel(boolean entropicAutoBlend, int globalBlend, SelectedTag missingMode) {
        this.entropicAutoBlend = entropicAutoBlend;
        this.globalBlend = globalBlend;
        this.missingMode = missingMode;
    }

    public boolean isEntropicAutoBlend() {
        return entropicAutoBlend;
    }

    public void setEntropicAutoBlend(boolean entropicAutoBlend) {
        this.entropicAutoBlend = entropicAutoBlend;
    }

    public int getGlobalBlend() {
        return globalBlend;
    }

    public void setGlobalBlend(int globalBlend) {
        this.globalBlend = globalBlend;
    }

    public SelectedTag getMissingMode() {
        return missingMode;
    }

    public void setMissingMode(SelectedTag missingMode) {
        this.missingMode = missingMode;
    }
}
