package br.ufpr.dinf.gres.domain.view.model.combomodel;

import br.ufpr.dinf.gres.domain.view.model.ComboModelBase;
import br.ufpr.dinf.gres.core.learning.Moment;

import java.util.Arrays;

/**
 * @author WmfSystem
 */
public class ClusteringMomentComboModel extends ComboModelBase<Moment> {

    public ClusteringMomentComboModel() {
        super(Arrays.asList(Moment.values()));
    }
}
