package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.FeatureDrivenMetric;

/**
 * 
 * @author Fernando
 *
 */
public class FeatureMetricTableModel extends AbstractMetricTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return 11;
	}

	@Override
	public String getColumnName(int column) {
		String[] columas = { "msiAggregation", "cdac ", "cdai", "cdao", "cibc", "iibc", "oobc", "lcc", "lccClass",
				"cdaClass", "cibClass" };
		return columas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		FeatureDrivenMetric obj = (FeatureDrivenMetric) lista.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return obj.getMsiAggregation();
		case 1:
			return obj.getCdac();
		case 2:
			return obj.getCdai();
		case 3:
			return obj.getCdao();
		case 4:
			return obj.getCibc();
		case 5:
			return obj.getIibc();
		case 6:
			return obj.getOobc();
		case 7:
			return obj.getLcc();
		case 8:
			return obj.getLccClass();
		case 9:
			return obj.getCdaClass();
		case 10:
			return obj.getCibClass();
		}

		return obj;
	}

}
