package br.ufpr.dinf.gres.domain.view.model.combomodel;

import br.ufpr.dinf.gres.domain.view.model.ComboModelBase;
import br.ufpr.dinf.gres.core.learning.ClusteringAlgorithm;

import java.util.Arrays;

/**
 * @author WmfSystem
 */
public class ClusteringAlgorithmComboModel extends ComboModelBase<ClusteringAlgorithm> {

    public ClusteringAlgorithmComboModel() {
        super(Arrays.asList(ClusteringAlgorithm.values()));
    }
}
