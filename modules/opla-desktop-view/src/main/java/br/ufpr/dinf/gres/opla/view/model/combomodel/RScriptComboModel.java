package br.ufpr.dinf.gres.opla.view.model.combomodel;

import br.ufpr.dinf.gres.opla.view.model.ComboModelBase;
import utils.RScriptOption;

import java.util.Arrays;

/**
 * @author WmfSystem
 */
public class RScriptComboModel extends ComboModelBase<RScriptOption> {

    public RScriptComboModel() {
        super(Arrays.asList(RScriptOption.values()));
    }
}
