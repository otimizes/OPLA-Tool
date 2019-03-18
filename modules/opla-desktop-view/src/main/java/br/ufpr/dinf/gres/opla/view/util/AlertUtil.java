package br.ufpr.dinf.gres.opla.view.util;

import javax.swing.*;

/**
 * @author Fernando
 */
public class AlertUtil {

    public static final String DEFAULT_ALERT_ERROR = "Problem occurred, view log file for details";

    /**
     * Exibe mensagem de aviso
     *
     * @param message
     */
    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
