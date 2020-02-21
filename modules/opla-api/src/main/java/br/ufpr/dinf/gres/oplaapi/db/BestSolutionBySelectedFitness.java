package br.ufpr.dinf.gres.oplaapi.db;

import br.ufpr.dinf.gres.loglog.Logger;
import metrics.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.*;
import java.util.Map.Entry;

public class BestSolutionBySelectedFitness {

    public static List<Metrics> calculateBestFeatureDriven(String experimentId) {
        return Database.getAllFeatureDrivenMetricsForExperimentId(experimentId);
    }

    public static List<Metrics> calculateBestElegance(String experimentId) {
        return Database.getAllEleganceMetricsForExperimentId(experimentId);

    }

    public static List<Metrics> calculateBestConventional(String experimentId) {
        return Database.getAllConventionalMetricsForExperimentId(experimentId);

    }

    public static List<Metrics> calculateBestPlaExt(String experimentId) {
        return Database.getAllPLAExtMetricsForExperimentId(experimentId);
    }

    //---addYni---

    public static List<Metrics> calculateBestWocsC(String experimentId) {
        return Database.getAllWocsCMetricsForExperimentId(experimentId);
    }

    public static List<Metrics> calculateBestWocsI(String experimentId) {
        return Database.getAllWocsIMetricsForExperimentId(experimentId);
    }

    public static List<Metrics> calculateBestCbcs(String experimentId) {
        return Database.getAllCbcsMetricsForExperimentId(experimentId);
    }

    public static List<Metrics> calculateBestSvc(String experimentId) {
        return Database.getAllSvcMetricsForExperimentId(experimentId);
    }

    public static List<Metrics> calculateBestSsc(String experimentId) {
        return Database.getAllSscMetricsForExperimentId(experimentId);
    }

    public static List<Metrics> calculateBestAv(String experimentId) {
        return Database.getAllAvMetricsForExperimentId(experimentId);
    }
    //---addYni---

    public static void buildTable(JTable tableMinorFitnessValues, List<Metrics> map) {

        Object[][] data = new Object[map.size()][map.size()];

        for (int i = 0; i < map.size(); i++) {
            data[i] = new String[]{Database.getNameSolutionById(map.get(i).getIdSolution()), String.valueOf(getValueFitness(map.get(i)))};
        }

        String columnNames[] = {"Solution Name", "Value"};

        TableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };

        tableMinorFitnessValues.setModel(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        tableMinorFitnessValues.setRowSorter(sorter);

    }

    public static void buildTableObjectives(JTable tableObjectives, Map<String, String> result) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Objective");
        model.addColumn("Value");
        tableObjectives.setModel(model);

        Iterator<Entry<String, String>> it = result.entrySet().iterator();
        while (it.hasNext()) {
            Object[] row = new Object[2];
            Entry<String, String> pairs = (Entry<String, String>) it.next();
            row[1] = pairs.getValue();
            row[0] = pairs.getKey();
            it.remove(); // evitar ConcurrentModificationException
            model.addRow(row);
        }

        tableObjectives.updateUI();
    }

    private static SortedMap<String, Metrics> buildMapElegance(List<Elegance> data) {
        SortedMap<String, Metrics> map = new TreeMap<>();
        for (int i = 0; i < data.size(); i++) {
            Elegance elegance = data.get(i);
            map.put(elegance.getIdSolution(), elegance);
        }
        return map;
    }

    private static SortedMap<String, Metrics> buildMapConventional(List<Conventional> data) {
        SortedMap<String, Metrics> map = new TreeMap();
        for (int i = 0; i < data.size(); i++) {
            Conventional conventional = data.get(i);
            map.put(conventional.getIdSolution(), conventional);
        }
        return map;
    }

    private static SortedMap<String, Metrics> buildMapFeatureDriven(List<FeatureDriven> data) {
        SortedMap<String, Metrics> map = new TreeMap();
        for (int i = 0; i < data.size(); i++) {
            FeatureDriven fd = data.get(i);
            map.put(fd.getIdSolution(), fd);
        }
        return map;
    }

    private static SortedMap<String, Metrics> buildMapPLAExt(List<PLAExtensibility> data) {
        SortedMap<String, Metrics> map = new TreeMap();
        for (int i = 0; i < data.size(); i++) {
            PLAExtensibility plaExt = data.get(i);
            map.put(plaExt.getIdSolution(), plaExt);
        }
        return map;
    }

    private static Double getValueFitness(Metrics f) {
        if (f instanceof Conventional) {
            return ((Conventional) f).evaluateMACFitness();
        } else if (f instanceof FeatureDriven) {
            return ((FeatureDriven) f).evaluateMSIFitness();
        } else if (f instanceof PLAExtensibility) {
            return ((PLAExtensibility) f).getPlaExtensibility();
        } else if (f instanceof Elegance) {
            return ((Elegance) f).evaluateEleganceFitness();
        }
        //---addYni
        else if (f instanceof Svc) {
            return ((Svc) f).getSvc();
            //---addYni
        } else if (f instanceof Elegance) {
            return ((Elegance) f).evaluateEleganceFitness();
        } else {
            Logger.getLogger().putLog("I dont know " + f.getClass().getName());
            throw new IllegalArgumentException("I dont know " + f.getClass().getName());
        }
    }


}
