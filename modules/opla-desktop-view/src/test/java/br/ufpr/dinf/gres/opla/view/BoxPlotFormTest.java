package br.ufpr.dinf.gres.opla.view;

import br.ufpr.dinf.gres.opla.view.analysis.BoxPlot;
import br.ufpr.dinf.gres.opla.view.analysis.BoxPlotItem;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class BoxPlotFormTest {

    public static void main(String... args) {

        List<BoxPlotItem> boxPlotItems = Arrays.asList(
                new BoxPlotItem(Arrays.asList(100, 99, 96, 55, 65, 76, 81, 80, 71, 59, 44, 34), "Planet", "Endor"),
                new BoxPlotItem(Arrays.asList(100, 99, 96, 55, 65, 76, 81, 80, 71, 59, 44, 34), "Planet", "Oite"),
                new BoxPlotItem(Arrays.asList(22, 25, 34, 44, 54, 63, 69, 1, 59, 48, 38, 1), "Planet", "Hoth"));

        EventQueue.invokeLater(new BoxPlot(boxPlotItems)::display);
    }
}
