package br.ufpr.dinf.gres.opla.view.model;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 * @param <T>
 * @author Fernando
 */
@SuppressWarnings("rawtypes")
public abstract class ComboModelBase<T> implements ComboBoxModel {

	protected List<T> list;

	private T selected;

	public ComboModelBase(List<T> values) {
		cleanList();
		if (values != null) {
			this.list = values;
		}
	}

	private void cleanList() {
		if (list != null) {
			list.clear();
		}
	}

	@Override
	public T getSelectedItem() {
		return selected;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setSelectedItem(Object anItem) {
		this.selected = (T) anItem;
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public T getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}

}
