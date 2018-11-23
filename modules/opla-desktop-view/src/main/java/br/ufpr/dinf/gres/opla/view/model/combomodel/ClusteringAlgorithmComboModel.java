package br.ufpr.dinf.gres.opla.view.model.combomodel;

import br.ufpr.dinf.gres.opla.view.model.ComboModelBase;
import learning.ClusteringAlgorithms;

import java.util.Arrays;

/**
 * @author WmfSystem
 */
public class ClusteringAlgorithmComboModel extends ComboModelBase<ClusteringAlgorithms> {

    public ClusteringAlgorithmComboModel() {
        super(Arrays.asList(ClusteringAlgorithms.values()));
    }
}
