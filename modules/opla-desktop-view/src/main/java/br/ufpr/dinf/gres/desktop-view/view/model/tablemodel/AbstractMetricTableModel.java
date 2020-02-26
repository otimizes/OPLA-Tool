package br.ufpr.dinf.gres.domain.view.model.tablemodel;

import java.util.List;

import br.ufpr.dinf.gres.domain.entity.metric.GenericMetric;
import br.ufpr.dinf.gres.domain.view.model.TableModelBase;

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
