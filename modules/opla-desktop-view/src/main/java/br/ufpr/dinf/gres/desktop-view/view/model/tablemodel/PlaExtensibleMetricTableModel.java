package br.ufpr.dinf.gres.domain.view.model.tablemodel;

import br.ufpr.dinf.gres.domain.entity.metric.PLAExtensibilityMetric;

/**
 * 
 * @author Fernando
 *
 */
public class PlaExtensibleMetricTableModel extends AbstractMetricTableModel {

	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int column) {
		String[] colunas = { "PLAExtensibility" };
		return colunas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		PLAExtensibilityMetric obj = (PLAExtensibilityMetric) lista.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return obj.getPlaExtensibility();
		}
		return obj;
	}

}
