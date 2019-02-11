package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.SscMetric;

/**
 * @author Fernando
 */
public class SscMetricTableModel extends AbstractMetricTableModel {

    private static final long serialVersionUID = 1L;

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        String[] colunas = {"ssc"};
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SscMetric obj = (SscMetric) lista.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return obj.getSsc();
        }

        return obj;
    }

}
