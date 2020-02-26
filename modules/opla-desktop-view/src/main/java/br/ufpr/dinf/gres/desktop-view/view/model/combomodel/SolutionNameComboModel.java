package br.ufpr.dinf.gres.domain.view.model.combomodel;

import java.util.List;

import br.ufpr.dinf.gres.domain.view.model.ComboModelBase;

/**
 *
 * @author Fernando
 */
public class SolutionNameComboModel extends ComboModelBase<String> {

	public SolutionNameComboModel(List<String> values) {
		super(values);
	}

	public void setList(List<String> list) {
		this.list = list;
	}

}
