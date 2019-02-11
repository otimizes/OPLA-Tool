package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.WocsclassMetric;

/**
 * @author Fernando
 */
public class WocsclassMetricTableModel extends AbstractMetricTableModel {

    private static final long serialVersionUID = 1L;

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        String[] colunas = {"wocsclass"};
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        WocsclassMetric obj = (WocsclassMetric) lista.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return obj.getWocsclass();
        }

        return obj;
    }

}
