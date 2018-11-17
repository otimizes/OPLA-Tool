package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.SvcMetric;

/**
 * @author Fernando
 */
public class SvcMetricTableModel extends AbstractMetricTableModel {

    private static final long serialVersionUID = 1L;

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        String[] colunas = {"svc"};
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SvcMetric obj = (SvcMetric) lista.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return obj.getSvc();
        }

        return obj;
    }

}
