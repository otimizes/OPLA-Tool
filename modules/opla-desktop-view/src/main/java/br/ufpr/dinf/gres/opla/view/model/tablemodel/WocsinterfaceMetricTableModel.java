package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.WocsinterfaceMetric;

/**
 * @author Fernando
 */
public class WocsinterfaceMetricTableModel extends AbstractMetricTableModel {

    private static final long serialVersionUID = 1L;

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        String[] colunas = {"wocsinterface"};
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        WocsinterfaceMetric obj = (WocsinterfaceMetric) lista.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return obj.getWocsinterface();
        }

        return obj;
    }

}
