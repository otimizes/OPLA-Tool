package br.ufpr.dinf.gres.opla.view;

import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.opla.view.util.Utils;
import jmetal4.core.SolutionSet;
import learning.Clustering;
import learning.ClusteringAlgorithm;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class InteractiveSolutions extends JDialog {

    private static final Logger LOGGER = Logger.getLogger(InteractiveSolutions.class);
    private ManagerApplicationConfig config;
    SolutionSet solutionSet;
    GridLayout gridLayout = new GridLayout();
    JPanel panelMaster = new JPanel(gridLayout);
    LayoutManager layoutPanelSubjectiveAnalyse = new MigLayout();
    JPanel jPanelSubjectiveAnalysis = new JPanel(layoutPanelSubjectiveAnalyse);
    final JTextField field = new JTextField();
    ClusteringAlgorithm clusteringAlgorithm;

    public InteractiveSolutions(ManagerApplicationConfig config, ClusteringAlgorithm clusteringAlgorithm, SolutionSet solutionSet) {
//        solutionSet.saveVariablesToFile("TEMP_", LOGGER,true);
        this.config = config;
        this.solutionSet = solutionSet;
        this.clusteringAlgorithm = clusteringAlgorithm;
        setTitle("Architectures");
        setModal(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setPreferredSize(new Dimension(screenSize.width - 300, screenSize.height - 300));
        setLocationByPlatform(true);

        try {
            paintTreeNode(solutionSet);
            getContentPane().revalidate();
            getContentPane().repaint();

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

    private void paintTreeNode(SolutionSet solutionSet) throws Exception {
        if (solutionSet.size() == 0) throw new RuntimeException("At least 1 solution is required.");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Solutions");
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
        Clustering clustering = new Clustering(solutionSet, this.clusteringAlgorithm);
        clustering.run();

        for (int i = 0; i < solutionSet.size(); i++) {
            String plaName = "TEMP_" + i + solutionSet.get(i).getOPLAProblem().getArchitecture_().getName();
            DefaultMutableTreeNode elem = new DefaultMutableTreeNode(plaName, true);
//            DefaultMutableTreeNode elem0 = new DefaultMutableTreeNode(i, true);
//            elem.add(elem0);

            DefaultMutableTreeNode elem1 = new DefaultMutableTreeNode("Id: " + i, false);
            elem.add(elem1);

            String objectives = getObjectivesFormattedStr(i);

            DefaultMutableTreeNode elem2 = new DefaultMutableTreeNode(objectives, false);
            elem.add(elem2);
            DefaultMutableTreeNode elem3 = new DefaultMutableTreeNode("Metrics: " + solutionSet.get(i).getOPLAProblem().getSelectedMetrics().toString(), false);
            elem.add(elem3);
            DefaultMutableTreeNode elem4 = new DefaultMutableTreeNode("Info: " + solutionSet.get(i).getOPLAProblem().getArchitecture_().toString(), false);
            elem.add(elem4);
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
            DefaultMutableTreeNode elem5 = new DefaultMutableTreeNode("Best Clusters: " + bestClusters.toString(), false);
            elem.add(elem5);
            root.add(elem);
        }
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

                int teste_valor = solutionSet.get(indexSolution).getEvaluation();
                teste.setText(Integer.toString(teste_valor));

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
                open = new JMenuItem("Open");
                open.addActionListener(e -> {
                    LOGGER.info("Opened solution " + nodeInfo.toString());
                    Utils.executePapyrus(config.getApplicationYaml().getPathPapyrus(), config.getApplicationYaml().getDirectoryToExportModels() + System.getProperty("file.separator") + nodeInfo.toString().concat(".di"));
                });
                add(open);

                subjectiveAnalyse = new JMenuItem("Subjective Analyse");
                subjectiveAnalyse.addActionListener(e -> {
                    LOGGER.info("Subjective Analyse " + nodeInfo.toString());
                    System.out.println(node.getDepth());
                    subjectiveAnalyseFn(Integer.parseInt(((DefaultMutableTreeNode) node.getFirstChild()).getUserObject().toString().split(":")[1].trim()));
                });
                add(subjectiveAnalyse);
            }
        }

        private void subjectiveAnalyseFn(int indexSolution) {
            setTitle("Architectures - Subjective Analyze of Solution " + indexSolution);
            bodyPanelFn(indexSolution);

        }
    }

}