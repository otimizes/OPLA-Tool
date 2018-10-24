package br.ufpr.dinf.gres.opla.view.model.combomodel;

import java.util.List;

import br.ufpr.dinf.gres.opla.view.model.ComboModelBase;

public class ObjectiveNameComboModel extends ComboModelBase<String> {

	public ObjectiveNameComboModel(List<String> values) {
		super(values);
	}

	public void setList(List<String> names) {
		this.list = names;
	}

}
