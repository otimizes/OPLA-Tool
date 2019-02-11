package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import java.util.List;

import br.ufpr.dinf.gres.opla.entity.metric.GenericMetric;
import br.ufpr.dinf.gres.opla.view.model.TableModelBase;

/**
 * 
 * @author Fernando
 *
 */
public abstract class AbstractMetricTableModel extends TableModelBase<GenericMetric> {

	private static final long serialVersionUID = 1L;

	@Override
	public void setLista(List<GenericMetric> lista) {
		this.lista = lista;
	}

}
