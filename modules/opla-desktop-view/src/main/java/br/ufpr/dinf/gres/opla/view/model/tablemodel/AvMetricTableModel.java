package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.AvMetric;

/**
 * 
 * @author Fernando
 *
 */
public class AvMetricTableModel extends AbstractMetricTableModel {

	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(int column) {
		String[] colunas = { "av" };
		return colunas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		AvMetric obj = (AvMetric) lista.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return obj.getAv();
		}

		return obj;
	}

}
