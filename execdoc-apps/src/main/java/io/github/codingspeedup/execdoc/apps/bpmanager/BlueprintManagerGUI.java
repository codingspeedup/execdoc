package io.github.codingspeedup.execdoc.apps.bpmanager;

import io.github.codingspeedup.execdoc.apps.folderdiff.FolderDiffGUI;
import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.blueprint.BlueprintGenCfg;
import io.github.codingspeedup.execdoc.toolbox.bpctx.BpCtx;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.files.TextFile;
import io.github.codingspeedup.execdoc.toolbox.processes.JavaProcess;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.SwingUtility;
import it.unibo.tuprolog.ui.gui.Main;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class BlueprintManagerGUI<T extends Blueprint<?>> extends JFrame {

    public static final String PROP_KEY_LAST_BLUEPRINT_PATH = BlueprintManagerGUI.class.getSimpleName() + ".last.blueprint.path";

    static {
        SwingUtility.setNimbus();
    }

    private final Class<T> bpType;
    private final BlueprintGenCfg genCfg;

    private JPanel mainPanel;
    private JTextField pathField;
    private JButton selectButton;
    private JButton generateBlueprintButton;
    private JButton openButton;
    private JButton normalizeButton;
    private JButton compareButton;
    private JButton generateKbButton;
    private JButton tuPrologButton;
    private JButton generateProjectButton;
    private JLabel statusLabel;

    @SneakyThrows
    public BlueprintManagerGUI(Class<T> bpType, File initialLocation, BlueprintGenCfg genCfg) {
        super("Blueprint Manager GUI (blueprint)");
        setContentPane(mainPanel);

        this.bpType = bpType;
        this.genCfg = genCfg;

        if (initialLocation != null) {
            pathField.setText(initialLocation.getCanonicalPath());
        } else {
            String lastPath = BpCtx.getInstance().getProperty(PROP_KEY_LAST_BLUEPRINT_PATH);
            if (StringUtils.isNotBlank(lastPath)) {
                pathField.setText(lastPath);
            } else {
                pathField.setText(BpCtx.getInstance().getTempFolder().getCanonicalPath());
            }
        }

        selectButton.addActionListener(this::select);

        generateBlueprintButton.addActionListener(this::generateBlueprint);
        openButton.addActionListener(this::open);
        normalizeButton.addActionListener(this::normalize);
        compareButton.addActionListener(this::compare);

        generateKbButton.addActionListener(this::explore);
        tuPrologButton.addActionListener(this::openTuPrologIde);
        generateProjectButton.addActionListener(this::generateProject);

        setPreferredSize(new Dimension(700, 180));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        updateUi();
        statusLabel.setText("Ready");
    }

    private void beginOperation(String status) {
        statusLabel.setText(status);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        selectButton.setEnabled(false);
        generateBlueprintButton.setEnabled(false);
        openButton.setEnabled(false);
        normalizeButton.setEnabled(false);
        compareButton.setEnabled(false);
        generateKbButton.setEnabled(false);
        tuPrologButton.setEnabled(false);
        generateProjectButton.setEnabled(false);
    }

    private void endOperation(String status) {
        updateUi();
        setCursor(Cursor.getDefaultCursor());
        statusLabel.setText(status);
    }

    @SneakyThrows
    private T getBlueprint() {
        File repository = new File(pathField.getText());
        return bpType.getConstructor(File.class).newInstance(repository);
    }

    private void updateUi() {
        boolean initialized = getBlueprint().getMaster().getFile().exists();
        selectButton.setEnabled(true);
        openButton.setEnabled(initialized);
        generateBlueprintButton.setEnabled(true);
        normalizeButton.setEnabled(initialized);
        compareButton.setEnabled(initialized);
        generateKbButton.setEnabled(initialized);
        tuPrologButton.setEnabled(true);
        generateProjectButton.setEnabled(initialized);
        BpCtx.getInstance().setProperty(PROP_KEY_LAST_BLUEPRINT_PATH, pathField.getText());
    }

    @SneakyThrows
    private void select(ActionEvent ev) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open blueprint repository");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        Blueprint<?> bp = getBlueprint();
        if (bp.getFile().exists()) {
            fc.setCurrentDirectory(bp.getFile());
        }
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            pathField.setText(fc.getSelectedFile().getCanonicalPath());
            updateUi();
        }
    }

    @SneakyThrows
    private void generateBlueprint(ActionEvent ev) {
        boolean okGenerate = FileUtils.isEmptyDirectory(getBlueprint().getDirectory());
        if (!okGenerate) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Blueprint repository will be DELETED\n'"
                            + getBlueprint().getDirectory().getCanonicalPath()
                            + "'!\nDo you want to continue?",
                    "Blueprint folder is not empty",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            okGenerate = option == JOptionPane.YES_OPTION;
        }
        if (okGenerate) {
            Blueprint<?> bp = getBlueprint();
            Folder start = new Folder(bp.getFile());
            start.deleteContent();
            bp.save();
        }
        updateUi();
    }

    private void open(ActionEvent ev) {
        OsUtility.open(getBlueprint().getMaster().getFile());
    }

    private void normalize(ActionEvent ev) {
        beginOperation("Normalizing blueprint...");
        Blueprint<?> bp = getBlueprint();
        bp.normalize();
        bp.save();
        endOperation("Done normalizing blueprint");
    }

    private void compare(ActionEvent ev) {
        new Thread(() -> new FolderDiffGUI(getBlueprint().getFile(), null, JFrame.DISPOSE_ON_CLOSE)).start();
    }

    private void explore(ActionEvent ev) {
        Blueprint<?> bp = getBlueprint();
        TextFile kbPl = new TextFile(new File(bp.getFile(), "kb.pl"));
        kbPl.writeStringToContent(bp.compileKb().listTheory());
        OsUtility.open(kbPl);
    }

    @SneakyThrows
    private void openTuPrologIde(ActionEvent ev) {
        new JavaProcess(0).execute(Main.class, null, null);
    }

    private void generateProject(ActionEvent actionEvent) {
        beginOperation("Generating project...");
        try {
            getBlueprint().generate(genCfg);
            endOperation("Done generating project.");
        } catch (Exception ex) {
            endOperation("Error generating project: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        pathField = new JTextField();
        pathField.setEditable(false);
        pathField.setEnabled(true);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 8;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 2, 0, 0);
        mainPanel.add(pathField, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Blueprint:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label1, gbc);
        generateBlueprintButton = new JButton();
        generateBlueprintButton.setText("re/Create");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(generateBlueprintButton, gbc);
        generateKbButton = new JButton();
        generateKbButton.setText("Generate Prolog KB");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(generateKbButton, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        openButton = new JButton();
        openButton.setText("Open");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(openButton, gbc);
        normalizeButton = new JButton();
        normalizeButton.setText("Normalize");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(normalizeButton, gbc);
        selectButton = new JButton();
        selectButton.setText("Browse...");
        gbc = new GridBagConstraints();
        gbc.gridx = 10;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(selectButton, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 9;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer6, gbc);
        tuPrologButton = new JButton();
        tuPrologButton.setText("tuProlog");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(tuPrologButton, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer7, gbc);
        compareButton = new JButton();
        compareButton.setText("Compare...");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(compareButton, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer8, gbc);
        generateProjectButton = new JButton();
        generateProjectButton.setText("Generate Project");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(generateProjectButton, gbc);
        statusLabel = new JLabel();
        statusLabel.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 2, 2, 2);
        mainPanel.add(statusLabel, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
