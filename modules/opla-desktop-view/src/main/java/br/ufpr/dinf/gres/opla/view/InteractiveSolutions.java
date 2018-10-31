package br.ufpr.dinf.gres.opla.view;

import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import jmetal4.core.SolutionSet;
import results.Execution;
import results.FunResults;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;

public class InteractiveSolutions extends JDialog {
    public InteractiveSolutions(ManagerApplicationConfig config, SolutionSet solutionSet, Execution execution) {
        setModal(true);
        setPreferredSize(new Dimension(600, 600));
        setLocationByPlatform(true);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Soluções");
        JTree tree = new JTree(root);
        tree.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        setContentPane(tree);
        for (int i = 0; i < execution.getFuns().size(); i++) {
            FunResults funResults = execution.getFuns().get(i);
//            JPanel panel = new JPanel();
//            GridLayout gridLayout = new GridLayout();
//            gridLayout.setColumns(1);
//            gridLayout.setRows(4);
//            panel.setLayout(gridLayout);
//            panel.add(new JLabel());
//            panel.add(new JLabel());
//            panel.add(new JLabel());
//            panel.add(new JLabel();

            DefaultMutableTreeNode elem = new DefaultMutableTreeNode(funResults.getSolution_name(), true);

            DefaultMutableTreeNode elem1 = new DefaultMutableTreeNode("Id: " + funResults.getId(), true);
            elem.add(elem1);
            DefaultMutableTreeNode elem2 = new DefaultMutableTreeNode("Objectives: " + (!funResults.getObjectives().equals("") ? funResults.getObjectives() : "None"), true);
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
                    PopUpDemo menu = new PopUpDemo(tree);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        getContentPane().revalidate();
        getContentPane().repaint();

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        setVisible(true);
    }

    class PopUpDemo extends JPopupMenu {
        JMenuItem details;
        JMenuItem open;
        public PopUpDemo(JTree tree){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    tree.getLastSelectedPathComponent();
            if (node == null) return;
            Object nodeInfo = node.getUserObject();
            details = new JMenuItem("Details");
            details.addActionListener(e -> {
                JTextArea jta = new JTextArea(nodeInfo.toString());
                JScrollPane jsp = new JScrollPane(jta){
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(480, 320);
                    }
                };
                JOptionPane.showMessageDialog(null, jsp, nodeInfo.toString().split(":")[0], JOptionPane.INFORMATION_MESSAGE);
            });
            add(details);

            System.out.println(nodeInfo.toString());
            if (nodeInfo.toString().contains("VAR_")) {
                open = new JMenuItem("Open");
                open.addActionListener(e -> {
                    System.out.println("open");
                });
                add(open);
            }
        }
    }

}
