package br.ufpr.dinf.gres.opla.view.analysis;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * wmfsystem
 */
public class BoxPlot {

    private List<BoxPlotItem> items = new ArrayList<>();
    private String title = "BoxPlot";
    private String categoryAxys = "Type";
    private String numberAxis = "Value";

    public BoxPlot(List<BoxPlotItem> items) {
        this.items = items;
    }

    public void display() {
        JFrame f = new JFrame(title);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultBoxAndWhiskerCategoryDataset boxData = new DefaultBoxAndWhiskerCategoryDataset();
        this.items.forEach(item -> boxData.add(item.getItems(), item.getRowKey(), item.getColumnKey()));

        BoxAndWhiskerRenderer boxRenderer = new BoxAndWhiskerRenderer();
        boxRenderer.setWhiskerWidth(0.5);
        StandardCategoryToolTipGenerator standardCategoryToolTipGenerator = new StandardCategoryToolTipGenerator();
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        defaultCategoryDataset.setValue(boxData.getValue(0, 0).doubleValue(), boxData.getRowKey(0), boxData.getColumnKey(0));
        standardCategoryToolTipGenerator.generateToolTip(defaultCategoryDataset, 0, 0);
        boxRenderer.setToolTipGenerator(standardCategoryToolTipGenerator);
        boxRenderer.setMeanVisible(false);

        DefaultCategoryDataset catData = new DefaultCategoryDataset();
//        catData.addValue(boxData.getMeanValue(0, 0), "Median", boxData.getColumnKey(0));
//        catData.addValue(boxData.getMeanValue(0, boxData.getColumnCount()-1), "Median", boxData.getColumnKey(1));

        LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
        CategoryAxis xAxis = new CategoryAxis(categoryAxys);
        NumberAxis yAxis = new NumberAxis(numberAxis);
        yAxis.setAutoRangeIncludesZero(false);

        CategoryPlot plot = new CategoryPlot(boxData, xAxis, yAxis, boxRenderer);
        plot.setDataset(1, catData);
        plot.setRenderer(1, lineRenderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        f.add(new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(620, 480);
            }
        });
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public List<BoxPlotItem> getItems() {
        return items;
    }

    public void setItems(List<BoxPlotItem> items) {
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}