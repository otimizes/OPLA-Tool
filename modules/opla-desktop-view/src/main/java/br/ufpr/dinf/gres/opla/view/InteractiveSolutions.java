package br.ufpr.dinf.gres.opla.view;

import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.opla.view.util.Constants;
import br.ufpr.dinf.gres.opla.view.util.UserHome;
import br.ufpr.dinf.gres.opla.view.util.Utils;
import jmetal4.core.SolutionSet;
import jmetal4.encodings.variable.Int;
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
    GridLayout gridLayout = new GridLayout();
    JPanel panelMaster = new JPanel(gridLayout);
    LayoutManager layoutPanelSubjectiveAnalyse = new MigLayout();
    JPanel jPanelSubjectiveAnalysis = new JPanel(layoutPanelSubjectiveAnalyse);

    public InteractiveSolutions(ManagerApplicationConfig config, SolutionSet solutionSet) {
//        solutionSet.saveVariablesToFile("TEMP_", LOGGER,true);
        this.config = config;
        this.solutionSet = solutionSet;
        setTitle("Architectures");
        setModal(true);
        setPreferredSize(new Dimension(1000, 600));
        setLocationByPlatform(true);

        paintTreeNode(solutionSet);

        getContentPane().revalidate();
        getContentPane().repaint();

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                solutionSet.get(0).getOPLAProblem().getArchitecture_().deleteTempFolder();
                e.getWindow().dispose();
            }
        });
        //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        setVisible(true);
    }

    private void paintTreeNode(SolutionSet solutionSet) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Solutions");
        gridLayout.setRows(2);
        JTree tree = new JTree(root);
        tree.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JScrollPane scroll = new JScrollPane(tree);
        panelMaster.add(scroll);
        setContentPane(panelMaster);
        for (int i = 0; i < solutionSet.size(); i++) {
            String plaName = "TEMP_" + i + solutionSet.get(i).getOPLAProblem().getArchitecture_().getName();
            DefaultMutableTreeNode elem = new DefaultMutableTreeNode(plaName, true);
            DefaultMutableTreeNode elem0 = new DefaultMutableTreeNode(i, true);
            elem.add(elem0);

            DefaultMutableTreeNode elem1 = new DefaultMutableTreeNode("Id: " + i, true);
            elem.add(elem1);

            String objectives = getObjectivesFormattedStr(i);

            DefaultMutableTreeNode elem2 = new DefaultMutableTreeNode(objectives, true);
            elem.add(elem2);
            DefaultMutableTreeNode elem3 = new DefaultMutableTreeNode("Metrics: " + solutionSet.get(i).getOPLAProblem().getSelectedMetrics().toString(), true);
            elem.add(elem3);
            DefaultMutableTreeNode elem4 = new DefaultMutableTreeNode("Info: " + solutionSet.get(i).getOPLAProblem().getArchitecture_().toString(), true);
            elem.add(elem4);
            root.add(elem);

            // save solution in smty format
            solutionSet.get(i).getOPLAProblem().getArchitecture_().save2(solutionSet.get(i).getOPLAProblem().getArchitecture_(), "TEMP/"+ plaName);
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
        notas.setText("5 - Excellent\n 4 - Good\n 3 - Regular\n 2 - Bad\n 1 - Very bad");
        notas.setEditable(false);
        jPanelSubjectiveAnalysis.add(notas);
        JLabel label = new JLabel("Your Evaluation");
        jPanelSubjectiveAnalysis.add(label);
        JTextField jTextField = new JTextField();
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
                    if(config.getApplicationYaml().getPathPapyrus().contains("SMartyModeling")){
                        try {
                            Process proc = Runtime.getRuntime().exec("java -jar "+config.getApplicationYaml().getPathPapyrus() +" "+config.getApplicationYaml().getDirectoryToExportModels() + System.getProperty("file.separator") +"TEMP" + System.getProperty("file.separator") + nodeInfo.toString().concat(".smty"));
                            System.out.println(config.getApplicationYaml().getDirectoryToExportModels() + System.getProperty("file.separator") + nodeInfo.toString().concat(".smty"));
                            return;
                        }
                        catch (Exception ex){
                            System.out.println(ex.fillInStackTrace());
                        }
                    }else {
                        Utils.executePapyrus(config.getApplicationYaml().getPathPapyrus(), config.getApplicationYaml().getDirectoryToExportModels() + System.getProperty("file.separator") + nodeInfo.toString().concat(".di"));
                    }
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