package br.ufpr.dinf.gres.opla.view.analysis;

import java.util.List;

public class BoxPlotItem {

    private List<Double> items;
    private String rowKey;
    private String columnKey;

    public BoxPlotItem() {
    }

    public BoxPlotItem(List items, String rowKey, String columnKey) {
        this.items = items;
        this.rowKey = rowKey;
        this.columnKey = columnKey;
    }

    public List<Double> getItems() {
        return items;
    }

    public void setItems(List<Double> items) {
        this.items = items;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }
}
