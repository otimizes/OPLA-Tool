package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.dinf.gres.opla.entity.MapObjectiveName;
import br.ufpr.dinf.gres.opla.view.model.TableModelBase;

/**
 * @author Fernando
 */
public class MapObjectiveNameTableModel extends TableModelBase<MapObjectiveName> {

	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return 2;
	}

	public MapObjectiveNameTableModel() {
		this.lista = new ArrayList<>();
	}

	@Override
	public String getColumnName(int column) {
		String[] colunas = new String[] { "Objective Function", "Value" };
		return colunas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		MapObjectiveName obj = lista.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return obj.getNames();
		case 1:
			return obj.getValue();
		}
		return obj;
	}

	public void setData(List<BigDecimal> objectiveValues, List<String> listNames) {
		lista.clear();
		if (!objectiveValues.isEmpty()) {
			for (int x = 0; x <= objectiveValues.size() - 1; x++) {
				lista.add(new MapObjectiveName(objectiveValues.get(x), listNames.get(x)));
			}
		}
	}

}
