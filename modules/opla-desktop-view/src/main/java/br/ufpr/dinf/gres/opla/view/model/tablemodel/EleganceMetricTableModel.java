package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.EleganceMetric;
import br.ufpr.dinf.gres.opla.view.model.TableModelBase;

/**
 * 
 * @author Fernando
 *
 */
public class EleganceMetricTableModel extends AbstractMetricTableModel {

	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int column) {
		String[] columas = { "NAC", "ATMR", "EC" };
		return columas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		EleganceMetric obj = (EleganceMetric) lista.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return obj.getNac();
		case 1:
			return obj.getAtmr();
		case 2:
			return obj.getEc();
		}

		return obj;
	}

}
