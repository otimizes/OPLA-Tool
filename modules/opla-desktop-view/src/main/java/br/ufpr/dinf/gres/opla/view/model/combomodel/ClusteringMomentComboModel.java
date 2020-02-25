package br.ufpr.dinf.gres.opla.view.model.combomodel;

import br.ufpr.dinf.gres.opla.view.model.ComboModelBase;
import learning.Moment;

import java.util.Arrays;

/**
 * @author WmfSystem
 */
public class ClusteringMomentComboModel extends ComboModelBase<Moment> {

    public ClusteringMomentComboModel() {
        super(Arrays.asList(Moment.values()));
    }
}
