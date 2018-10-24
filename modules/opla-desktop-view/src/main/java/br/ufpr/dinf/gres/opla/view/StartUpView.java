package br.ufpr.dinf.gres.opla.view;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.opla.config.ApplicationFile;
import br.ufpr.dinf.gres.opla.view.util.AlertUtil;
import br.ufpr.dinf.gres.opla.view.util.UserHome;
import br.ufpr.dinf.gres.opla.view.util.Utils;

/**
 * @author Fernando
 */
public class StartUpView extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(StartUpView.class);

    public StartUpView() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            StartUpView view = new StartUpView();
            view.createPathOplaTool();
            view.configureApplicationFile();
            view.setPathDatabase();
            view.configureDb();
            view.carregarPrincipal();
            view.setVisible(false);
        } catch (Exception ex) {
            LOGGER.error(ex);
            AlertUtil.showMessage(AlertUtil.DEFAULT_ALERT_ERROR);
            System.exit(0);
        }
    }

    // @formatter:off
    // <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		loadProgressBar = new javax.swing.JProgressBar();
		jLabel1 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		loadProgressBar.setToolTipText("Carregando Configurações");
		loadProgressBar.setIndeterminate(true);
		loadProgressBar.setName(""); // NOI18N
		loadProgressBar.setString("Carregando Configurações");
		loadProgressBar.setStringPainted(true);

		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jLabel1.setText("OPLA-Tool  - 1.0.0");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(loadProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createSequentialGroup().addGap(149, 149, 149).addComponent(jLabel1)
						.addContainerGap(161, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(loadProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31,
								javax.swing.GroupLayout.PREFERRED_SIZE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JProgressBar loadProgressBar;
	// End of variables declaration//GEN-END:variables

    // @formatter:on
    private void configureApplicationFile() {
        ApplicationFile.getInstance();
    }

    /**
     * Cria diretório raiz da ferramentas
     */
    private void createPathOplaTool() {
        UserHome.createDefaultOplaPathIfDontExists();
    }

    private void setPathDatabase() {
        database.Database.setPathToDB(UserHome.getPathToDb());
    }

    /**
     * Somente faz uma copia do banco de dados vazio para a pasta da oplatool no
     * diretorio do usuario se o mesmo nao existir.
     *
     * @throws Exception
     */
    private void configureDb() throws Exception {
        Utils.createDataBaseIfNotExists();
    }

    private void carregarPrincipal() throws Exception {
        Principal principal = new Principal();
        principal.configureView();
        principal.setVisible(true);
    }
}
