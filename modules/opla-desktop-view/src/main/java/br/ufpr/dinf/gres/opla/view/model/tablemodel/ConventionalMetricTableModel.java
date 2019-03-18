package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.ConventionalMetric;

/**
 * 
 * @author Fernando
 *
 */
public class ConventionalMetricTableModel extends AbstractMetricTableModel {

	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(int column) {
		String[] columas = { "macAggregation", "choesion", "meanDepComps", "meanNumOps", "sumClassesDepIn",
				"sumClassesDepOut", "sumDepIn", "sumDepOut" };
		return columas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ConventionalMetric obj = (ConventionalMetric) lista.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return obj.getMacAggregation();
		case 1:
			return obj.getCohesion();
		case 2:
			return obj.getMeanDepComps();
		case 3:
			return obj.getMeanNumOps();
		case 4:
			return obj.getSumClassesDepIn();
		case 5:
			return obj.getSumClassesDepOut();
		case 6:
			return obj.getSumDepIn();
		case 7:
			return obj.getSumDepOut();
		}

		return obj;
	}

}
