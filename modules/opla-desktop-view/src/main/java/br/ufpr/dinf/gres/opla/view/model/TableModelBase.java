package br.ufpr.dinf.gres.opla.view.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @param <T>
 * @author Fernando
 */
public abstract class TableModelBase<T> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	protected List<T> lista;

	/**
	 * Number of Columns for table
	 *
	 * @return
	 */
	@Override
	public abstract int getColumnCount();

	/**
	 * Name of Columns
	 *
	 * @param column
	 * @return
	 */
	@Override
	public abstract String getColumnName(int column);

	/**
	 * Value of Column
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);

	@Override
	public int getRowCount() {
		if (lista == null) {
			return 0;
		}
		return lista.size();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public void setLista(List<T> lista) {
		this.lista = lista;
	}

	public T getValue(int index) {
		return this.lista.get(index);
	}

}
