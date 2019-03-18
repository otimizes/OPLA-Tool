package br.ufpr.dinf.gres.opla.view.model.combomodel;

import java.util.Arrays;

import br.ufpr.dinf.gres.opla.view.enumerators.AlgorithmType;
import br.ufpr.dinf.gres.opla.view.model.ComboModelBase;

/**
 * @author Fernando
 */
public class AlgorithmComboModel extends ComboModelBase<AlgorithmType> {

    public AlgorithmComboModel() {
        super(Arrays.asList(AlgorithmType.values()));
    }
}
