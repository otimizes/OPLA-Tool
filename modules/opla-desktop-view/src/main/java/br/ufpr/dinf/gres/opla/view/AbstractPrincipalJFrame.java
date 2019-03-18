package br.ufpr.dinf.gres.opla.view;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.opla.view.util.Constants;
import br.ufpr.dinf.gres.opla.view.util.UserHome;

/**
 * @author Fernando
 */
public abstract class AbstractPrincipalJFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    protected ManagerApplicationConfig config;
    private org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Principal.class);

    protected void checkAll(JPanel panel, Boolean isChecked) {
        Arrays.asList(panel.getComponents()).stream()
                .filter(component -> component instanceof JCheckBox)
                .map(JCheckBox.class::cast)
                .forEach(check -> check.setSelected(isChecked));
        LOGGER.debug("checked all JCheckBox " + panel.getName());
    }

    protected void enableAllChecks(JPanel panel, Boolean isDisabled) {
        Arrays.stream(panel.getComponents()).forEach(component -> {
            component.setEnabled(isDisabled);
            if (component instanceof JCheckBox) {
                ((JCheckBox) component).setSelected(isDisabled);
            }
        });

        LOGGER.debug("JCheckBox in " + panel.getName() + " enable = " + isDisabled);
    }

    protected void enableAllRadioButons(JPanel panel, Boolean isDisabled) {
        Arrays.asList(panel.getComponents()).stream()
                .filter(component -> component instanceof JRadioButton)
                .map(JRadioButton.class::cast)
                .forEach(radioButon -> radioButon.setEnabled(isDisabled));

        LOGGER.debug("JRadioButton in " + panel.getName() + " enable = " + isDisabled);
    }

    protected void enableAllSliders(JPanel panel, Boolean isDisabled) {
        Arrays.asList(panel.getComponents()).stream()
                .filter(component -> component instanceof JSlider)
                .map(JSlider.class::cast)
                .forEach(jSlider -> jSlider.setEnabled(isDisabled));

        LOGGER.info("JSlider in " + panel.getName() + " enable = " + isDisabled);
    }

    protected void cleanAllSelectedComboBox(JPanel panel) {
        Arrays.asList(panel.getComponents()).stream()
                .filter(component -> component instanceof JComboBox<?>)
                .map(JComboBox.class::cast)
                .forEach(jComboBox -> jComboBox.setSelectedItem(null));

        LOGGER.debug("JComboBox in " + panel.getName() + " is cleaned");
    }

    protected void configureProfile(JTextField jTexField, Path path, String profileName) throws IOException, URISyntaxException {
        Path target = Paths.get(UserHome.getOplaUserHome() + Constants.PROFILES_DIR + Constants.FILE_SEPARATOR + profileName);
        if (!Files.exists(target)) {
            URI uri = ClassLoader.getSystemResource(Constants.PROFILES_DIR).toURI();
            Path pathProfile = Paths.get(uri.getSchemeSpecificPart()).resolve(profileName);
            arquitetura.io.FileUtils.copy(pathProfile, target);
            jTexField.setText(target.toString());
            LOGGER.info("new profile = " + profileName + " has configured");
        } else {
            LOGGER.info(profileName + " is configured");
            jTexField.setText(path.toString());
        }
    }

    protected Boolean isDoubleClick(java.awt.event.MouseEvent evt) {
        return evt.getClickCount() == 2;
    }

}
