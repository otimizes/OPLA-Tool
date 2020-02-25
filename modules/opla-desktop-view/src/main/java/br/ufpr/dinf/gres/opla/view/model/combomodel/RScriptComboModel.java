package br.ufpr.dinf.gres.opla.view.model.combomodel;

import br.ufpr.dinf.gres.opla.view.model.ComboModelBase;
import jmetal4.util.RScriptOption;

import java.util.Arrays;

/**
 * @author WmfSystem
 */
public class RScriptComboModel extends ComboModelBase<RScriptOption> {

    public RScriptComboModel() {
        super(Arrays.asList(RScriptOption.values()));
    }
}
