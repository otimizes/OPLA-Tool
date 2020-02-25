package br.ufpr.dinf.gres.opla.view;

import arquitetura.representation.Architecture;
import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.opla.view.util.Utils;
import jmetal45.core.Solution;
import jmetal45.core.SolutionSet;
import jmetal45.experiments.NSGAIIConfig;
import jmetal45.experiments.OPLAConfigs;
import jmetal45.problems.OPLA;
import jmetal4.core.learning.Clustering;
import jmetal4.core.learning.ClusteringAlgorithm;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class InteractiveSolutions extends JDialog {

    private static final Logger LOGGER = Logger.getLogger(InteractiveSolutions.class);

    public static Integer currentExecution = 0;
    private ManagerApplicationConfig config;
    SolutionSet solutionSet;
    GridLayout gridLayout = new GridLayout();
    JPanel panelMaster = new JPanel(gridLayout);
    List<DefaultMutableTreeNode> objectNodes;
    LayoutManager layoutPanelSubjectiveAnalyse = new MigLayout();
    JPanel jPanelSubjectiveAnalysis = new JPanel(layoutPanelSubjectiveAnalyse);
    final JTextField field = new JTextField();
    ClusteringAlgorithm clusteringAlgorithm;
    String fileOnAnalyses;
    String plaNameOnAnalyses;
    Solution solutionOnAnalyses;
    JDialog dialog;
    StringBuilder logInteraction;
    Clustering clustering;
    JProgressBar progressBar = new JProgressBar();

    public InteractiveSolutions() {
    }

    public InteractiveSolutions(ManagerApplicationConfig config, ClusteringAlgorithm clusteringAlgorithm, SolutionSet solutionSet) {
        InteractiveSolutions.currentExecution++;
        this.config = config;
        this.solutionSet = solutionSet;
        this.clusteringAlgorithm = clusteringAlgorithm;
        setTitle("Architectures");
        setModal(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width - 500, screenSize.height - 300));
        setLocationByPlatform(true);

        try {
            paintTreeNode();
            getContentPane().revalidate();
            getContentPane().repaint();

            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    System.out.println("jdialog window closed event received");
                }

                public void windowClosing(WindowEvent e) {
                    Map<Double, Set<Integer>> clusterIds = solutionSet.getClusterIds();
                    AtomicBoolean complete = new AtomicBoolean(true);
                    clusterIds.forEach((k, v) -> {
                        if (v.stream().filter(vv -> vv > 0).count() <= 0) complete.set(false);
                    });
                    if (!complete.get()) {
                        JOptionPane.showMessageDialog(e.getComponent(), "Please, evaluate one solution by cluster.");
                    } else {
                        FileWriter fileWriter = null;
                        try {
                            fileWriter = new FileWriter(config.getApplicationYaml().getDirectoryToExportModels() + System.getProperty("file.separator") + "LogInteraction_" + InteractiveSolutions.currentExecution + ".txt");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        paintNodesBySolutionSet(solutionSet);

                        PrintWriter printWriter = new PrintWriter(fileWriter);
                        printWriter.print(logInteraction);
                        printWriter.close();
                        dispose();
                        setVisible(false);
                    }
                    System.out.println("jdialog window closing event received");
                }
            });
            pack();

            setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);

            // Assuming you have a tree of Strings
            Object objNode = ((DefaultMutableTreeNode) value).getUserObject();
            String node = (objNode instanceof String) ? (String) objNode : "";

            // If the node is a leaf and ends with "xxx"
            if (leaf && node.contains(":")) {
                // Paint the node in blue
                String search = field.getText();
                String text = value.toString();

                StringBuffer html = new StringBuffer("<html><body>");
                html.append(getText().split(":")[0] + ":");
                html.append("<b>" + getText().split(":")[1] + "</b>");
                html.append("</body></body>");

                return super.getTreeCellRendererComponent(
                        tree, html.toString(), sel, exp, leaf, row, hasFocus);
//                setForeground(new Color(13, 57 ,115));
            }

            return this;
        }
    }

    private void paintTreeNode() throws Exception {
        if (solutionSet.size() == 0) throw new RuntimeException("At least 1 solution is required.");

        clustering = new Clustering(solutionSet, this.clusteringAlgorithm);
        clustering.setNumClusters(solutionSet.getSolutionSet().get(0).numberOfObjectives() + 1);
        clustering.run();
        int numClusters = clustering.getGeneratedClusters();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Solutions");
        objectNodes = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            StringBuilder str = new StringBuilder();

            if (i == 0) str.append("Trade-off ");
            for (int j = 0; j < solutionSet.get(0).numberOfObjectives(); j++) {
                if (clustering.getMinClusterByObjective(j) == i)
                    str.append(solutionSet.get(0).getOPLAProblem().getSelectedMetrics().get(j)).append(" ");
            }
            if (str.length() <= 0) str.append("none");
            DefaultMutableTreeNode objMetric = new DefaultMutableTreeNode("Cluster " + i + ", Best Objects: " + str);
            objectNodes.add(objMetric);
            root.add(objMetric);
        }

        gridLayout.setRows(1);
        gridLayout.setColumns(2);
        JTree tree = new JTree(root);
        tree.setCellRenderer(new MyTreeCellRenderer());
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                update();
            }

            private void update() {
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.nodeStructureChanged((TreeNode) model.getRoot());
            }
        });
        tree.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JScrollPane scroll = new JScrollPane(tree);
        panelMaster.add(scroll);
        setContentPane(panelMaster);

        paintNodesBySolutionSet(solutionSet);

        int row = tree.getRowCount();
        while (row >= 0) {
            tree.expandRow(row);
            row--;
        }

        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    PopUp menu = new PopUp(tree);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void paintNodesBySolutionSet(SolutionSet solutionSet) {
        logInteraction = new StringBuilder();
        for (int i = 0; i < solutionSet.size(); i++) {
            String plaName = "TEMP_" + i + solutionSet.get(i).getOPLAProblem().getArchitecture_().getName();
            DefaultMutableTreeNode elem = new DefaultMutableTreeNode(plaName, true);
            logInteraction.append(elem.toString() + "\n");
//            DefaultMutableTreeNode elem0 = new DefaultMutableTreeNode(i, true);
//            elem.add(elem0);

            DefaultMutableTreeNode elem1 = new DefaultMutableTreeNode("Id: " + i, false);
            logInteraction.append(elem1.toString() + "\n");
            elem.add(elem1);

            String objectives = getObjectivesFormattedStr(i);

            DefaultMutableTreeNode elem2 = new DefaultMutableTreeNode(objectives, false);
            logInteraction.append(elem2.toString() + "\n");
            elem.add(elem2);
            DefaultMutableTreeNode elem3 = new DefaultMutableTreeNode("Metrics: " + solutionSet.get(i).getOPLAProblem().getSelectedMetrics().toString(), false);
            logInteraction.append(elem3.toString() + "\n");
            elem.add(elem3);
            DefaultMutableTreeNode elem4 = new DefaultMutableTreeNode("Info: " + solutionSet.get(i).getOPLAProblem().getArchitecture_().toString(), false);
            logInteraction.append("Info: " + solutionSet.get(i).getAlternativeArchitecture().toDetailedString(true) + "\n");
            elem.add(elem4);
            DefaultMutableTreeNode elem5 = new DefaultMutableTreeNode("Previous User Evaluation: " + solutionSet.get(i).getEvaluation(), false);
            logInteraction.append(elem5.toString() + "\n");
            elem.add(elem5);
            double clusterId = clustering.getAllSolutions().get(i).getClusterId();
            List bestClusters = new ArrayList();
            int finalI = i;
            if (clustering.getBestPerformingCluster().stream().filter(e -> e.getSolutionName() == solutionSet.get(finalI).getSolutionName()).count() > 0) {
                bestClusters.add("Trade-off");
            }
            for (int j = 0; j < solutionSet.get(i).numberOfObjectives(); j++) {
                if (clustering.getMinClusterByObjective(j) == clusterId) {
                    bestClusters.add(solutionSet.get(i).getOPLAProblem().getSelectedMetrics().get(j));
                }
            }
            if (bestClusters.size() <= 0) bestClusters.add("(-1) noise");
            DefaultMutableTreeNode elem6 = new DefaultMutableTreeNode("Best Clusters: " + bestClusters.toString(), false);
            logInteraction.append(elem6 + "\n");
            elem.add(elem6);

            objectNodes.get(solutionSet.get(i).getClusterId().intValue()).add(elem);
//            root.add(elem);
        }
    }


    private JPanel bodyPanelFn(int indexSolution) {

        jPanelSubjectiveAnalysis.removeAll();

        JTextArea notas = new JTextArea();
        notas.setText(" 5 - Excellent \n 4 - Good \n 3 - Regular \n 2 - Bad \n 1 - Very bad ");
        notas.setEditable(false);
        jPanelSubjectiveAnalysis.add(notas);
        JLabel label = new JLabel("Your Evaluation");
        jPanelSubjectiveAnalysis.add(label);
        JTextField jTextField = new JTextField();
        if (solutionSet.get(indexSolution).getEvaluation() > 0)
            jTextField.setText(String.valueOf(solutionSet.get(indexSolution).getEvaluation()));
        jTextField.setColumns(5);
        jPanelSubjectiveAnalysis.add(jTextField);

        JTextField teste = new JTextField();
        teste.setColumns(10);
        //  jPanelSubjectiveAnalysis.add(teste);


        JButton apply = new JButton("Apply");
        jPanelSubjectiveAnalysis.add(apply);
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int value = Integer.parseInt(jTextField.getText());
                solutionSet.get(indexSolution).setEvaluation(value);
                solutionSet.get(indexSolution).setEvaluatedByUser(true);

                int testeValor = solutionSet.get(indexSolution).getEvaluation();
                teste.setText(Integer.toString(testeValor));

                applySavedFile(testeValor);

                apply.setText("Saved");
                setTitle("Architectures");
            }
        });

        jPanelSubjectiveAnalysis.repaint();
        jPanelSubjectiveAnalysis.setVisible(true);
        panelMaster.add(jPanelSubjectiveAnalysis);
        panelMaster.repaint();

        getContentPane().repaint();
        pack();

        return jPanelSubjectiveAnalysis;
    }

    private void applySavedFile(int teste_valor) {
        File f1Di = new File(fileOnAnalyses);
        String replaceFileOnAnalyses = fileOnAnalyses.replace("_TEMP", "_Score_" + teste_valor + "_PlaId")
                .replaceFirst(solutionOnAnalyses.getOPLAProblem().getArchitecture_().getName(), "_Fitness_" + solutionOnAnalyses.toStringObjectives());
        if (f1Di.exists()) {
            new File(replaceFileOnAnalyses).delete();
            new File(replaceFileOnAnalyses.replace(".di", ".uml")).delete();
            new File(replaceFileOnAnalyses.replace(".di", ".notation")).delete();

            File f2Di = new File(replaceFileOnAnalyses);
            f1Di.renameTo(f2Di);

            File f1Uml = new File(fileOnAnalyses.replace(".di", ".uml"));
            File f2Uml = new File(replaceFileOnAnalyses.replace(".di", ".uml"));
            f1Uml.renameTo(f2Uml);

            File f1Not = new File(fileOnAnalyses.replace(".di", ".notation"));
            File f2Not = new File(replaceFileOnAnalyses.replace(".di", ".notation"));
            f1Not.renameTo(f2Not);
        } else {
            new Thread(() -> solutionSet.saveVariableToFile(solutionOnAnalyses, plaNameOnAnalyses
                    .replace("_TEMP", "_Score_" + teste_valor + "_PlaId").replaceFirst(solutionOnAnalyses.getOPLAProblem()
                            .getArchitecture_().getName(), "_Fitness_" + solutionOnAnalyses.toStringObjectives()), LOGGER, true)).start();
        }


        NSGAIIConfig configs = new NSGAIIConfig();
        configs.setClusteringAlgorithm(ClusteringAlgorithm.KMEANS);
        configs.setOplaConfigs(new OPLAConfigs(solutionSet.get(0).getOPLAProblem().getSelectedMetrics()));

        try {
            OPLA opla = new OPLA(replaceFileOnAnalyses.replace(".di", ".uml"), configs);
            Architecture architecture_ = opla.getArchitecture_();
            solutionOnAnalyses.setDecisionVariables(new Architecture[]{architecture_});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getObjectivesFormattedStr(int indexSolution) {
        if (solutionSet == null) return "";
        StringBuilder objectives = new StringBuilder("Objectives: ");
        double[] split = solutionSet.get(indexSolution).getObjectives();
        for (int i1 = 0; i1 < split.length; i1++) {
            objectives.append(getSelectedMetrics(indexSolution).get(i1)).append(" = ").append(split[i1]).append(", ");
        }

        return objectives.toString().substring(0, objectives.length() - 1);
    }

    private List<String> getSelectedMetrics(int indexSolution) {
        return solutionSet.get(indexSolution).getOPLAProblem().getSelectedMetrics();
    }

    class PopUp extends JPopupMenu {
        JMenuItem details;
        JMenuItem open;
        JMenuItem subjectiveAnalyse;

        public PopUp(JTree tree) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();
            if (node == null) return;
            Object nodeInfo = node.getUserObject();
            details = new JMenuItem("Details");
            details.addActionListener(e -> {
                JTextArea jta = new JTextArea(nodeInfo.toString());
                JScrollPane jsp = new JScrollPane(jta) {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(700, 320);
                    }
                };
                JOptionPane.showMessageDialog(null, jsp, nodeInfo.toString().split(":")[0], JOptionPane.INFORMATION_MESSAGE);
            });
            add(details);

            if (nodeInfo.toString().contains("TEMP_")) {
                open = new JMenuItem("Only view");
                open.addActionListener(e -> {
                    openOnPapyrus(node, nodeInfo);
                });
                add(open);

                subjectiveAnalyse = new JMenuItem("Subjective Analyse");
                subjectiveAnalyse.addActionListener(e -> {
                    LOGGER.info("Subjective Analyse " + nodeInfo.toString());
                    new Thread(() -> {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        openOnPapyrus(node, nodeInfo);
                        subjectiveAnalyseFn(Integer.parseInt(((DefaultMutableTreeNode) node.getFirstChild()).getUserObject().toString().split(":")[1].trim()));
                    }).start();
                    enableProgressOpen();
                });
                add(subjectiveAnalyse);
            }
        }

        private void openOnPapyrus(DefaultMutableTreeNode node, Object nodeInfo) {
            Integer id = Integer.valueOf(node.getFirstChild().toString().replace("Id: ", ""));
            progressBar.setValue(20);
            plaNameOnAnalyses = "Interaction_" + InteractiveSolutions.currentExecution + "_" + nodeInfo.toString();
            solutionOnAnalyses = solutionSet.get(id);
            progressBar.setValue(30);
            solutionSet.saveVariableToFile(solutionOnAnalyses, plaNameOnAnalyses, LOGGER, true);
            progressBar.setValue(60);
            LOGGER.info("Opened solution " + nodeInfo.toString());
            fileOnAnalyses = config.getApplicationYaml().getDirectoryToExportModels() + System.getProperty("file.separator") + plaNameOnAnalyses.concat(solutionSet.get(0).getOPLAProblem().getArchitecture_().getName() + ".di");
            progressBar.setValue(80);
            Process process = Utils.executePapyrus(config.getApplicationYaml().getPathPapyrus(), fileOnAnalyses);
            new Thread(() -> {
                try {
                    progressBar.setValue(90);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                disableProgressOpen();
            }).start();
//                if (process != null) {
//                    process.waitFor();
//                }
        }

        private void subjectiveAnalyseFn(int indexSolution) {
            setTitle("Architectures - Subjective Analyze of Solution " + indexSolution);
            bodyPanelFn(indexSolution);

        }
    }

    private void enableProgressOpen() {
        dialog = new JDialog(this, "Generating and opening File...");
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setLocationRelativeTo(this);
        dialog.setAlwaysOnTop(true);
        dialog.add(progressBar);
        dialog.setSize(300, 70);
        dialog.setVisible(true);
    }

    private void disableProgressOpen() {
        dialog.setVisible(false);
        dialog.dispose();
    }

}