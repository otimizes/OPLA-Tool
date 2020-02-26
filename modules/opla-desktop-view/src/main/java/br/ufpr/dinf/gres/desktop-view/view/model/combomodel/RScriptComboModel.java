package br.ufpr.dinf.gres.domain.view.model.combomodel;

import br.ufpr.dinf.gres.domain.view.model.ComboModelBase;
import br.ufpr.dinf.gres.core.jmetal4.util.RScriptOption;

import java.util.Arrays;

/**
 * @author WmfSystem
 */
public class RScriptComboModel extends ComboModelBase<RScriptOption> {

    public RScriptComboModel() {
        super(Arrays.asList(RScriptOption.values()));
    }
}
