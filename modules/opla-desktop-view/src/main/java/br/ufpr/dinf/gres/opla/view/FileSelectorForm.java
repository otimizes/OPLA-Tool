package br.ufpr.dinf.gres.opla.view;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Fernando-Godoy
 */
public class FileSelectorForm extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;
    private JFileChooser seletor = null;
    private int saveDialog;

    /**
     * Creates new form FileSelector
     */
    public FileSelectorForm() {
        initComponents();
        this.setModal(true);
        this.setTitle("Seletor de Arquivos");
        this.setLocationRelativeTo(null);
    }

    // @formatter:off
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    // @formatter:on
    public String openSeletor(JTextField textField, int mode) {
        seletor = new JFileChooser();
        seletor.setFileSelectionMode(mode);
        saveDialog = seletor.showSaveDialog(null);
        if (saveDialog == 1) {
            textField.setText(StringUtils.EMPTY);
            this.dispose();
        } else {
            File arquivo = seletor.getSelectedFile();
            textField.setText(arquivo.getPath());
            this.dispose();
            return arquivo.getPath();
        }
        return StringUtils.EMPTY;
    }
}
