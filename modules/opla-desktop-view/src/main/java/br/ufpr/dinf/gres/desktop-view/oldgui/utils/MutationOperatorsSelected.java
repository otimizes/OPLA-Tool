package br.ufpr.dinf.gres.domain.oldgui.utils;

import java.util.ArrayList;
import java.util.List;

public class MutationOperatorsSelected {

    private static List<String> selectedMutationOperators = new ArrayList<>();
    private static List<String> selectedPatternsToApply = new ArrayList<>();

    /**
     * Lista de operadores de mutação (Thelma) selecionados na aba "Execution Configurations"
     *
     * @return
     */
    public static List<String> getSelectedMutationOperators() {
        return selectedMutationOperators;
    }

    /**
     * Lista da br.ufpr.dinf.gres.patterns selecinados na aba "Design Patterns"
     *
     * @return
     */
    public static List<String> getSelectedPatternsToApply() {
        return selectedPatternsToApply;
    }
}
