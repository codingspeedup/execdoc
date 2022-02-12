package io.github.codingspeedup.execdoc.apps.folderdiff;

import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffContainer;
import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffMiner;
import io.github.codingspeedup.execdoc.reporters.folderdiff.FolderDiffReport;
import io.github.codingspeedup.execdoc.reporters.folderdiff.FolderDiffReportPreferences;
import io.github.codingspeedup.execdoc.reporters.folderdiff.FolderDiffReporter;
import io.github.codingspeedup.execdoc.toolbox.bpctx.BpCtx;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.SwingUtility;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

public class FolderDiffGUI extends JFrame {

    public static final String PROP_KEY_LAST_LEFT_PATH = FolderDiffGUI.class.getSimpleName() + ".last.left.path";
    public static final String PROP_KEY_LAST_RIGHT_PATH = FolderDiffGUI.class.getSimpleName() + ".last.right.path";

    static {
        SwingUtility.setNimbus();
    }

    private JPanel mainPanel;
    private JTextField leftField;
    private JTextField rightField;
    private JTextField destinationField;
    private JCheckBox embedResourcesCheckBox;
    private JCheckBox openReportAtEndCheckBox;
    private JButton browseLeftButton;
    private JButton browseRightButton;
    private JButton openReportFolderButton;
    private JButton openReportFileButton;
    private JButton generateDiffReportButton;

    private boolean comparing = false;

    public FolderDiffGUI(File left, File right) {
        this(left, right, null);
    }

    @SneakyThrows
    public FolderDiffGUI(File left, File right, Integer defaultCloseOperation) {
        super("Folder Diff GUI (blueprint)");

        if (left == null) {
            String lastPath = BpCtx.getInstance().getProperty(PROP_KEY_LAST_LEFT_PATH);
            if (StringUtils.isNotBlank(lastPath)) {
                left = new File(lastPath);
            } else {
                left = BpCtx.getInstance().getTempFolder();
            }
        }
        if (right == null) {
            String lastPath = BpCtx.getInstance().getProperty(PROP_KEY_LAST_RIGHT_PATH);
            if (StringUtils.isNotBlank(lastPath)) {
                right = new File(lastPath);
            } else {
                right = BpCtx.getInstance().getTempFolder();
            }
        }

        setContentPane(mainPanel);

        DocumentListener docListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateUi();
            }

            public void removeUpdate(DocumentEvent e) {
                updateUi();
            }

            public void insertUpdate(DocumentEvent e) {
                updateUi();
            }
        };

        openReportFolderButton.setIcon(IconFontSwing.buildIcon(FontAwesome.FOLDER_OPEN, openReportFolderButton.getFont().getSize(), new Color(0, 0, 0)));
        openReportFileButton.setIcon(IconFontSwing.buildIcon(FontAwesome.FILE, openReportFileButton.getFont().getSize(), new Color(0, 0, 0)));

        leftField.setText(left.getCanonicalPath());
        rightField.setText(right.getCanonicalPath());

        destinationField.setText(BpCtx.getInstance().getDefaultDiffReportsFolder().getCanonicalPath());

        leftField.getDocument().addDocumentListener(docListener);
        browseLeftButton.addActionListener(this::openLeftFolder);

        rightField.getDocument().addDocumentListener(docListener);
        browseRightButton.addActionListener(this::openRightFolder);

        openReportFolderButton.addActionListener(this::openReportFolder);
        openReportFileButton.addActionListener(this::openReportFile);

        generateDiffReportButton.addActionListener(this::generateReport);

        setPreferredSize(new Dimension(800, 200));
        pack();
        setDefaultCloseOperation(Objects.requireNonNullElse(defaultCloseOperation, JFrame.EXIT_ON_CLOSE));
        setLocationRelativeTo(null);
        setVisible(true);
        updateUi();
    }

    private void updateUi() {
        leftField.setEnabled(!comparing);
        browseLeftButton.setEnabled(!comparing);

        rightField.setEnabled(!comparing);
        browseRightButton.setEnabled(!comparing);

        embedResourcesCheckBox.setEnabled(!comparing);
        openReportAtEndCheckBox.setEnabled(!comparing);

        openReportFileButton.setEnabled(false);
        File destination = new File(destinationField.getText());
        if (!comparing && destination.exists() && destination.isFile()) {
            openReportFileButton.setEnabled(true);
        }

        generateDiffReportButton.setEnabled(false);
        if (!comparing && StringUtils.isNoneEmpty(leftField.getText(), rightField.getText())) {
            File left = new File(leftField.getText());
            File right = new File(rightField.getText());
            if (left.exists() && right.exists() && left.isDirectory() && right.isDirectory()) {
                generateDiffReportButton.setEnabled(true);
            }
        }

        BpCtx.getInstance().setProperty(PROP_KEY_LAST_LEFT_PATH, leftField.getText());
        BpCtx.getInstance().setProperty(PROP_KEY_LAST_RIGHT_PATH, rightField.getText());
    }

    @SneakyThrows
    private void openLeftFolder(ActionEvent ev) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open left folder");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File start = new File(leftField.getText());
        if (start.exists()) {
            fc.setCurrentDirectory(start);
        }
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            File file = fc.getSelectedFile();
            leftField.setText(file.getCanonicalPath());
        }
        updateUi();
    }

    @SneakyThrows
    private void openRightFolder(ActionEvent ev) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open right folder");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File start = new File(rightField.getText());
        if (start.exists()) {
            fc.setCurrentDirectory(start);
        }
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            File file = fc.getSelectedFile();
            rightField.setText(file.getCanonicalPath());
        }
        updateUi();
    }

    private void openReportFolder(ActionEvent ev) {
        File destination = new File(destinationField.getText());
        if (!destination.exists() || destination.isFile()) {
            destination = destination.getParentFile();
        }
        OsUtility.open(destination);
    }

    private void openReportFile(ActionEvent ev) {
        File destination = new File(destinationField.getText());
        OsUtility.open(destination);
    }

    @SneakyThrows
    private void generateReport(ActionEvent ev) {
        LocalDateTime now = LocalDateTime.now();
        String reportFileName = String.format("diff_%03d_%02d.%02d.%02d.docx", now.getDayOfYear(), now.getHour(), now.getMinute(), now.getSecond());
        File destination = new File(destinationField.getText());
        if (!destination.exists() || destination.isFile()) {
            destination = destination.getParentFile();
        }
        final File reportFile = new File(destination, reportFileName);
        destinationField.setText(reportFile.getCanonicalPath());
        comparing = true;
        updateUi();

        new Thread(() -> {
            File left = new File(leftField.getText());
            File right = new File(rightField.getText());
            FolderDiffMiner miner = new FolderDiffMiner();
            FolderDiffContainer diffs = miner.compare(left, right);

            FolderDiffReportPreferences reportPreferences = new FolderDiffReportPreferences();
            reportPreferences.setEmbedResources(embedResourcesCheckBox.isSelected());
            FolderDiffReporter fdReporter = new FolderDiffReporter(reportPreferences);
            FolderDiffReport report = fdReporter.buildReport(diffs);
            report.saveAs(reportFile);

            if (openReportAtEndCheckBox.isSelected()) {
                OsUtility.open(report.getFile());
            }

            comparing = false;
            updateUi();
        }).start();
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
        final JLabel label1 = new JLabel();
        label1.setText("Left:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Right:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(label2, gbc);
        leftField = new JTextField();
        leftField.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 0, 1, 0);
        mainPanel.add(leftField, gbc);
        rightField = new JTextField();
        rightField.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 0, 1, 0);
        mainPanel.add(rightField, gbc);
        browseLeftButton = new JButton();
        browseLeftButton.setText("Browse...");
        browseLeftButton.setMnemonic('B');
        browseLeftButton.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(browseLeftButton, gbc);
        browseRightButton = new JButton();
        browseRightButton.setText("Browse...");
        browseRightButton.setMnemonic('R');
        browseRightButton.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(browseRightButton, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Destination:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(label3, gbc);
        destinationField = new JTextField();
        destinationField.setEditable(false);
        destinationField.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(1, 0, 1, 0);
        mainPanel.add(destinationField, gbc);
        openReportFolderButton = new JButton();
        openReportFolderButton.setText("Open");
        openReportFolderButton.setMnemonic('O');
        openReportFolderButton.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(openReportFolderButton, gbc);
        openReportAtEndCheckBox = new JCheckBox();
        openReportAtEndCheckBox.setSelected(true);
        openReportAtEndCheckBox.setText("Open report at end");
        openReportAtEndCheckBox.setMnemonic('D');
        openReportAtEndCheckBox.setDisplayedMnemonicIndex(17);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 0, 0);
        mainPanel.add(openReportAtEndCheckBox, gbc);
        embedResourcesCheckBox = new JCheckBox();
        embedResourcesCheckBox.setSelected(true);
        embedResourcesCheckBox.setText("Embedd resources");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 0, 0, 0);
        mainPanel.add(embedResourcesCheckBox, gbc);
        generateDiffReportButton = new JButton();
        generateDiffReportButton.setText("Generate diff report");
        generateDiffReportButton.setMnemonic('D');
        generateDiffReportButton.setDisplayedMnemonicIndex(9);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(4, 0, 0, 0);
        mainPanel.add(generateDiffReportButton, gbc);
        openReportFileButton = new JButton();
        openReportFileButton.setText("Open");
        openReportFileButton.setMnemonic('N');
        openReportFileButton.setDisplayedMnemonicIndex(3);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(openReportFileButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}