package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import java.util.List;

import br.ufpr.dinf.gres.opla.entity.Execution;
import br.ufpr.dinf.gres.opla.view.model.TableModelBase;
import br.ufpr.dinf.gres.opla.view.util.DateUtil;
import br.ufpr.dinf.gres.persistence.dao.ObjectiveDAO;

/**
 * @author Fernando
 */
public class ExecutionTableModel extends TableModelBase<Execution> {

	private static final long serialVersionUID = 1L;

	private ObjectiveDAO objectiveDAO;
	private Long numberNonDominatedSoluction;

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		String[] colunas = { "Run", "Time (min:seg)", "Genr. Solutions", "Non Dominated Solutions" };
		return colunas[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Execution obj = lista.get(rowIndex);

		Long numberSoluction = objectiveDAO.countAllSoluctions(obj.getExperiment(), obj);

		switch (columnIndex) {
		case 0:
			return obj.getId();
		case 1:
			return DateUtil.toMinutesAndSeconds(obj.getTime());
		case 2:
			return Math.abs(numberSoluction - numberNonDominatedSoluction);
		case 3:
			return numberNonDominatedSoluction;

		}
		return obj;
	}

	public void setData(List<Execution> lista, Long numberNonDominatedSoluction) {
		cleanList();
		this.lista = lista;
		this.numberNonDominatedSoluction = numberNonDominatedSoluction;
		this.objectiveDAO = new ObjectiveDAO();
	}

	private void cleanList() {
		if (this.lista != null) {
			this.lista.clear();
		}
	}

}
