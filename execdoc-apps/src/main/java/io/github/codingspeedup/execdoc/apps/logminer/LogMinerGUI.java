package io.github.codingspeedup.execdoc.apps.logminer;

import io.github.codingspeedup.execdoc.miners.logs.LogFileMatcher;
import io.github.codingspeedup.execdoc.miners.logs.LogMiner;
import io.github.codingspeedup.execdoc.miners.logs.LogMinerFinding;
import io.github.codingspeedup.execdoc.miners.logs.LogMinerListener;
import io.github.codingspeedup.execdoc.miners.logs.defaults.DefaultLogMinerListener;
import io.github.codingspeedup.execdoc.toolbox.bpctx.BpCtx;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.SwingUtility;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;

public class LogMinerGUI extends JFrame implements LogMinerListener {

    static {
        SwingUtility.setNimbus();
    }

    private JPanel mainPanel;
    private JComboBox<ResourceGroup> groupBox;
    private JComboBox<String> fromBox;
    private JComboBox<String> toBox;
    private JTextField patternField;
    private JCheckBox caseSensitiveCheckBox;
    private JButton searchButton;
    private JList<LogMinerFinding> findingsList;
    private JLabel statusLabel;

    private JPopupMenu findingMenu;

    private LogMiner miner;

    public LogMinerGUI(ResourceGroup... groups) {
        super("Log Miner GUI (blueprint)");
        findingMenu = buildFindingsMenu();
        setContentPane(mainPanel);

        Arrays.stream(groups).forEach(groupBox::addItem);
        fillHours(fromBox);
        fromBox.setSelectedIndex(1);
        fillHours(toBox);
        patternField.setText("");
        statusLabel.setText("Ready");
        searchButton.addActionListener(this::onSearchAction);

        findingsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeOpenContextMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeOpenContextMenu(e);
            }

            public void maybeOpenContextMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    findingsList.setSelectedIndex(findingsList.locationToIndex(e.getPoint()));
                    findingMenu.show(findingsList, e.getX(), e.getY());
                }
            }
        });

        setPreferredSize(new Dimension(800, 400));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private static Long parseTimestamp(String timeHint) {
        if (timeHint == null || !timeHint.contains(":")) {
            return null;
        }
        String hourHint = timeHint.split(":")[0];
        if (hourHint.startsWith("0")) {
            hourHint = hourHint.substring(1);
        }
        int hour = Integer.parseInt(hourHint);
        Calendar dt = Calendar.getInstance();
        dt.set(Calendar.MILLISECOND, 0);
        dt.set(Calendar.SECOND, 0);
        dt.set(Calendar.MINUTE, 0);
        if (dt.get(Calendar.HOUR_OF_DAY) < hour) {
            dt.add(Calendar.HOUR_OF_DAY, -24);
        }
        dt.set(Calendar.HOUR_OF_DAY, hour);
        return dt.getTimeInMillis();
    }

    private JPopupMenu buildFindingsMenu() {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem item = new JMenuItem("Copy path to clipboard");
        item.addActionListener(e -> {
            try {
                LogMinerFinding finding = findingsList.getSelectedValue();
                StringSelection selection = new StringSelection(finding.getFile().getCanonicalPath());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Open file");
        item.addActionListener(e -> {
            LogMinerFinding finding = findingsList.getSelectedValue();
            OsUtility.open(finding.getFile());
        });
        menu.add(item);

        item = new JMenuItem("Open containing folder");
        item.addActionListener(e -> {
            LogMinerFinding finding = findingsList.getSelectedValue();
            OsUtility.open(finding.getFile().getParentFile());
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Cache and open file");
        item.addActionListener(e -> {
            LogMinerFinding finding = findingsList.getSelectedValue();
            File logFile = finding.getFile();
            File cachedFile = new File(BpCtx.getInstance().getLocalCopyLogFolder(), logFile.getName());
            try {
                FileUtils.copyFile(logFile, cachedFile);
                OsUtility.open(cachedFile);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        });
        menu.add(item);

        item = new JMenuItem("Cache and open containing folder");
        item.addActionListener(e -> {
            LogMinerFinding finding = findingsList.getSelectedValue();
            File logFile = finding.getFile();
            File cachedFile = new File(BpCtx.getInstance().getLocalCopyLogFolder(), logFile.getName());
            try {
                FileUtils.copyFile(logFile, cachedFile);
                OsUtility.open(cachedFile.getParentFile());
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Open log cache folder");
        item.addActionListener(e -> OsUtility.open(BpCtx.getInstance().getLocalCopyLogFolder()));
        menu.add(item);

        return menu;
    }

    private void fillHours(JComboBox<String> box) {
        box.addItem("Any");
        int topHour = LocalDateTime.now().getHour();
        for (int i = 0; i < 16; ++i) {
            box.addItem(String.format("%02d:00", (24 + topHour - i) % 24));
        }
    }

    private synchronized void onSearchAction(ActionEvent e) {
        if (miner == null) {
            String pattern = patternField.getText().trim();
            if (pattern.isEmpty()) {
                return;
            }
            LogFileMatcher matcher = new LogFileMatcher(pattern, caseSensitiveCheckBox.isSelected());

            FileFilter filter = new LastModifiedFileFilter(
                    parseTimestamp((String) fromBox.getSelectedItem()),
                    parseTimestamp((String) toBox.getSelectedItem())
            );

            miner = new LogMiner((ResourceGroup) groupBox.getSelectedItem(), new DefaultLogMinerListener(), this);

            new Thread(() -> miner.browse(filter, matcher)).start();
        } else {
            miner.stop();
        }
    }

    @Override
    public synchronized void onStart() {
        groupBox.setEnabled(false);
        fromBox.setEnabled(false);
        toBox.setEnabled(false);
        patternField.setEnabled(false);
        caseSensitiveCheckBox.setEnabled(false);
        findingsList.setModel(new DefaultListModel<>());
        searchButton.setText("Stop");
        statusLabel.setText("Searching...");
    }

    @Override
    public synchronized void onLocationDiscovered(File file) {
        statusLabel.setText("Discovered: " + file.getAbsolutePath());
    }

    @Override
    public synchronized void onFileAccepted(File file) {
        statusLabel.setText("Searching: " + file.getName());
    }

    @Override
    public synchronized void onMatchFound(LogMinerFinding finding) {
        DefaultListModel<LogMinerFinding> listModel = (DefaultListModel<LogMinerFinding>) findingsList.getModel();
        listModel.addElement(finding);
    }

    @Override
    public synchronized void onEnd() {
        groupBox.setEnabled(true);
        fromBox.setEnabled(true);
        toBox.setEnabled(true);
        patternField.setEnabled(true);
        caseSensitiveCheckBox.setEnabled(true);
        searchButton.setText("Search");
        statusLabel.setText("Done - " + findingsList.getModel().getSize() + " file(s) found");
        miner = null;
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
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(1, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        mainPanel.add(tabbedPane1, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Search", panel1);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Location group:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(label1, gbc);
        groupBox = new JComboBox();
        groupBox.setPreferredSize(new Dimension(150, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(groupBox, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Search pattern:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(label2, gbc);
        patternField = new JTextField();
        patternField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(patternField, gbc);
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(11);
        label3.setMinimumSize(new Dimension(50, 16));
        label3.setPreferredSize(new Dimension(50, 16));
        label3.setText("From:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(label3, gbc);
        fromBox = new JComboBox();
        fromBox.setPreferredSize(new Dimension(150, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(fromBox, gbc);
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(11);
        label4.setMinimumSize(new Dimension(50, 16));
        label4.setPreferredSize(new Dimension(50, 16));
        label4.setText("To:");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(label4, gbc);
        toBox = new JComboBox();
        toBox.setPreferredSize(new Dimension(150, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(toBox, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        caseSensitiveCheckBox = new JCheckBox();
        caseSensitiveCheckBox.setText("Case Sensitive");
        caseSensitiveCheckBox.setMnemonic('C');
        caseSensitiveCheckBox.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(caseSensitiveCheckBox, gbc);
        searchButton = new JButton();
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.setText("Search");
        searchButton.setMnemonic('S');
        searchButton.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel1.add(searchButton, gbc);
        statusLabel = new JLabel();
        statusLabel.setText("Status");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 0);
        panel1.add(statusLabel, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);
        findingsList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        findingsList.setModel(defaultListModel1);
        scrollPane1.setViewportView(findingsList);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Configuration", panel2);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
