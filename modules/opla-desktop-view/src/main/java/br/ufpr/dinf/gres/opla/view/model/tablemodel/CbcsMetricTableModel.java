package br.ufpr.dinf.gres.opla.view.model.tablemodel;

import br.ufpr.dinf.gres.opla.entity.metric.CbcsMetric;

/**
 * @author Fernando
 */
public class CbcsMetricTableModel extends AbstractMetricTableModel {

    private static final long serialVersionUID = 1L;

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        String[] colunas = {"cbcs"};
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CbcsMetric obj = (CbcsMetric) lista.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return obj.getCbcs();
        }

        return obj;
    }

}
