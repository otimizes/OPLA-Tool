package br.ufpr.dinf.gres.opla.view.model.combomodel;

import br.ufpr.dinf.gres.opla.view.model.ComboModelBase;
import jmetal4.core.learning.ClusteringAlgorithm;

import java.util.Arrays;

/**
 * @author WmfSystem
 */
public class ClusteringAlgorithmComboModel extends ComboModelBase<ClusteringAlgorithm> {

    public ClusteringAlgorithmComboModel() {
        super(Arrays.asList(ClusteringAlgorithm.values()));
    }
}
