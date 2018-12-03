package br.ufpr.dinf.gres.opla.view;

import arquitetura.io.FileUtils;
import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.loglog.Logger;
import br.ufpr.dinf.gres.opla.config.ApplicationFile;
import br.ufpr.dinf.gres.opla.entity.Execution;
import br.ufpr.dinf.gres.opla.entity.Experiment;
import br.ufpr.dinf.gres.opla.entity.metric.GenericMetric;
import br.ufpr.dinf.gres.opla.view.analysis.BoxPlot;
import br.ufpr.dinf.gres.opla.view.analysis.BoxPlotItem;
import br.ufpr.dinf.gres.opla.view.enumerators.Metric;
import br.ufpr.dinf.gres.opla.view.log.LogListener;
import br.ufpr.dinf.gres.opla.view.model.combomodel.*;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.AbstractMetricTableModel;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.ExecutionTableModel;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.ExperimentTableModel;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.MapObjectiveNameTableModel;
import br.ufpr.dinf.gres.opla.view.util.*;
import br.ufpr.dinf.gres.persistence.dao.ExecutionDAO;
import br.ufpr.dinf.gres.persistence.dao.ExperimentDAO;
import br.ufpr.dinf.gres.persistence.dao.MapObjectivesDAO;
import br.ufpr.dinf.gres.persistence.dao.ObjectiveDAO;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import com.ufpr.br.opla.algorithms.NSGAII;
import com.ufpr.br.opla.algorithms.PAES;
import com.ufpr.br.opla.charts.ChartGenerate;
import com.ufpr.br.opla.charts.EdBar;
import com.ufpr.br.opla.charts.EdLine;
import com.ufpr.br.opla.configuration.GuiFile;
import com.ufpr.br.opla.configuration.VolatileConfs;
import com.ufpr.br.opla.gui.GuiServices;
import com.ufpr.br.opla.gui.HypervolumeWindow;
import com.ufpr.br.opla.gui.SmallerFintnessValuesWindow;
import com.ufpr.br.opla.gui.StartUp;
import com.ufpr.br.opla.indicators.HypervolumeData;
import com.ufpr.br.opla.indicators.HypervolumeGenerateObjsData;
import com.ufpr.br.opla.utils.GuiUtils;
import com.ufpr.br.opla.utils.MutationOperatorsSelected;
import com.ufpr.br.opla.utils.Time;
import com.ufpr.br.opla.utils.Validators;
import domain.AlgorithmExperiment;
import jmetal4.experiments.FeatureMutationOperators;
import jmetal4.experiments.Metrics;
import learning.Moment;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import utils.RScriptOption;
import utils.RScriptOptionElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fernando
 */
public class Principal extends AbstractPrincipalJFrame {

    private static final long serialVersionUID = 1L;

    private final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Principal.class);

    private static final LogLog VIEW_LOGGER = Logger.getLogger();

    private final ExperimentTableModel tmExperiments = new ExperimentTableModel();
    private final ExperimentTableModel tmExecExperiments = new ExperimentTableModel();
    private final ExecutionTableModel tmExecution = new ExecutionTableModel();
    private final MapObjectiveNameTableModel tmMapObjectiveSolution = new MapObjectiveNameTableModel();

    private final SolutionNameComboModel solutionNameComboModel = new SolutionNameComboModel(Collections.emptyList());
    private final ObjectiveNameComboModel objetiveNameComboModel = new ObjectiveNameComboModel(Collections.emptyList());

    private final ExperimentDAO experimentDAO;
    private final ExecutionDAO executionDAO;
    private final ObjectiveDAO objectiveDAO;
    private final MapObjectivesDAO mapObjectivesDAO;
    private String inputArchitecture = StringUtils.EMPTY;
    Locale locale = Locale.US;
    ResourceBundle rb = ResourceBundle.getBundle("i18n", locale);

    public Principal() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
        defineModels();
        this.experimentDAO = new ExperimentDAO();
        this.executionDAO = new ExecutionDAO();
        this.objectiveDAO = new ObjectiveDAO();
        this.mapObjectivesDAO = new MapObjectivesDAO();
        loadExecutionsData();
    }

    @SuppressWarnings("unchecked")
    private void defineModels() {
        this.cbAlgothm.setModel(new AlgorithmComboModel());
        this.cbClusteringAlgorithm.setModel(new ClusteringAlgorithmComboModel());
        this.cbRScript.setModel(new RScriptComboModel());
        this.cbClusteringMoment.setModel(new ClusteringMomentComboModel());
        this.cbClusteringMoment.setSelectedItem(Moment.NONE);
        this.tbExperiments.setModel(tmExperiments);
        this.tbExecutions.setModel(tmExecExperiments);
        this.tbRuns.setModel(tmExecution);
        this.tbObjectiveSolution.setModel(tmMapObjectiveSolution);
    }

    public void configureView() throws IOException, Exception {
        configPaths();
        configureLogArea();
        configurePainelProfiles();
        configurePanelMutation();
        configurePanelScopeSelection();
        configurePanelOperators();
        configureSmartyProfile();
        configureConcernsProfile();
        configurePatternsProfile();
        configureRelationshipsProfile();
        configureTemplates();
        configureLocaleToSaveModels();
        configureLocaleToInteractionPapyrus();
        configureLocaleToExportModels();
        copyBinHypervolume();
        configureLastInputArchitecture();
    }

    private void configureLastInputArchitecture() {
        this.tfInputArchitecturePath.setText(config.getApplicationYaml().getPathLastOptimizationInput());
    }

    private void configPaths() {
        try {
            Utils.createPathsOplaTool();
            config = ApplicationFile.getInstance();
            GuiUtils.fontSize(GuiFile.getInstance().getFontSize());
        } catch (Exception ex) {
            LOGGER.error(ex);
            AlertUtil.showMessage(AlertUtil.DEFAULT_ALERT_ERROR);
        }
    }

    private void configurePanelOperators() {
        enableAllSliders(panelOperatorOption, false);
    }

    private void configurePanelMutation() {
        checkAll(panelMutations, true);
        enableAllChecks(panelMutations, false);
    }

    private void configurePanelScopeSelection() {
        enableAllRadioButons(panelScopeSelection, false);
    }

    private void configureLogArea() {
        DefaultCaret cared = (DefaultCaret) taLogStatus.getCaret();
        cared.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        Logger.addListener(new LogListener(taLogStatus));
        VIEW_LOGGER.putLog("Inicializando OPLA-Tool");
    }

    private void configurePainelProfiles() {
        checkAll(panelCkProfiles, true);
    }

    private void configureSmartyProfile() throws IOException, URISyntaxException {
        try {
            configureProfile(tfSmartProfile, config.getConfig().getPathToProfileSmarty(), Constants.PROFILE_SMART_NAME);
            config.updatePathToProfileSmarty(tfSmartProfile.getText());
        } catch (IOException ex) {
            LOGGER.error("Smart Profile Config error: ", ex);
            throw ex;
        }
    }

    private void configureConcernsProfile() throws IOException, URISyntaxException {
        try {
            configureProfile(tfFeatureProfile, config.getConfig().getPathToProfileConcern(),
                    Constants.PROFILE_CONCERNS_NAME);
            config.updatePathToProfileConcerns(tfFeatureProfile.getText());
        } catch (IOException ex) {
            LOGGER.error("Feature Profile Config error: ", ex);
            throw ex;
        }
    }

    private void configurePatternsProfile() throws IOException, URISyntaxException {
        try {
            configureProfile(tfPatternProfile, config.getConfig().getPathToProfilePatterns(),
                    Constants.PROFILE_PATTERN_NAME);
            config.updatePathToProfilePatterns(tfPatternProfile.getText());
        } catch (IOException ex) {
            LOGGER.error("Pattern Profile Config error: ", ex);
            throw ex;
        }
    }

    private void configureRelationshipsProfile() throws IOException, URISyntaxException {
        try {
            configureProfile(tfRelationshipProfile, config.getConfig().getPathToProfileRelationships(),
                    Constants.PROFILE_RELATIONSHIP_NAME);
            config.updatePathToProfileRelationships(tfRelationshipProfile.getText());
        } catch (IOException ex) {
            LOGGER.error("Relationship Profile Config error: ", ex);
            throw ex;
        }
    }

    private void configureTemplates() throws IOException, URISyntaxException {
        long count = Files.list(Paths.get(UserHome.getPathToTemplates())).count();
        if (count > 0) {
            LOGGER.info("Templates Path is configured");
            tfTemplateDiretory.setText(config.getConfig().getPathToTemplateModelsDirectory().toString());
        } else {
            try {
                Principal.copyTemplates();

                tfTemplateDiretory.setText(UserHome.getPathToTemplates());
                config.updatePathToTemplateFiles(tfTemplateDiretory.getText());
            } catch (IOException ex) {
                LOGGER.error("Template directory Config error: ", ex);
                throw ex;
            }
        }
    }

    public static void copyTemplates() throws URISyntaxException {
        URI uriTemplatesDir = ClassLoader.getSystemResource(Constants.TEMPLATES_DIR).toURI();
        String simplesUmlPath = Constants.SIMPLES_UML_NAME;
        String simplesDiPath = Constants.SIMPLES_DI_NAME;
        String simplesNotationPath = Constants.SIMPLES_NOTATION_NAME;

        Path externalPathSimplesUml = Paths.get(UserHome.getPathToTemplates() + simplesUmlPath);
        Path externalPathSimplesDi = Paths.get(UserHome.getPathToTemplates() + simplesDiPath);
        Path externalPathSimplesNotation = Paths.get(UserHome.getPathToTemplates() + simplesNotationPath);

        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesUmlPath), externalPathSimplesUml);
        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesDiPath), externalPathSimplesDi);
        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesNotationPath), externalPathSimplesNotation);
    }

    private void configureLocaleToSaveModels() throws IOException {
        if (StringUtils.isNotBlank(config.getConfig().getDirectoryToSaveModels().toString())) {
            LOGGER.info("Manipulation Directory is configured");
            tfManipulationDirectory.setText(config.getConfig().getDirectoryToSaveModels().toString());
            config.updatePathToSaveModels(tfManipulationDirectory.getText());
        } else {
            try {
                String pathTempDir = config.updateDefaultPathToSaveModels();
                tfManipulationDirectory.setText(pathTempDir);
            } catch (IOException ex) {
                LOGGER.error("Manipulation directory Config error: ", ex);
                throw ex;
            }
        }
    }


    private void configureLocaleToInteractionPapyrus() throws IOException {
        ckEnableInteraction.setSelected(false);
        tfInteractionDirectory1.setEnabled(ckEnableInteraction.isSelected());
        btManipulationDirectory3.setEnabled(ckEnableInteraction.isSelected());
        ckEnableInteraction1.setEnabled(ckEnableInteraction.isSelected());
        cbClusteringAlgorithm.setEnabled(false);
        if (StringUtils.isNotBlank(config.getConfig().getPathPapyrus().toString())) {
            LOGGER.info("Papyrus Directory is configured");
            ckEnableInteraction1.setSelected(true);
            tfInteractionDirectory1.setText(config.getConfig().getPathPapyrus().toString());
            config.updatePathPapyurs(tfInteractionDirectory1.getText());
        } else {
            ckEnableInteraction1.setSelected(false);
        }
    }

    private void configureLocaleToExportModels() throws IOException {
        if (StringUtils.isNotBlank(config.getConfig().getDirectoryToExportModels().toString())) {
            LOGGER.info("Output Directory is configured");
            tfOutputDirectory.setText(config.getConfig().getDirectoryToExportModels().toString());
            config.updatePathToExportModels(tfOutputDirectory.getText());
        } else {
            try {
                String path = config.configureDefaultLocaleToExportModels();
                tfOutputDirectory.setText(path);
            } catch (IOException ex) {
                LOGGER.error("Output directory Config error: ", ex);
                throw ex;
            }
        }
    }


    /**
     * Copy hybervolume binary to oplatool bins directory if OS isn't Windows.
     *
     * @throws Exception
     */
    private void copyBinHypervolume() throws Exception {
        if (!OSUtils.isWindows()) {
            String target = UserHome.getOplaUserHome() + Constants.BINS_DIR;
            Path path = Paths.get(target + Constants.FILE_SEPARATOR + Constants.HYPERVOLUME_TAR_GZ);
            Utils.copy(Constants.HYPERVOLUME_TAR_GZ, path.toString());

            Utils.unTargz(path.toFile(), target);
        }
    }


    // @formatter:off
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        panelCkProfiles = new javax.swing.JPanel();
        ckSmarty = new javax.swing.JCheckBox();
        ckFeature = new javax.swing.JCheckBox();
        ckPatterns = new javax.swing.JCheckBox();
        ckRelationship = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfSmartProfile = new javax.swing.JTextField();
        btBrowserSmartProfile = new javax.swing.JButton();
        tfFeatureProfile = new javax.swing.JTextField();
        btBrowserFeatureProfile = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        tfPatternProfile = new javax.swing.JTextField();
        btBrowserPatternProfile = new javax.swing.JButton();
        tfRelationshipProfile = new javax.swing.JTextField();
        btBrowserRelationshipProfile = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        tfTemplateDiretory = new javax.swing.JTextField();
        btTemplateDirectory = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        tfManipulationDirectory = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btManipulationDirectory = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btViewApplicationConfig = new javax.swing.JButton();
        ckEnableInteraction = new javax.swing.JCheckBox();
        ckEnableInteraction1 = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        tfInteractionDirectory1 = new javax.swing.JTextField();
        btManipulationDirectory3 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        cbAlgothm = new javax.swing.JComboBox<>();
        tfNumberRuns = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfMaxEvaluations = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tfPopulationSize = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tfArchiveSize = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        tfArchiveSize1 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        ckConventional = new javax.swing.JCheckBox();
        ckComponentCoupling = new javax.swing.JCheckBox();
        ckClassCoupling = new javax.swing.JCheckBox();
        ckSize = new javax.swing.JCheckBox();
        ckFeatureDriven = new javax.swing.JCheckBox();
        ckFeatureInterlacing = new javax.swing.JCheckBox();
        ckFeatureDifusion = new javax.swing.JCheckBox();
        ckPLAExtensibility = new javax.swing.JCheckBox();
        ckCohesion = new javax.swing.JCheckBox();
        ckElegance = new javax.swing.JCheckBox();
        ckWocsClass = new javax.swing.JCheckBox();
        ckWocsInterface = new javax.swing.JCheckBox();
        ckCBCS = new javax.swing.JCheckBox();
        ckSVS = new javax.swing.JCheckBox();
        ckSSC = new javax.swing.JCheckBox();
        ckAV = new javax.swing.JCheckBox();
        ckLLC = new javax.swing.JCheckBox();
        panelOperators = new javax.swing.JPanel();
        panelOperatorOption = new javax.swing.JPanel();
        jsMutation = new javax.swing.JSlider();
        jsCrossover = new javax.swing.JSlider();
        ckMutation = new javax.swing.JCheckBox();
        ckCrossover = new javax.swing.JCheckBox();
        panelMutations = new javax.swing.JPanel();
        ckFeatureDrivenMutation = new javax.swing.JCheckBox();
        ckMoveMethodMutation = new javax.swing.JCheckBox();
        ckAddClassMutation = new javax.swing.JCheckBox();
        ckMoveOperationMutation = new javax.swing.JCheckBox();
        ckAddManagerClassMutation = new javax.swing.JCheckBox();
        ckMoveAttributeMutation = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        tfInputArchitecturePath = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btConfirme = new javax.swing.JButton();
        btClean = new javax.swing.JButton();
        btSelectPath = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        tfOutputDirectory = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btSelectOutputDirectory = new javax.swing.JButton();
        tfDescription = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        btRun = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        cbClusteringAlgorithm = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        cbClusteringMoment = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        ckMediator = new javax.swing.JCheckBox();
        ckStrategy = new javax.swing.JCheckBox();
        ckBridge = new javax.swing.JCheckBox();
        panelScopeSelection = new javax.swing.JPanel();
        rbRandom = new javax.swing.JRadioButton();
        rbElements = new javax.swing.JRadioButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbExecutions = new javax.swing.JTable();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbRuns = new javax.swing.JTable();
        panelResultSolution = new javax.swing.JPanel();
        cbSolutionName = new javax.swing.JComboBox<>();
        lbSolution = new javax.swing.JLabel();
        panelResultObjetive = new javax.swing.JPanel();
        cbObjectiveSoluction = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbObjectiveSolution = new javax.swing.JTable();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbMetrics = new javax.swing.JTable();
        btNonDomitedSolutions = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbExperiments = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        btSelectObjective = new javax.swing.JButton();
        btGenerateChart = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        btEuclidianDistance = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        btHypervolume = new javax.swing.JButton();
        ckUseNormalization = new javax.swing.JCheckBox();
        jPanel27 = new javax.swing.JPanel();
        btBoxPlot = new javax.swing.JButton();
        cbRScript = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taLogStatus = new javax.swing.JTextArea();
        jProgressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setMaximumSize(new java.awt.Dimension(2767, 2767));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Profiles Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        panelCkProfiles.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelCkProfiles.setName("Profile Smart Configuration"); // NOI18N

        ckSmarty.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ckSmarty.setText("SMarty");

        ckFeature.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ckFeature.setText("Feature");

        ckPatterns.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ckPatterns.setText("Patterns");

        ckRelationship.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ckRelationship.setText("Relationships");

        javax.swing.GroupLayout panelCkProfilesLayout = new javax.swing.GroupLayout(panelCkProfiles);
        panelCkProfiles.setLayout(panelCkProfilesLayout);
        panelCkProfilesLayout.setHorizontalGroup(
                panelCkProfilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelCkProfilesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ckSmarty)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ckFeature)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ckPatterns)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ckRelationship)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelCkProfilesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{ckFeature, ckPatterns, ckRelationship, ckSmarty});

        panelCkProfilesLayout.setVerticalGroup(
                panelCkProfilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelCkProfilesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelCkProfilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckSmarty)
                                        .addComponent(ckFeature)
                                        .addComponent(ckPatterns)
                                        .addComponent(ckRelationship))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setName("Panel Text Fields Profiles"); // NOI18N

        jLabel1.setText("Smarty Profile:");

        tfSmartProfile.setColumns(60);

        btBrowserSmartProfile.setText("Browser");
        btBrowserSmartProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowserSmartProfileActionPerformed(evt);
            }
        });

        tfFeatureProfile.setColumns(60);

        btBrowserFeatureProfile.setText("Browser");
        btBrowserFeatureProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowserFeatureProfileActionPerformed(evt);
            }
        });

        jLabel3.setText("Pattern Profile:");

        tfPatternProfile.setColumns(60);

        btBrowserPatternProfile.setText("Browser");
        btBrowserPatternProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowserPatternProfileActionPerformed(evt);
            }
        });

        tfRelationshipProfile.setColumns(60);

        btBrowserRelationshipProfile.setText("Browser");
        btBrowserRelationshipProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBrowserRelationshipProfileActionPerformed(evt);
            }
        });

        jLabel5.setText("Feature Profile:");

        jLabel17.setText("Relationship Profile:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel17)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(tfRelationshipProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(35, 35, 35)
                                                .addComponent(tfPatternProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(33, 33, 33)
                                                .addComponent(tfFeatureProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(37, 37, 37)
                                                .addComponent(tfSmartProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btBrowserSmartProfile)
                                        .addComponent(btBrowserFeatureProfile)
                                        .addComponent(btBrowserPatternProfile)
                                        .addComponent(btBrowserRelationshipProfile))
                                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(tfSmartProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btBrowserSmartProfile))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfFeatureProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btBrowserFeatureProfile)
                                        .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfPatternProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btBrowserPatternProfile)
                                        .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfRelationshipProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btBrowserRelationshipProfile)
                                        .addComponent(jLabel17))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelCkProfiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelCkProfiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Template Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        tfTemplateDiretory.setColumns(63);

        btTemplateDirectory.setText("Select a Directory");
        btTemplateDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTemplateDirectoryActionPerformed(evt);
            }
        });

        jLabel2.setText("Directory:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTemplateDiretory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btTemplateDirectory)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfTemplateDiretory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btTemplateDirectory)
                                        .addComponent(jLabel2))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Manipulation Directory", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        tfManipulationDirectory.setColumns(63);

        jLabel6.setText("Directory:");

        btManipulationDirectory.setText("Select a Directory");
        btManipulationDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btManipulationDirectoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfManipulationDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btManipulationDirectory)
                                .addContainerGap(548, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfManipulationDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addComponent(btManipulationDirectory))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "User Interaction", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        btViewApplicationConfig.setText("Visualize your application config file");
        btViewApplicationConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btViewApplicationConfigActionPerformed(evt);
            }
        });

        ckEnableInteraction.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ckEnableInteraction.setText("Enable");
        ckEnableInteraction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckEnableInteraction(evt);
            }
        });

        ckEnableInteraction1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ckEnableInteraction1.setText("Enable to open Papyrus");
        ckEnableInteraction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckEnableInteraction1(evt);
            }
        });

        jLabel19.setText("Papyrus:");

        tfInteractionDirectory1.setColumns(63);

        btManipulationDirectory3.setLabel("Select a File");
        btManipulationDirectory3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btManipulationDirectory3btInteractionDirectoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btViewApplicationConfig)
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addComponent(jLabel19)
                                                .addGap(6, 6, 6)
                                                .addComponent(tfInteractionDirectory1, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btManipulationDirectory3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                                .addComponent(ckEnableInteraction)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(ckEnableInteraction1)))
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckEnableInteraction)
                                        .addComponent(ckEnableInteraction1))
                                .addGap(17, 17, 17)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel19)
                                        .addComponent(tfInteractionDirectory1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btManipulationDirectory3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btViewApplicationConfig)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(177, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General Configuration", jPanel1);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 300));
        jPanel8.setName("Panel Settings"); // NOI18N
        jPanel8.setPreferredSize(new java.awt.Dimension(310, 200));

        tfNumberRuns.setColumns(10);

        jLabel7.setText("Number of Runs:");

        jLabel8.setText("Select Algorithm Whinch Want Use ");

        tfMaxEvaluations.setColumns(10);

        jLabel9.setText("Max Evaluations:");

        tfPopulationSize.setColumns(10);

        jLabel10.setText("Population Size:");

        tfArchiveSize.setColumns(10);

        jLabel11.setText("Archive Size:");

        jLabel15.setText("Max  Interactions:");

        tfArchiveSize1.setColumns(10);
        tfArchiveSize1.setText("5");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(tfArchiveSize1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel7)
                                                        .addComponent(jLabel9)
                                                        .addComponent(jLabel10)
                                                        .addComponent(jLabel11))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(tfArchiveSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(tfPopulationSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(tfMaxEvaluations, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(tfNumberRuns, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(cbAlgothm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbAlgothm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfNumberRuns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfMaxEvaluations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfPopulationSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfArchiveSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfArchiveSize1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel15))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Objective Functions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 300));
        jPanel9.setName("Panel Objective Functions"); // NOI18N
        jPanel9.setPreferredSize(new java.awt.Dimension(694, 200));

        ckConventional.setText("Conventional");
        ckConventional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckConventionalActionPerformed(evt);
            }
        });

        ckComponentCoupling.setText("Component Coupling");
        ckComponentCoupling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckComponentCouplingActionPerformed(evt);
            }
        });

        ckClassCoupling.setText("Class Coupling");
        ckClassCoupling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckClassCouplingActionPerformed(evt);
            }
        });

        ckSize.setText("Size");
        ckSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckSizeActionPerformed(evt);
            }
        });

        ckFeatureDriven.setText("Feature Driven");
        ckFeatureDriven.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckFeatureDrivenActionPerformed(evt);
            }
        });

        ckFeatureInterlacing.setText("Features Interlacing");
        ckFeatureInterlacing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckFeatureInterlacingActionPerformed(evt);
            }
        });

        ckFeatureDifusion.setText("Features Diffusion");
        ckFeatureDifusion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckFeatureDifusionActionPerformed(evt);
            }
        });

        ckPLAExtensibility.setText("PLA Extensibility");
        ckPLAExtensibility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckPLAExtensibilityActionPerformed(evt);
            }
        });

        ckCohesion.setText("Cohesion");
        ckCohesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckCohesionActionPerformed(evt);
            }
        });

        ckElegance.setText("Elegance");
        ckElegance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckEleganceActionPerformed(evt);
            }
        });

        ckWocsClass.setText("Wocsclass");
        ckWocsClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckWocsClassActionPerformed(evt);
            }
        });

        ckWocsInterface.setText("Wocsinterface");
        ckWocsInterface.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckWocsinterfaceActionPerformed(evt);
            }
        });

        ckCBCS.setText("CBCS");
        ckCBCS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckCBCSActionPerformed(evt);
            }
        });

        ckSVS.setText("SVS");
        ckSVS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckSVSActionPerformed(evt);
            }
        });

        ckSSC.setText("SSC");
        ckSSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckSSCActionPerformed(evt);
            }
        });

        ckAV.setText("AV");
        ckAV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckAVActionPerformed(evt);
            }
        });

        ckLLC.setText("LLC");
        ckLLC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckLLCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ckConventional)
                                        .addComponent(ckComponentCoupling)
                                        .addComponent(ckClassCoupling)
                                        .addComponent(ckSize)
                                        .addComponent(ckPLAExtensibility))
                                .addGap(100, 100, 100)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ckFeatureDriven)
                                        .addComponent(ckFeatureInterlacing)
                                        .addComponent(ckFeatureDifusion)
                                        .addComponent(ckCohesion)
                                        .addComponent(ckElegance)
                                        .addComponent(ckLLC))
                                .addGap(92, 92, 92)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ckAV)
                                        .addComponent(ckSSC)
                                        .addComponent(ckSVS)
                                        .addComponent(ckCBCS)
                                        .addComponent(ckWocsInterface)
                                        .addComponent(ckWocsClass))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckConventional)
                                        .addComponent(ckFeatureDriven)
                                        .addComponent(ckWocsClass))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckComponentCoupling)
                                        .addComponent(ckFeatureInterlacing)
                                        .addComponent(ckWocsInterface))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckClassCoupling)
                                        .addComponent(ckFeatureDifusion)
                                        .addComponent(ckCBCS))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckSize)
                                        .addComponent(ckCohesion)
                                        .addComponent(ckSVS))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckPLAExtensibility)
                                        .addComponent(ckElegance)
                                        .addComponent(ckSSC))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckAV)
                                        .addComponent(ckLLC))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelOperators.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Operators", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        panelOperatorOption.setName("Panel Operators Options"); // NOI18N

        jsMutation.setMajorTickSpacing(10);
        jsMutation.setMaximum(10);
        jsMutation.setMinorTickSpacing(1);
        jsMutation.setPaintLabels(true);
        jsMutation.setPaintTicks(true);
        jsMutation.setBorder(javax.swing.BorderFactory.createTitledBorder("Mutation Probability"));

        jsCrossover.setMajorTickSpacing(10);
        jsCrossover.setMaximum(10);
        jsCrossover.setMinorTickSpacing(1);
        jsCrossover.setPaintLabels(true);
        jsCrossover.setPaintTicks(true);
        jsCrossover.setSnapToTicks(true);
        jsCrossover.setBorder(javax.swing.BorderFactory.createTitledBorder("Crossover Probability"));

        ckMutation.setText("Mutation");
        ckMutation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckMutationActionPerformed(evt);
            }
        });

        ckCrossover.setText("Crossover");
        ckCrossover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckCrossoverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOperatorOptionLayout = new javax.swing.GroupLayout(panelOperatorOption);
        panelOperatorOption.setLayout(panelOperatorOptionLayout);
        panelOperatorOptionLayout.setHorizontalGroup(
                panelOperatorOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelOperatorOptionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelOperatorOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelOperatorOptionLayout.createSequentialGroup()
                                                .addComponent(ckMutation)
                                                .addGap(18, 18, 18)
                                                .addComponent(ckCrossover)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(panelOperatorOptionLayout.createSequentialGroup()
                                                .addComponent(jsMutation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jsCrossover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))
        );
        panelOperatorOptionLayout.setVerticalGroup(
                panelOperatorOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelOperatorOptionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelOperatorOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckMutation)
                                        .addComponent(ckCrossover))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelOperatorOptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jsCrossover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jsMutation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelOperatorsLayout = new javax.swing.GroupLayout(panelOperators);
        panelOperators.setLayout(panelOperatorsLayout);
        panelOperatorsLayout.setHorizontalGroup(
                panelOperatorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelOperatorsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelOperatorOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelOperatorsLayout.setVerticalGroup(
                panelOperatorsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelOperatorsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panelOperatorOption, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        panelMutations.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mutation Operators", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        panelMutations.setName("Panel Mutations Operators"); // NOI18N

        ckFeatureDrivenMutation.setText("Feature-driven Mutation");
        ckFeatureDrivenMutation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckFeatureDrivenMutationItemStateChanged(evt);
            }
        });

        ckMoveMethodMutation.setText("Move Method Mutation");
        ckMoveMethodMutation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckMoveMethodMutationItemStateChanged(evt);
            }
        });

        ckAddClassMutation.setText("Add Class Mutation");
        ckAddClassMutation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckAddClassMutationItemStateChanged(evt);
            }
        });

        ckMoveOperationMutation.setText("Move Operation Mutation");
        ckMoveOperationMutation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckMoveOperationMutationItemStateChanged(evt);
            }
        });

        ckAddManagerClassMutation.setText("Add Manager Class Mutation");
        ckAddManagerClassMutation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckAddManagerClassMutationItemStateChanged(evt);
            }
        });

        ckMoveAttributeMutation.setText("Move Attribute Mutation");
        ckMoveAttributeMutation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckMoveAttributeMutationItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelMutationsLayout = new javax.swing.GroupLayout(panelMutations);
        panelMutations.setLayout(panelMutationsLayout);
        panelMutationsLayout.setHorizontalGroup(
                panelMutationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMutationsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelMutationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ckFeatureDrivenMutation)
                                        .addComponent(ckMoveMethodMutation)
                                        .addComponent(ckAddClassMutation))
                                .addGap(64, 64, 64)
                                .addGroup(panelMutationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ckMoveAttributeMutation)
                                        .addComponent(ckAddManagerClassMutation)
                                        .addComponent(ckMoveOperationMutation))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMutationsLayout.setVerticalGroup(
                panelMutationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMutationsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelMutationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckFeatureDrivenMutation)
                                        .addComponent(ckMoveOperationMutation))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelMutationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckMoveMethodMutation)
                                        .addComponent(ckAddManagerClassMutation))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelMutationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckAddClassMutation)
                                        .addComponent(ckMoveAttributeMutation))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Input Architecture", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        tfInputArchitecturePath.setColumns(50);

        jLabel12.setText("Path:");

        btConfirme.setText("Confirme");
        btConfirme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConfirmeActionPerformed(evt);
            }
        });

        btClean.setText("Clean");
        btClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCleanActionPerformed(evt);
            }
        });

        btSelectPath.setText("Select  a Path");
        btSelectPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectPathActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfInputArchitecturePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btConfirme, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btClean, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btSelectPath, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        jPanel12Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{btClean, btConfirme, btSelectPath});

        jPanel12Layout.setVerticalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfInputArchitecturePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btConfirme)
                                        .addComponent(btClean)
                                        .addComponent(btSelectPath))
                                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel12Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[]{btClean, btConfirme, btSelectPath});

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Output Directory", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        tfOutputDirectory.setColumns(50);

        jLabel13.setText("Path:");

        btSelectOutputDirectory.setText("Select a Directory");
        btSelectOutputDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectOutputDirectoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btSelectOutputDirectory)
                                        .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfOutputDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(141, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfOutputDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btSelectOutputDirectory)
                                .addContainerGap(14, Short.MAX_VALUE))
        );

        tfDescription.setColumns(50);

        jLabel14.setText("Set a description for this execuition: (Optional)");

        btRun.setText("RUN");
        btRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRunActionPerformed(evt);
            }
        });

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Learning Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        jPanel10.setMaximumSize(new java.awt.Dimension(32767, 300));
        jPanel10.setName("Panel Settings"); // NOI18N
        jPanel10.setPreferredSize(new java.awt.Dimension(200, 99));

        cbClusteringAlgorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbClusteringAlgorithmActionPerformed(evt);
            }
        });

        jLabel20.setText("Select Clustering Algorithm");

        cbClusteringMoment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbClusteringMomentActionPerformed(evt);
            }
        });

        jLabel21.setText("Clustering Moment");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cbClusteringAlgorithm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                                                .addComponent(jLabel20)
                                                                .addGap(0, 35, Short.MAX_VALUE)))
                                                .addGap(8, 8, 8))
                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cbClusteringMoment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(jPanel10Layout.createSequentialGroup()
                                                                .addComponent(jLabel21)
                                                                .addGap(0, 87, Short.MAX_VALUE)))
                                                .addContainerGap())))
        );
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbClusteringMoment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbClusteringAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 1044, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(panelOperators, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(panelMutations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(jPanel7Layout.createSequentialGroup()
                                                .addComponent(jLabel14)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btRun, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panelOperators, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelMutations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14)
                                        .addComponent(btRun, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(124, 124, 124))
        );

        jTabbedPane1.addTab("Execution Configuration", jPanel7);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Design Pattern Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        jPanel16.setName("Panel Desig Patterns Selection"); // NOI18N

        ckMediator.setText("Mediator");
        ckMediator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckMediatorActionPerformed(evt);
            }
        });

        ckStrategy.setText("Strategy");
        ckStrategy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckStrategyActionPerformed(evt);
            }
        });

        ckBridge.setText("Bridge");
        ckBridge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckBridgeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ckMediator)
                                .addGap(18, 18, 18)
                                .addComponent(ckStrategy)
                                .addGap(18, 18, 18)
                                .addComponent(ckBridge)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ckMediator)
                                        .addComponent(ckStrategy)
                                        .addComponent(ckBridge))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelScopeSelection.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Scope Selection", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        panelScopeSelection.setName("Panel Scope Selection"); // NOI18N

        rbRandom.setText("Random");

        rbElements.setText("Elements with same design pattern or none");

        javax.swing.GroupLayout panelScopeSelectionLayout = new javax.swing.GroupLayout(panelScopeSelection);
        panelScopeSelection.setLayout(panelScopeSelectionLayout);
        panelScopeSelectionLayout.setHorizontalGroup(
                panelScopeSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelScopeSelectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelScopeSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(rbRandom)
                                        .addComponent(rbElements))
                                .addContainerGap(1271, Short.MAX_VALUE))
        );
        panelScopeSelectionLayout.setVerticalGroup(
                panelScopeSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelScopeSelectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(rbRandom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbElements)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelScopeSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelScopeSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(571, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Design Patterns", jPanel15);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Executions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        tbExecutions.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {},
                        {},
                        {},
                        {}
                },
                new String[]{

                }
        ));
        tbExecutions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbExecutionsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbExecutions);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Runs", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        tbRuns.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {},
                        {},
                        {},
                        {}
                },
                new String[]{

                }
        ));
        tbRuns.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRunsMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbRuns);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
                jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel26Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
                jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel26Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                                .addGap(15, 15, 15))
        );

        panelResultSolution.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelResultSolution.setName("Panel Result Solution"); // NOI18N

        cbSolutionName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSolutionNameActionPerformed(evt);
            }
        });

        lbSolution.setText("Solution:");

        javax.swing.GroupLayout panelResultSolutionLayout = new javax.swing.GroupLayout(panelResultSolution);
        panelResultSolution.setLayout(panelResultSolutionLayout);
        panelResultSolutionLayout.setHorizontalGroup(
                panelResultSolutionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelResultSolutionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbSolution)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbSolutionName, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelResultSolutionLayout.setVerticalGroup(
                panelResultSolutionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelResultSolutionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelResultSolutionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbSolutionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbSolution))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelResultObjetive.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelResultObjetive.setName("Panel Result Objetive"); // NOI18N

        cbObjectiveSoluction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbObjectiveSoluctionActionPerformed(evt);
            }
        });

        jLabel16.setText("Objective Solution:");

        tbObjectiveSolution.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {},
                        {},
                        {},
                        {}
                },
                new String[]{

                }
        ));
        jScrollPane5.setViewportView(tbObjectiveSolution);

        javax.swing.GroupLayout panelResultObjetiveLayout = new javax.swing.GroupLayout(panelResultObjetive);
        panelResultObjetive.setLayout(panelResultObjetiveLayout);
        panelResultObjetiveLayout.setHorizontalGroup(
                panelResultObjetiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelResultObjetiveLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelResultObjetiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane5)
                                        .addGroup(panelResultObjetiveLayout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbObjectiveSoluction, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        panelResultObjetiveLayout.setVerticalGroup(
                panelResultObjetiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelResultObjetiveLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelResultObjetiveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbObjectiveSoluction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel29.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbMetrics.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {},
                        {},
                        {},
                        {}
                },
                new String[]{

                }
        ));
        jScrollPane6.setViewportView(tbMetrics);

        btNonDomitedSolutions.setText("Non-Domited Solutions");
        btNonDomitedSolutions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNonDomitedSolutionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
                jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane6)
                                        .addGroup(jPanel29Layout.createSequentialGroup()
                                                .addComponent(btNonDomitedSolutions)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
                jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                                .addContainerGap(18, Short.MAX_VALUE)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btNonDomitedSolutions)
                                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelResultSolution, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelResultObjetive, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel18Layout.createSequentialGroup()
                                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(10, 10, 10))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap())))
        );
        jPanel18Layout.setVerticalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelResultSolution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(panelResultObjetive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(149, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Results", jPanel18);

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbExperiments.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{

                }
        ));
        jScrollPane1.setViewportView(tbExperiments);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1595, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Soluctions in the Seach Space", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        btSelectObjective.setText("Select the Objective");
        btSelectObjective.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelectObjectiveActionPerformed(evt);
            }
        });

        btGenerateChart.setText("Generate Chart");
        btGenerateChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGenerateChartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btSelectObjective)
                                .addGap(18, 18, 18)
                                .addComponent(btGenerateChart)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{btGenerateChart, btSelectObjective});

        jPanel21Layout.setVerticalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btSelectObjective)
                                        .addComponent(btGenerateChart))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Euclidean Distance", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        btEuclidianDistance.setText("Number Of Soluction Per Eucidean Distance");
        btEuclidianDistance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEuclidianDistanceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btEuclidianDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btEuclidianDistance)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hypervolume", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        btHypervolume.setText("Hypervolume");
        btHypervolume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHypervolumeActionPerformed(evt);
            }
        });

        ckUseNormalization.setText("Use Normalization");
        ckUseNormalization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckUseNormalizationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btHypervolume, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ckUseNormalization)
                                .addContainerGap(1146, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btHypervolume)
                                        .addComponent(ckUseNormalization))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Others", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        btBoxPlot.setText("BoxPlot");
        btBoxPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBoxPlotActionPerformed(evt);
            }
        });

        cbRScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRScriptActionPerformed(evt);
            }
        });

        jLabel18.setText("R Script");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
                jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel27Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btBoxPlot, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbRScript, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel18))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
                jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel27Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btBoxPlot)
                                .addGap(25, 25, 25)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbRScript, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(61, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(117, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Experiments", jPanel19);

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        taLogStatus.setColumns(114);
        taLogStatus.setRows(25);
        jScrollPane2.setViewportView(taLogStatus);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
                jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel25Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2)
                                        .addComponent(jProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
                jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel25Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
                jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(22, 22, 22))
        );
        jPanel24Layout.setVerticalGroup(
                jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(237, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Logs", jPanel24);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTabbedPane1)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTabbedPane1)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btTemplateDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTemplateDirectoryActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfTemplateDiretory, JFileChooser.DIRECTORIES_ONLY);
        updateTemplatePathYaml(tfTemplateDiretory.getText());
    }//GEN-LAST:event_btTemplateDirectoryActionPerformed

    private void cbObjectiveSoluctionActionPerformed(java.awt.event.ActionEvent evt) {
        loadMetricValue();
    }

    private void cbSolutionNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSolutionNameActionPerformed
        loadObjetiveNames();
        loadMetricValue();
    }//GEN-LAST:event_cbSolutionNameActionPerformed

    private void tbExecutionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbExecutionsMouseClicked
        if (isDoubleClick(evt)) {
            Experiment experiment = tmExecExperiments.getValue(tbExecutions.getSelectedRow());
            loadExecutionsInfo(experiment);
        }
    }//GEN-LAST:event_tbExecutionsMouseClicked

    private void tbRunsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRunsMouseClicked
        if (isDoubleClick(evt)) {
            loadComboSoluctions();
            loadComboObjectiveSolutionName();
            loadObjetiveNames();
        }
    }//GEN-LAST:event_tbRunsMouseClicked

    private void btBrowserSmartProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBrowserSmartProfileActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfSmartProfile, JFileChooser.FILES_ONLY);
        updateSmartyProfilePathYaml(tfSmartProfile.getText());
    }//GEN-LAST:event_btBrowserSmartProfileActionPerformed

    private void btBrowserFeatureProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBrowserFeatureProfileActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfFeatureProfile, JFileChooser.FILES_ONLY);
        updateFeatureProfilePathYaml(tfFeatureProfile.getText());
    }//GEN-LAST:event_btBrowserFeatureProfileActionPerformed

    private void btBrowserPatternProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBrowserPatternProfileActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfPatternProfile, JFileChooser.FILES_ONLY);
        updatePatternProfilePathYaml(tfPatternProfile.getText());
    }//GEN-LAST:event_btBrowserPatternProfileActionPerformed

    private void btBrowserRelationshipProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBrowserRelationshipProfileActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfRelationshipProfile, JFileChooser.FILES_ONLY);
        updateRelationshipProfilePathYaml(tfRelationshipProfile.getText());
    }//GEN-LAST:event_btBrowserRelationshipProfileActionPerformed

    private void btManipulationDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btManipulationDirectoryActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfManipulationDirectory, JFileChooser.DIRECTORIES_ONLY);
        updateManipulationPathYaml(tfManipulationDirectory.getText());
    }//GEN-LAST:event_btManipulationDirectoryActionPerformed

    private void setProfilesToSpecificPath(String path) {
        updateProfilesConfig(path);
        tfSmartProfile.setText(path + System.getProperty("file.separator") + "smarty.profile.uml");
        tfFeatureProfile.setText(path + System.getProperty("file.separator") + "concerns.profile.uml");
        tfPatternProfile.setText(path + System.getProperty("file.separator") + "patterns.profile.uml");
        tfRelationshipProfile.setText(path + System.getProperty("file.separator") + "relationships.profile.uml");
    }

    private void updateProfilesConfig(String path) {
        updateSmartyProfilePathYaml(path + System.getProperty("file.separator") + "smarty.profile.uml");
        updateFeatureProfilePathYaml(path + System.getProperty("file.separator") + "concerns.profile.uml");
        updatePatternProfilePathYaml(path + System.getProperty("file.separator") + "patterns.profile.uml");
        updateRelationshipProfilePathYaml(path + System.getProperty("file.separator") + "relationships.profile.uml");
    }

    private void btViewApplicationConfigActionPerformed(java.awt.event.ActionEvent evt) {
        ApplicationYamlView applicationYamlView = new ApplicationYamlView(config);
        applicationYamlView.setVisible(true);
    }

    private void ckEnableInteraction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckEnableInteraction
        tfInteractionDirectory1.setEnabled(ckEnableInteraction.isSelected());
        btManipulationDirectory3.setEnabled(ckEnableInteraction.isSelected());
        ckEnableInteraction1.setEnabled(ckEnableInteraction.isSelected());
    }//GEN-LAST:event_ckEnableInteraction

    private void ckMediatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckMediatorActionPerformed
        enableAllRadioButons(panelScopeSelection, ckMediator.isSelected());
        if (ckMediator.isSelected())
            MutationOperatorsSelected.getSelectedPatternsToApply().add("Mediator");
        else
            MutationOperatorsSelected.getSelectedPatternsToApply().remove("Mediator");
    }//GEN-LAST:event_ckMediatorActionPerformed

    private void ckStrategyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckStrategyActionPerformed
        if (ckStrategy.isSelected())
            MutationOperatorsSelected.getSelectedPatternsToApply().add("Strategy");
        else
            MutationOperatorsSelected.getSelectedPatternsToApply().remove("Strategy");
    }//GEN-LAST:event_ckStrategyActionPerformed

    private void ckBridgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckBridgeActionPerformed
        if (ckBridge.isSelected())
            MutationOperatorsSelected.getSelectedPatternsToApply().add("Bridge");
        else
            MutationOperatorsSelected.getSelectedPatternsToApply().remove("Bridge");
    }//GEN-LAST:event_ckBridgeActionPerformed

    private void ckEnableInteraction1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckEnableInteraction1
        if (tfInteractionDirectory1.getText() == null || tfInteractionDirectory1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You need select a Papyrus Path");
            ckEnableInteraction1.setSelected(false);
        } else {
            tfInteractionDirectory1.setEnabled(ckEnableInteraction1.isSelected());
            btManipulationDirectory3.setEnabled(ckEnableInteraction1.isSelected());
        }

    }//GEN-LAST:event_ckEnableInteraction1

    private void btManipulationDirectory3btInteractionDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btManipulationDirectory3btInteractionDirectoryActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfInteractionDirectory1, JFileChooser.FILES_ONLY);
        updatePapyrusPathYaml(tfInteractionDirectory1.getText());
        ckEnableInteraction1.setSelected(tfInteractionDirectory1.getText() != null && !tfInteractionDirectory1.getText().isEmpty());
    }//GEN-LAST:event_btManipulationDirectory3btInteractionDirectoryActionPerformed

    private void cbClusteringAlgorithmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbClusteringAlgorithmActionPerformed
        if (cbClusteringAlgorithm.isEnabled() && cbClusteringAlgorithm.getSelectedIndex() < 0)
            cbClusteringAlgorithm.setSelectedIndex(0);
    }//GEN-LAST:event_cbClusteringAlgorithmActionPerformed

    private void btRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRunActionPerformed
        optimizationRun();
    }//GEN-LAST:event_btRunActionPerformed

    private void btSelectOutputDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectOutputDirectoryActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfOutputDirectory, JFileChooser.DIRECTORIES_ONLY);
        updateOutputPathYaml(tfOutputDirectory.getText());
    }//GEN-LAST:event_btSelectOutputDirectoryActionPerformed

    private void btSelectPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectPathActionPerformed
        FileSelectorForm form = new FileSelectorForm();
        form.openSeletor(tfInputArchitecturePath, JFileChooser.FILES_ONLY);
    }//GEN-LAST:event_btSelectPathActionPerformed

    private void btCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCleanActionPerformed
        tfInputArchitecturePath.setText(StringUtils.EMPTY);
    }//GEN-LAST:event_btCleanActionPerformed

    private void btConfirmeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConfirmeActionPerformed
        this.inputArchitecture = tfInputArchitecturePath.getText();
        updatePathLastOptimizationInput(tfInputArchitecturePath.getText());
        String path = tfInputArchitecturePath.getText().substring(0, tfInputArchitecturePath.getText().lastIndexOf(System.getProperty("file.separator")));
        setProfilesToSpecificPath(path);
        JOptionPane.showMessageDialog(this, "The profiles have been updated. Please check if they are correct!");
    }//GEN-LAST:event_btConfirmeActionPerformed

    private void ckMoveAttributeMutationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckMoveAttributeMutationItemStateChanged
        if (ckMoveOperationMutation.isSelected())
            MutationOperatorsSelected.getSelectedMutationOperators().add(FeatureMutationOperators.MOVE_OPERATION_MUTATION.getOperatorName());
        else
            MutationOperatorsSelected.getSelectedMutationOperators().remove(FeatureMutationOperators.MOVE_OPERATION_MUTATION.getOperatorName());
    }//GEN-LAST:event_ckMoveAttributeMutationItemStateChanged

    private void ckAddManagerClassMutationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckAddManagerClassMutationItemStateChanged
        if (ckAddManagerClassMutation.isSelected())
            MutationOperatorsSelected.getSelectedMutationOperators().add(FeatureMutationOperators.ADD_MANAGER_CLASS_MUTATION.getOperatorName());
        else
            MutationOperatorsSelected.getSelectedMutationOperators().remove(FeatureMutationOperators.ADD_MANAGER_CLASS_MUTATION.getOperatorName());
    }//GEN-LAST:event_ckAddManagerClassMutationItemStateChanged

    private void ckMoveOperationMutationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckMoveOperationMutationItemStateChanged
        if (ckMoveOperationMutation.isSelected())
            MutationOperatorsSelected.getSelectedMutationOperators().add(FeatureMutationOperators.MOVE_OPERATION_MUTATION.getOperatorName());
        else
            MutationOperatorsSelected.getSelectedMutationOperators().remove(FeatureMutationOperators.MOVE_OPERATION_MUTATION.getOperatorName());
    }//GEN-LAST:event_ckMoveOperationMutationItemStateChanged

    private void ckAddClassMutationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckAddClassMutationItemStateChanged
        if (ckAddClassMutation.isSelected())
            MutationOperatorsSelected.getSelectedMutationOperators().add(FeatureMutationOperators.ADD_CLASS_MUTATION.getOperatorName());
        else
            MutationOperatorsSelected.getSelectedMutationOperators().remove(FeatureMutationOperators.ADD_CLASS_MUTATION.getOperatorName());
    }//GEN-LAST:event_ckAddClassMutationItemStateChanged

    private void ckMoveMethodMutationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckMoveMethodMutationItemStateChanged
        if (ckMoveMethodMutation.isSelected())
            MutationOperatorsSelected.getSelectedMutationOperators().add(FeatureMutationOperators.MOVE_METHOD_MUTATION.getOperatorName());
        else
            MutationOperatorsSelected.getSelectedMutationOperators().remove(FeatureMutationOperators.MOVE_METHOD_MUTATION.getOperatorName());
    }//GEN-LAST:event_ckMoveMethodMutationItemStateChanged

    private void ckFeatureDrivenMutationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckFeatureDrivenMutationItemStateChanged
        if (ckFeatureDrivenMutation.isSelected())
            MutationOperatorsSelected.getSelectedMutationOperators().add(FeatureMutationOperators.FEATURE_MUTATION.getOperatorName());
        else
            MutationOperatorsSelected.getSelectedMutationOperators().remove(FeatureMutationOperators.FEATURE_MUTATION.getOperatorName());
    }//GEN-LAST:event_ckFeatureDrivenMutationItemStateChanged

    private void ckCrossoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckCrossoverActionPerformed
        enableCrossoverOption();
    }//GEN-LAST:event_ckCrossoverActionPerformed

    private void ckMutationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckMutationActionPerformed
        enableMutationOption();
    }//GEN-LAST:event_ckMutationActionPerformed

    private void ckLLCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckLLCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ckLLCActionPerformed

    private void ckAVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckAVActionPerformed
        System.out.println("AV");
        final String metric = Metrics.AV.getName();
        addToMetrics(ckAV, metric);
    }//GEN-LAST:event_ckAVActionPerformed

    private void ckSSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckSSCActionPerformed
        System.out.println("SSC");
        final String metric = Metrics.SSC.getName();
        addToMetrics(ckSSC, metric);
    }//GEN-LAST:event_ckSSCActionPerformed

    private void ckSVSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckSVSActionPerformed
        System.out.println("SVC");
        final String metric = Metrics.SVC.getName();
        addToMetrics(ckSVS, metric);
    }//GEN-LAST:event_ckSVSActionPerformed

    private void ckCBCSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckCBCSActionPerformed
        System.out.println("CBCS");
        final String metric = Metrics.CBCS.getName();
        addToMetrics(ckCBCS, metric);
    }//GEN-LAST:event_ckCBCSActionPerformed

    private void ckWocsinterfaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckWocsinterfaceActionPerformed
        System.out.println("WOCSINTER");
        final String metric = Metrics.WOCSINTER.getName();
        addToMetrics(ckWocsInterface, metric);
    }//GEN-LAST:event_ckWocsinterfaceActionPerformed

    private void ckWocsClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckWocsClassActionPerformed
        System.out.println("WOCSCLASS");
        final String metric = Metrics.WOCSCLASS.getName();
        addToMetrics(ckWocsClass, metric);
    }//GEN-LAST:event_ckWocsClassActionPerformed

    private void ckEleganceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckEleganceActionPerformed
        System.out.println("Elegance");
        String metric = Metrics.ELEGANCE.getName();
        addToMetrics(ckElegance, metric);
    }//GEN-LAST:event_ckEleganceActionPerformed

    private void ckCohesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckCohesionActionPerformed
        System.out.println("Cohesion");
        String metric = Metrics.COE.getName();
        addToMetrics(ckCohesion, metric);
    }//GEN-LAST:event_ckCohesionActionPerformed

    private void ckPLAExtensibilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckPLAExtensibilityActionPerformed
        System.out.println("PLA Extensibility");
        String metric = Metrics.PLA_EXTENSIBILIY.getName();
        addToMetrics(ckPLAExtensibility, metric);
    }//GEN-LAST:event_ckPLAExtensibilityActionPerformed

    private void ckFeatureDifusionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckFeatureDifusionActionPerformed
        System.out.println("Features Diffusion");
        String metric = Metrics.DC.getName();
        addToMetrics(ckFeatureDifusion, metric);
    }//GEN-LAST:event_ckFeatureDifusionActionPerformed

    private void ckFeatureInterlacingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckFeatureInterlacingActionPerformed
        System.out.println("Features Interlacing");
        String metric = Metrics.EC.getName();
        addToMetrics(ckFeatureInterlacing, metric);
    }//GEN-LAST:event_ckFeatureInterlacingActionPerformed

    private void ckFeatureDrivenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckFeatureDrivenActionPerformed
        System.out.println("Feature Driven");
        String metric = Metrics.FEATURE_DRIVEN.getName();
        addToMetrics(ckFeatureDriven, metric);
    }//GEN-LAST:event_ckFeatureDrivenActionPerformed

    private void ckSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckSizeActionPerformed
        System.out.println("Tam");
        String metric = Metrics.TAM.getName();
        addToMetrics(ckSize, metric);
    }//GEN-LAST:event_ckSizeActionPerformed

    private void ckClassCouplingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckClassCouplingActionPerformed
        System.out.println("Class Coupling");
        String metric = Metrics.ACLASS.getName();
        addToMetrics(ckClassCoupling, metric);
    }//GEN-LAST:event_ckClassCouplingActionPerformed

    private void ckComponentCouplingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckComponentCouplingActionPerformed
        System.out.println("Component Coupling");
        String metric = Metrics.ACOMP.getName();
        addToMetrics(ckComponentCoupling, metric);
    }//GEN-LAST:event_ckComponentCouplingActionPerformed

    private void ckConventionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckConventionalActionPerformed
        System.out.println("Conventional");
        String metric = Metrics.CONVENTIONAL.getName();
        addToMetrics(ckConventional, metric);
    }//GEN-LAST:event_ckConventionalActionPerformed

    private void cbClusteringMomentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbClusteringMomentActionPerformed
        cbClusteringAlgorithm.setEnabled(!Objects.equals(cbClusteringMoment.getSelectedItem(), Moment.NONE));
    }//GEN-LAST:event_cbClusteringMomentActionPerformed

    private void btHypervolumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btHypervolumeActionPerformed
        try {
            int[] selectedRows = tbExperiments.getSelectedRows();
            String ids[] = new String[selectedRows.length];
            String funcs = "";

            for (int i = 0; i < selectedRows.length; i++) {
                ids[i] = tbExperiments.getModel().getValueAt(selectedRows[i], 0).toString();
                String functions = db.Database.getOrdenedObjectives(ids[i]);
                if (funcs.isEmpty()) {
                    funcs = functions;
                } else if (!funcs.equalsIgnoreCase(functions)) {
                    JOptionPane.showMessageDialog(null, rb.getString("notSameFunctions"));

                    return;
                }

            }

            HypervolumeWindow hyperPanel = new HypervolumeWindow();
            hyperPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            if (VolatileConfs.hypervolumeNormalized()) {
                hyperPanel.setTitle("Hypervolume - Normalized");
            } else {
                hyperPanel.setTitle("Hypervolume - Non Normalized");
            }
            hyperPanel.pack();
            hyperPanel.setResizable(false);
            hyperPanel.setVisible(true);

            hyperPanel.loadData(ids);
        } catch (IOException ex) {
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
            JOptionPane.showMessageDialog(null, rb.getString("errorGenerateHypervolumeTable"));
        }
    }//GEN-LAST:event_btHypervolumeActionPerformed

    private void btEuclidianDistanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEuclidianDistanceActionPerformed
        int[] selectedRows = tbExperiments.getSelectedRows();
        String ids[] = new String[selectedRows.length];

        for (int i = 0; i < selectedRows.length; i++) {
            ids[i] = tbExperiments.getModel().getValueAt(selectedRows[i], 0).toString();
        }

        String name = db.Database.getPlaUsedToExperimentId(ids[0]);

        if (selectedRows.length >= 1) {
            String typeChart = GuiFile.getInstance().getEdChartType();
            if ("bar".equalsIgnoreCase(typeChart)) {
                EdBar edBar = new EdBar(ids, "Euclidean Distance (" + name + ")");
                edBar.displayOnFrame();
            } else if ("line".equals(typeChart)) {
                EdLine edLine = new EdLine(ids, null);
                edLine.displayOnFrame();
            } else {
                JOptionPane.showMessageDialog(null, rb.getString("confEdChartInvalid"));
            }
        } else {
            JOptionPane.showMessageDialog(null, rb.getString("atLeastOneExecution"));
        }
    }//GEN-LAST:event_btEuclidianDistanceActionPerformed

    private void btGenerateChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGenerateChartActionPerformed
        String referenceExp = null;
        List<JCheckBox> allChecks = new ArrayList<>();
        List<JCheckBox> checkeds = new ArrayList<>();
        HashMap<String, String> experimentToAlgorithmUsed = new HashMap<>();

        for (Object comp : jPanel21.getComponents()) {
            if (comp instanceof JCheckBox) {
                JCheckBox checkBox = ((JCheckBox) comp);
                if (checkBox.isSelected()) {
                    checkeds.add(checkBox);
                }
                allChecks.add(checkBox);
            }
        }

        for (JCheckBox box : checkeds) {
            String id = box.getName().split(",")[0]; // experimentID
            referenceExp = id;
            String algorithmUsed = db.Database.getAlgoritmUsedToExperimentId(id);
            experimentToAlgorithmUsed.put(id, algorithmUsed);
        }
        if (Validators.hasMoreThatTwoFunctionsSelectedForSelectedExperiments(allChecks)) {
            JOptionPane.showMessageDialog(null, rb.getString("onlyTwoFunctions"));
        } else if (checkeds.isEmpty()) {
            JOptionPane.showMessageDialog(null, rb.getString("atLeastTwoFunctionPerSelectedExperiment"));
        } else if (Validators.validateCheckedsFunctions(allChecks)) {
            JOptionPane.showMessageDialog(null, rb.getString("sameFunctions"));
        } else {
            String[] functions = new String[2]; // x,y Axis
            int[] columns = new int[2]; // Quais colunas do arquivo deseja-se
            // ler.

            for (int i = 0; i < 2; i++) {
                final String[] splited = checkeds.get(i).getName().split(",");
                columns[i] = Integer.parseInt(splited[2]);
                functions[i] = splited[1];
            }

            String outputDir = this.config.getConfig().getDirectoryToExportModels().toString();
            try {
                ChartGenerate.generate(functions, experimentToAlgorithmUsed, columns, outputDir, referenceExp);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(StartUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btGenerateChartActionPerformed

    private void btSelectObjectiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelectObjectiveActionPerformed
        jPanel21.setLayout(new MigLayout());

        for (Component comp : jPanel21.getComponents()) {
            if (comp instanceof JCheckBox) {
                jPanel21.remove((JCheckBox) comp);
            }
            if (comp instanceof JLabel) {
                jPanel21.remove((JLabel) comp);
            }

        }

        int[] selectedRows = tbExperiments.getSelectedRows();
        HashMap<String, String[]> map = new HashMap<>();

        for (int i = 0; i < selectedRows.length; i++) {
            String experimentId = tbExperiments.getModel().getValueAt(selectedRows[i], 0).toString();
            map.put(experimentId, db.Database.getOrdenedObjectives(experimentId).split(" "));
        }

        // Validacao
        if (selectedRows.length <= 1) {
            JOptionPane.showMessageDialog(null, rb.getString("atLeastTwoExecution"));
            return;
        } else if (selectedRows.length > 5) {
            JOptionPane.showMessageDialog(null, rb.getString("maxExecutions"));
            return;
        } else if (!Validators.selectedsExperimentsHasTheSameObjectiveFunctions(map)) {
            JOptionPane.showMessageDialog(null, rb.getString("notSameFunctions"));
            return;
        }

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String experimentId = entry.getKey();
            String[] values = entry.getValue();
            jPanel21.add(new JLabel(""), "wrap");
            jPanel21.add(new JLabel("Execution: " + experimentId + "\n"), "wrap");
            for (int i = 0; i < values.length; i++) {
                JCheckBox box = new JCheckBox(values[i].toUpperCase());
                box.setName(experimentId + "," + values[i] + "," + i); // id do
                // experimemto,
                // nome
                // da
                // funcao,
                // indice
                jPanel21.add(box, "span, grow");
            }
        }

        jPanel21.updateUI();
    }//GEN-LAST:event_btSelectObjectiveActionPerformed

    private void ckUseNormalizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckUseNormalizationActionPerformed
        if (ckUseNormalization.isSelected())
            VolatileConfs.enableHybervolumeNormalization();
        else
            VolatileConfs.disableHybervolumeNormalization();
    }//GEN-LAST:event_ckUseNormalizationActionPerformed

    private void btNonDomitedSolutionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNonDomitedSolutionsActionPerformed
        SmallerFintnessValuesWindow sfvw = new SmallerFintnessValuesWindow();

        sfvw.setVisible(true);
        Execution value = tmExecution.getValue(tbRuns.getSelectedRow());
        sfvw.setTitle("Execution " + value.getDescription());
        sfvw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sfvw.setExperimentId(value.getExperiment().getId().toString());
        sfvw.enablePanelsObjectiveFunctions();
        sfvw.loadEds();
    }//GEN-LAST:event_btNonDomitedSolutionsActionPerformed

    private void btBoxPlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBoxPlotActionPerformed
        try {
            int[] selectedRows = tbExperiments.getSelectedRows();
            String ids[] = new String[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++) {
                ids[i] = tbExperiments.getModel().getValueAt(selectedRows[i], 0).toString();
            }
            Map<String, List<Double>> content = db.Database.getAllObjectivesForDominatedSolutions(ids);
            List<HypervolumeData> hypers = HypervolumeGenerateObjsData.generate(content);
            List<BoxPlotItem> collect = hypers.stream()
                    .map(h -> new BoxPlotItem(h.getValues(), h.getAlgorithm(), h.getIdExperiment())).collect(Collectors.toList());
            BoxPlot boxPlot = new BoxPlot(collect);
            boxPlot.display();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btBoxPlotActionPerformed

    private void cbRScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRScriptActionPerformed
        try {
            int[] selectedRows = tbExperiments.getSelectedRows();
            String ids[] = new String[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++) {
                ids[i] = tbExperiments.getModel().getValueAt(selectedRows[i], 0).toString();
            }
            Map<String, List<Double>> content = db.Database.getAllObjectivesForDominatedSolutions(ids);
            List<HypervolumeData> hypervolumeData = HypervolumeGenerateObjsData.generate(content);
            List<List<Double>> collect = hypervolumeData.stream().map(h -> h.getValues()).collect(Collectors.toList());
            String result = ((RScriptOption) cbRScript.getSelectedItem()).getResult(new RScriptOptionElement(collect, UserHome.getOplaUserHome() + Constants.TEMP_DIR + Constants.FILE_SEPARATOR));

            if (result != null) {
                Component jta = null;
                if (result.contains("oplatool")) {
                    System.out.println(result);
                    jta = new JLabel(new ImageIcon(result));
                } else {
                    jta = new JTextArea(result);
                }


                JScrollPane jsp = new JScrollPane(jta) {

                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(700, 700);
                    }
                };
                JOptionPane.showMessageDialog(null, jsp, ((RScriptOption) cbRScript.getSelectedItem()).getDescription(), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cbRScriptActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBoxPlot;
    private javax.swing.JButton btBrowserFeatureProfile;
    private javax.swing.JButton btBrowserPatternProfile;
    private javax.swing.JButton btBrowserRelationshipProfile;
    private javax.swing.JButton btBrowserSmartProfile;
    private javax.swing.JButton btClean;
    private javax.swing.JButton btConfirme;
    private javax.swing.JButton btEuclidianDistance;
    private javax.swing.JButton btGenerateChart;
    private javax.swing.JButton btHypervolume;
    private javax.swing.JButton btManipulationDirectory;
    private javax.swing.JButton btManipulationDirectory3;
    private javax.swing.JButton btNonDomitedSolutions;
    private javax.swing.JButton btRun;
    private javax.swing.JButton btSelectObjective;
    private javax.swing.JButton btSelectOutputDirectory;
    private javax.swing.JButton btSelectPath;
    private javax.swing.JButton btTemplateDirectory;
    private javax.swing.JButton btViewApplicationConfig;
    private javax.swing.JComboBox<String> cbAlgothm;
    private javax.swing.JComboBox<String> cbClusteringAlgorithm;
    private javax.swing.JComboBox<String> cbClusteringMoment;
    private javax.swing.JComboBox<String> cbObjectiveSoluction;
    private javax.swing.JComboBox<String> cbRScript;
    private javax.swing.JComboBox<String> cbSolutionName;
    private javax.swing.JCheckBox ckAV;
    private javax.swing.JCheckBox ckAddClassMutation;
    private javax.swing.JCheckBox ckAddManagerClassMutation;
    private javax.swing.JCheckBox ckBridge;
    private javax.swing.JCheckBox ckCBCS;
    private javax.swing.JCheckBox ckClassCoupling;
    private javax.swing.JCheckBox ckCohesion;
    private javax.swing.JCheckBox ckComponentCoupling;
    private javax.swing.JCheckBox ckConventional;
    private javax.swing.JCheckBox ckCrossover;
    private javax.swing.JCheckBox ckElegance;
    private javax.swing.JCheckBox ckEnableInteraction;
    private javax.swing.JCheckBox ckEnableInteraction1;
    private javax.swing.JCheckBox ckFeature;
    private javax.swing.JCheckBox ckFeatureDifusion;
    private javax.swing.JCheckBox ckFeatureDriven;
    private javax.swing.JCheckBox ckFeatureDrivenMutation;
    private javax.swing.JCheckBox ckFeatureInterlacing;
    private javax.swing.JCheckBox ckLLC;
    private javax.swing.JCheckBox ckMediator;
    private javax.swing.JCheckBox ckMoveAttributeMutation;
    private javax.swing.JCheckBox ckMoveMethodMutation;
    private javax.swing.JCheckBox ckMoveOperationMutation;
    private javax.swing.JCheckBox ckMutation;
    private javax.swing.JCheckBox ckPLAExtensibility;
    private javax.swing.JCheckBox ckPatterns;
    private javax.swing.JCheckBox ckRelationship;
    private javax.swing.JCheckBox ckSSC;
    private javax.swing.JCheckBox ckSVS;
    private javax.swing.JCheckBox ckSize;
    private javax.swing.JCheckBox ckSmarty;
    private javax.swing.JCheckBox ckStrategy;
    private javax.swing.JCheckBox ckUseNormalization;
    private javax.swing.JCheckBox ckWocsClass;
    private javax.swing.JCheckBox ckWocsInterface;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSlider jsCrossover;
    private javax.swing.JSlider jsMutation;
    private javax.swing.JLabel lbSolution;
    private javax.swing.JPanel panelCkProfiles;
    private javax.swing.JPanel panelMutations;
    private javax.swing.JPanel panelOperatorOption;
    private javax.swing.JPanel panelOperators;
    private javax.swing.JPanel panelResultObjetive;
    private javax.swing.JPanel panelResultSolution;
    private javax.swing.JPanel panelScopeSelection;
    private javax.swing.JRadioButton rbElements;
    private javax.swing.JRadioButton rbRandom;
    private javax.swing.JTextArea taLogStatus;
    private javax.swing.JTable tbExecutions;
    private javax.swing.JTable tbExperiments;
    private javax.swing.JTable tbMetrics;
    private javax.swing.JTable tbObjectiveSolution;
    private javax.swing.JTable tbRuns;
    private javax.swing.JTextField tfArchiveSize;
    private javax.swing.JTextField tfArchiveSize1;
    private javax.swing.JTextField tfDescription;
    private javax.swing.JTextField tfFeatureProfile;
    private javax.swing.JTextField tfInputArchitecturePath;
    private javax.swing.JTextField tfInteractionDirectory1;
    private javax.swing.JTextField tfManipulationDirectory;
    private javax.swing.JTextField tfMaxEvaluations;
    private javax.swing.JTextField tfNumberRuns;
    private javax.swing.JTextField tfOutputDirectory;
    private javax.swing.JTextField tfPatternProfile;
    private javax.swing.JTextField tfPopulationSize;
    private javax.swing.JTextField tfRelationshipProfile;
    private javax.swing.JTextField tfSmartProfile;
    private javax.swing.JTextField tfTemplateDiretory;
    // End of variables declaration//GEN-END:variables

    // @formatter:on
    private void updateTemplatePathYaml(String path) {
        try {
            config.updatePathToTemplateFiles(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar diretório", e);
            AlertUtil.showMessage("Erro ao selecionar diretório");
        }
    }

    private void updateManipulationPathYaml(String path) {
        try {
            config.updatePathToExportModels(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar diretório", e);
            AlertUtil.showMessage("Erro ao selecionar diretório");
        }
    }

    private void updateInteractionPathYaml(String path) {
        try {
            config.updatePathToInteraction(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar diretório", e);
            AlertUtil.showMessage("Erro ao selecionar diretório");
        }
    }

    private void updatePapyrusPathYaml(String path) {
        try {
            config.updatePathPapyurs(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar diretório", e);
            AlertUtil.showMessage("Erro ao selecionar diretório");
        }
    }


    private void updateOutputPathYaml(String path) {
        try {
            config.updatePathToSaveModels(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar diretório", e);
            AlertUtil.showMessage("Erro ao selecionar diretório");
        }
    }

    private void updateSmartyProfilePathYaml(String path) {
        try {
            config.updatePathToProfileSmarty(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar arquivo", e);
            AlertUtil.showMessage("Erro ao selecionar arquivo");
        }
    }

    private void updateFeatureProfilePathYaml(String path) {
        try {
            config.updatePathToProfileConcerns(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar arquivo", e);
            AlertUtil.showMessage("Erro ao selecionar arquivo");
        }
    }

    private void updatePatternProfilePathYaml(String path) {
        try {
            config.updatePathToProfilePatterns(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar arquivo", e);
            AlertUtil.showMessage("Erro ao selecionar arquivo");
        }
    }

    private void updateRelationshipProfilePathYaml(String path) {
        try {
            config.updatePathToProfileRelationships(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar arquivo", e);
            AlertUtil.showMessage("Erro ao selecionar arquivo");
        }
    }

    private void updatePathLastOptimizationInput(String path) {
        try {
            config.updatePathLastOptimizationInput(path);
        } catch (IOException e) {
            LOGGER.debug("Erro ao selecionar arquivo", e);
            AlertUtil.showMessage("Erro ao selecionar arquivo");
        }
    }

    private void enableMutationOption() {
        jsMutation.setEnabled(ckMutation.isSelected());
        enableAllChecks(panelMutations, ckMutation.isSelected());
    }

    private void enableCrossoverOption() {
        jsCrossover.setEnabled(ckCrossover.isSelected());
    }

    private void loadExecutionsData() {
        List<Experiment> experiments = experimentDAO.findAllOrdened();
        this.tmExecExperiments.setLista(experiments);
        tbExecutions.setModel(tmExecExperiments);
        tbExecutions.updateUI();
        this.tmExperiments.setLista(experiments);
        tbExperiments.setModel(tmExperiments);
        tbExperiments.updateUI();

    }

    private void loadExecutionsInfo(Experiment experiment) {
        List<Execution> executions = executionDAO.findByExperiment(experiment);
        Long numberNonDominatedSolution = objectiveDAO.countNonDominatedSolution(experiment);
        tmExecution.setData(executions, numberNonDominatedSolution);
        tbRuns.setModel(tmExecution);
        tbRuns.updateUI();
        cleanCompoments();
    }

    @SuppressWarnings("unchecked")
    private void loadComboSoluctions() {
        Experiment experiment = tmExecExperiments.getValue(tbExecutions.getSelectedRow());
        Execution execution = tmExecution.getValue(tbRuns.getSelectedRow());
        List<String> solutionsName = objectiveDAO.findNameSolution(experiment, execution);
        solutionNameComboModel.setList(solutionsName);
        cbSolutionName.setModel(solutionNameComboModel);
        cbSolutionName.setSelectedIndex(0);
        cbSolutionName.updateUI();

    }

    @SuppressWarnings("unchecked")
    private void loadComboObjectiveSolutionName() {
        Experiment experiment = tmExecExperiments.getValue(tbExecutions.getSelectedRow());
        List<String> listNames = mapObjectivesDAO.findNamesByExperiment(experiment);
        objetiveNameComboModel.setList(listNames);
        cbObjectiveSoluction.setModel(objetiveNameComboModel);
        cbObjectiveSoluction.setSelectedIndex(0);
        cbObjectiveSoluction.updateUI();
    }

    private void loadObjetiveNames() {
        String solutionName = solutionNameComboModel.getSelectedItem();
        if (StringUtils.isNotBlank(solutionName)) {
            Experiment experiment = tmExecExperiments.getValue(tbExecutions.getSelectedRow());
            Execution execution = tmExecution.getValue(tbRuns.getSelectedRow());
            List<BigDecimal> objectiveValues = objectiveDAO.findObjectiveValues(experiment, execution, solutionName);
            List<String> listNames = mapObjectivesDAO.findNamesByExperiment(experiment);
            tmMapObjectiveSolution.setData(objectiveValues, listNames);
            tbObjectiveSolution.setModel(tmMapObjectiveSolution);
            tbObjectiveSolution.updateUI();
        }
    }

    public void loadMetricValue() {
        String objetiveSolutionName = objetiveNameComboModel.getSelectedItem();
        if (StringUtils.isNotBlank(objetiveSolutionName)) {
            Metric metric = Metric.getMetricByName(objetiveSolutionName);
            if (metric != null) {
                String solutionName = solutionNameComboModel.getSelectedItem();
                AbstractMetricTableModel tableModel = metric.getTableModel();
                GenericMetricDAO<GenericMetric> DAO = metric.getDAO();
                tableModel.setLista(DAO.findBySolution(solutionName));
                tbMetrics.setModel(tableModel);
                tbMetrics.updateUI();
            } else {
                tbMetrics.setModel(new DefaultTableModel());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void cleanCompoments() {
        cleanAllSelectedComboBox(panelResultSolution);
        cleanAllSelectedComboBox(panelResultObjetive);
        solutionNameComboModel.setList(Collections.emptyList());
        cbSolutionName.setModel(solutionNameComboModel);
        cbSolutionName.updateUI();
        objetiveNameComboModel.setList(Collections.emptyList());
        cbObjectiveSoluction.setModel(objetiveNameComboModel);
        cbObjectiveSoluction.updateUI();
        tmMapObjectiveSolution.setData(Collections.emptyList(), Collections.emptyList());
        tbObjectiveSolution.setModel(tmMapObjectiveSolution);
        tbObjectiveSolution.updateUI();
        tbMetrics.setModel(new DefaultTableModel());
        tbMetrics.updateUI();
    }

    private void optimizationRun() {

        // Validacoes inicias
        // Verifica se as entradas sao validas. Caso contrario finaliza
//        if (Validators.validateEntries(fieldArchitectureInput.getText())) {
//            return;
//        }

        // Recupera o algoritmo selecionado pelo usuário
        String algoritmToRun = VolatileConfs.getAlgorithmName();

        // Caso nenhum for selecionado, informa o usuario
        if (cbAlgothm.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "You need select a algorithm");
        } else {
            // Pede confirmacao para o usuario para de fato executar o
            // experimento.
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "This will take a time." + " Meanwhile the UI will be blocked", "You have sure?", dialogButton);
            // Caso usuário aceite, verifica qual algoritmo executar
            // E invoca a classe responsável.
            if (dialogResult == 0) {
                if ("NSGA II".equalsIgnoreCase(cbAlgothm.getSelectedItem().toString())) {
                    SwingWorker sw = new SwingWorker() {

                        @Override
                        protected Object doInBackground() throws Exception {
                            jLabel12.setText("Working... wait. Started " + Time.timeNow());
                            Logger.getLogger().putLog("Execution NSGAII...");
                            jTabbedPane1.setSelectedIndex(5);
                            btRun.setEnabled(false);
                            jProgressBar.setIndeterminate(true);
                            executeNSGAII();
                            return 0;
                        }

                        @Override
                        protected void done() {
                            jLabel12.setText("Done");
                            jProgressBar.setIndeterminate(false);
                            Logger.getLogger().putLog(String.format("Done NSGAII Execution at: %s", Time.timeNow().toString()));
                            db.Database.reloadContent();
                            loadExecutionsData();
                        }
                    };

                    sw.execute();

                }
                if ("PAES".equalsIgnoreCase(algoritmToRun)) {
                    SwingWorker sw2 = new SwingWorker() {

                        @Override
                        protected Object doInBackground() throws Exception {
                            jLabel12.setText("Working... wait. Started " + Time.timeNow());
                            Logger.getLogger().putLog("Execution PAES...");
                            jTabbedPane1.setSelectedIndex(5);
                            btRun.setEnabled(false);
                            jProgressBar.setIndeterminate(true);
                            executePAES();
                            return 0;
                        }

                        @Override
                        protected void done() {
                            jLabel12.setText("Done");
                            jProgressBar.setIndeterminate(false);
                            Logger.getLogger()
                                    .putLog(String.format("Done PAES Execution at: %s", Time.timeNow().toString()));
                            db.Database.reloadContent();
                            loadExecutionsData();
                        }
                    };

                    sw2.execute();
//                    progressBar.setIndeterminate(true);
                }

            }
        }
    }

    public static void executeCommandLineAlgorithm(AlgorithmExperiment algorithmExperiment) {
        try {
            MutationOperatorsSelected.getSelectedMutationOperators().addAll(algorithmExperiment.getMutationOperators());
            MutationOperatorsSelected.getSelectedPatternsToApply().addAll(algorithmExperiment.getPatterns());
            VolatileConfs.getObjectiveFunctionSelected().addAll(algorithmExperiment.getObjectiveFunctions());
            switch (algorithmExperiment.getAlgorithm()) {
                case NSGAII:
                    NSGAII nsgaii = new NSGAII();
                    nsgaii.execute(algorithmExperiment);
                    break;
                case PAES:

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeNSGAII() {
        try {
            NSGAII nsgaii = new NSGAII();
            nsgaii.execute(cbAlgothm, ckMutation, jsMutation, tfInputArchitecturePath, tfNumberRuns,
                    tfPopulationSize, tfMaxEvaluations, ckCrossover, jsCrossover,
                    tfDescription, ckEnableInteraction, tfArchiveSize1, cbClusteringAlgorithm, cbClusteringMoment, (solutionSet, execution) -> {
                        InteractiveSolutions interactiveSolutions = new InteractiveSolutions(config, solutionSet, execution);
                    });
            JOptionPane.showMessageDialog(null, "Success execution NSGA-II, Finalizing....");
            Logger.getLogger().putLog(
                    String.format("Success execution NSGA-II, Finalizing...", Level.INFO, StartUp.class.getName()));
            btRun.setEnabled(true);

        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error when try execute NSGA-II, Finalizing...." + e.getMessage());
            Logger.getLogger().putLog(
                    String.format("Error when try execute NSGA-II, Finalizing...", Level.FATAL, StartUp.class.getName()));
            btRun.setEnabled(true);
        }
    }

    private void addToMetrics(JCheckBox check, final String metric) {
        if (!check.isSelected()) {
            VolatileConfs.getObjectiveFunctionSelected().remove(metric);
        } else {
            VolatileConfs.getObjectiveFunctionSelected().add(metric);
        }
    }

    private void executePAES() {
        try {
            PAES paes = new PAES();
            Logger.getLogger().putLog("Execution PAES...");
            jTabbedPane1.setSelectedIndex(4);
            paes.execute(cbAlgothm, ckMutation, jsMutation, tfInputArchitecturePath, tfNumberRuns,
                    tfPopulationSize, tfMaxEvaluations, ckCrossover, jsCrossover,
                    tfDescription, tfArchiveSize);
            btRun.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error when try execute PAES Finalizing...." + e.getMessage());
            Logger.getLogger().putLog(
                    String.format("Error when try execute PAES, Finalizing...", Level.FATAL, StartUp.class.getName()));
            btRun.setEnabled(true);
        }
    }
}
