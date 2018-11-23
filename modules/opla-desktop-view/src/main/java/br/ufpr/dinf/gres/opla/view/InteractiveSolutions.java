package br.ufpr.dinf.gres.opla.view;

import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.opla.view.util.Utils;
import jmetal4.core.SolutionSet;
import jmetal4.problems.OPLA;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import results.Execution;
import results.FunResults;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InteractiveSolutions extends JDialog {

    private static final Logger LOGGER = Logger.getLogger(InteractiveSolutions.class);
    private ManagerApplicationConfig config;
    SolutionSet solutionSet;
    Execution execution;
    GridLayout gridLayout = new GridLayout();
    JPanel panelMaster = new JPanel(gridLayout);
    LayoutManager layoutPanelSubjectiveAnalyse = new MigLayout();
    JPanel jPanelSubjectiveAnalysis = new JPanel(layoutPanelSubjectiveAnalyse);

    public InteractiveSolutions(ManagerApplicationConfig config, SolutionSet solutionSet, Execution execution) {
        this.config = config;
        this.solutionSet = solutionSet;
        this.execution = execution;
        setTitle("Architectures");
        setModal(true);
        setPreferredSize(new Dimension(1000, 600));
        setLocationByPlatform(true);

        paintTreeNode(solutionSet, execution);

        getContentPane().revalidate();
        getContentPane().repaint();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        setVisible(true);
    }

    private void paintTreeNode(SolutionSet solutionSet, Execution execution) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Solutions");
        gridLayout.setRows(2);
        JTree tree = new JTree(root);
        tree.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelMaster.add(tree);
        setContentPane(panelMaster);
        for (int i = 0; i < execution.getFuns().size(); i++) {
            FunResults funResults = execution.getFuns().get(i);
            DefaultMutableTreeNode elem = new DefaultMutableTreeNode(funResults.getSolution_name(), true);
            DefaultMutableTreeNode elem0 = new DefaultMutableTreeNode(i, true);
            elem.add(elem0);

            DefaultMutableTreeNode elem1 = new DefaultMutableTreeNode("Id: " + funResults.getId(), true);
            elem.add(elem1);

            String objectives = getObjectivesFormattedStr(i);

            DefaultMutableTreeNode elem2 = new DefaultMutableTreeNode(objectives, true);
            elem.add(elem2);
            DefaultMutableTreeNode elem3 = new DefaultMutableTreeNode("Metrics: " + funResults.getExecution().getAllMetrics(), true);
            elem.add(elem3);
            DefaultMutableTreeNode elem4 = new DefaultMutableTreeNode("Info: " + funResults.getExecution().getInfos().get(i), true);
            elem.add(elem4);
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
        for (String metric : getSelectedMetrics(indexSolution)) {
            JLabel label = new JLabel(metric + ": ");
            jPanelSubjectiveAnalysis.add(label);
            JTextField jTextField = new JTextField();
            jTextField.setColumns(10);
            jPanelSubjectiveAnalysis.add(jTextField);
        }

        JButton apply = new JButton("Apply");
        jPanelSubjectiveAnalysis.add(apply);
        jPanelSubjectiveAnalysis.repaint();
        jPanelSubjectiveAnalysis.setVisible(true);
        panelMaster.add(jPanelSubjectiveAnalysis);
        panelMaster.repaint();

        getContentPane().repaint();
        pack();

        return jPanelSubjectiveAnalysis;
    }

    private String getObjectivesFormattedStr(int indexSolution) {
        FunResults funResults = execution.getFuns().get(indexSolution);
        if (solutionSet == null) return "";
        StringBuilder objectives = new StringBuilder("Objectives: ");
        String[] split = funResults.getObjectives().split("\\|");
        for (int i1 = 0; i1 < split.length; i1++) {
            objectives.append(getSelectedMetrics(indexSolution).get(i1)).append(" = ").append(split[i1]).append(", ");
        }

        return objectives.toString().substring(0, objectives.length() - 1);
    }

    private List<String> getSelectedMetrics(int indexSolution) {
        return ((OPLA) solutionSet.get(indexSolution).getType().problem_).getSelectedMetrics();
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

            if (nodeInfo.toString().contains("VAR_")) {
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
                    subjectiveAnalyseFn((Integer) ((DefaultMutableTreeNode) node.getFirstChild()).getUserObject());


                });
                add(subjectiveAnalyse);
            }
        }

        private void subjectiveAnalyseFn(int indexSolution) {
            bodyPanelFn(indexSolution);

        }
    }

}
