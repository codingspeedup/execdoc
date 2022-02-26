package io.github.codingspeedup.execdoc.apps.codeminer;

import io.github.codingspeedup.execdoc.apps.codeminer.clipboard.CodeMinerClipboard;
import io.github.codingspeedup.execdoc.reporters.codexray.XRayReporter;
import io.github.codingspeedup.execdoc.apps.AppCtx;
import io.github.codingspeedup.execdoc.toolbox.resources.DefaultResourceFactory;
import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceFilter;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileOrFolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaMethodResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaPackageResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaTypeResource;
import io.github.codingspeedup.execdoc.toolbox.utilities.DateTimeUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.SwingUtility;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.*;

public class CodeMinerGUI extends JFrame {

    public static final String PROP_KEY_LAST_CLIPBOARD_PATH = CodeMinerGUI.class.getSimpleName() + ".last.clipboard.path";

    public static final String FILESYSTEM_RESOURCES = "Filesystem Resources";
    public static final String JAVA_RESOURCES = "Java Resources";
    public static final String JAVA_PACKAGES = "Packages";
    public static final String JAVA_TYPES = "Types";
    public static final String DOUBLE_CLICK_TO_EXPLORE = " (double click to explore)";

    static {
        SwingUtility.setNimbus();
    }

    private final int resourcesTabIndex = 1;
    private CodeMinerClipboard clipboard;
    private ResourceFilter resourceFilter;

    private JPanel mainPanel;
    private JTabbedPane wizardPane;
    private JTextField clipboardFileField;
    private JLabel clipboardDiagnostics;
    private JButton browseButton;
    private JButton addSourceFolderButton;
    private JButton invalidateButton;
    private JTextField patternField;
    private JButton applyButton;
    private JTree resourceTree;
    private JButton addButton;
    private JButton removeButton;
    private JButton removeAllButton;
    private JList<Resource> resourceList;
    private JButton visualizeButton;

    @SneakyThrows
    public CodeMinerGUI(File initialClipboard) {
        super("Code Miner GUI (blueprint)");
        setContentPane(mainPanel);

        if (initialClipboard == null) {
            String lastPath = AppCtx.getInstance().getProperty(PROP_KEY_LAST_CLIPBOARD_PATH);
            if (StringUtils.isNotBlank(lastPath)) {
                File lastFile = new File(lastPath);
                if (lastFile.exists()) {
                    initialClipboard = lastFile;
                }
            }
        }

        wizardPane.addChangeListener(this::onWizardTabChange);

        browseButton.addActionListener(this::onBrowse);

        addSourceFolderButton.addActionListener(this::onAddSourceFolder);

        invalidateButton.addActionListener(this::onInvalidate);

        patternField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if ('\n' == e.getKeyChar() || '\r' == e.getKeyChar()) {
                    onApply(null);
                } else {
                    super.keyTyped(e);
                }
            }
        });

        applyButton.addActionListener(this::onApply);

        ToolTipManager.sharedInstance().registerComponent(resourceTree);
        resourceTree.setCellRenderer(new ResourceTreeRenderer());
        resourceTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(null)));
        resourceTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    explore((DefaultMutableTreeNode) resourceTree.getLastSelectedPathComponent());
                }
            }
        });
        resourceTree.addTreeSelectionListener(this::updateUi);

        addButton.addActionListener(this::onAdd);

        removeButton.addActionListener(this::onRemove);

        removeAllButton.addActionListener(this::onRemoveAll);

        ToolTipManager.sharedInstance().registerComponent(resourceList);
        resourceList.setCellRenderer(new ResourceListRenderer());
        resourceList.setModel(new DefaultListModel<>());
        resourceList.addListSelectionListener(this::updateUi);

        visualizeButton.addActionListener(this::onVisualize);

        setPreferredSize(new Dimension(1024, 768));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        updateUi(null);

        if (initialClipboard == null) {
            onBrowse(null);
        } else {
            openClipboard(initialClipboard);
        }
    }

    private void updateUi(EventObject e) {
        boolean sourcesAvailable = clipboard != null;
        addSourceFolderButton.setEnabled(sourcesAvailable);
        if (sourcesAvailable) {
            ResourceGroup resourceGroup = clipboard.getResourcesSheet().getResourceGroup();
            sourcesAvailable = !resourceGroup.isEmpty();
            if (resourceGroup.isEmpty()) {
                clipboardDiagnostics.setText("No sources configured");
                clipboardDiagnostics.setForeground(Color.RED);
                sourcesAvailable = false;
            } else {
                clipboardDiagnostics.setText(resourceGroup.getChildren().size() + " source(s) configured");
                clipboardDiagnostics.setForeground(Color.DARK_GRAY);
            }
        } else {
            clipboardDiagnostics.setText("No clipboard selected");
            clipboardDiagnostics.setForeground(Color.RED);
        }
        invalidateButton.setEnabled(sourcesAvailable);
        patternField.setEnabled(sourcesAvailable);
        applyButton.setEnabled(sourcesAvailable);
        wizardPane.setEnabledAt(resourcesTabIndex, sourcesAvailable);
        addButton.setEnabled(getTreeSelection() != null);
        removeButton.setEnabled(CollectionUtils.isNotEmpty(resourceList.getSelectedValuesList()));
        removeAllButton.setEnabled(resourceList.getModel().getSize() > 0);
        visualizeButton.setEnabled(resourceList.getModel().getSize() > 0);
    }

    @SneakyThrows
    private void onBrowse(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open clipboard");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
        fc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || "xlsx".equalsIgnoreCase(FilenameUtils.getExtension(f.getName()));
            }

            @Override
            public String getDescription() {
                return "*.xlsx";
            }

        });

        File start = null;
        if (StringUtils.isNotBlank(clipboardFileField.getText())) {
            start = new File(clipboardFileField.getText());
            if (start.exists()) {
                start = start.getParentFile();
            } else {
                start = null;
            }
        }
        if (start == null) {
            start = AppCtx.getInstance().getTempFolder();
        }
        fc.setCurrentDirectory(start);

        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            openClipboard(fc.getSelectedFile());
        }
    }

    @SneakyThrows
    private void openClipboard(File clipboardFile) {
        if (clipboardFile == null) {
            clipboardFile = clipboard.getFile();
        } else {
            if (!clipboardFile.exists() && !clipboardFile.getName().toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
                clipboardFile = new File(clipboardFile.getParentFile(), clipboardFile.getName() + ".xlsx");
            }
            clipboardFileField.setText(clipboardFile.getCanonicalPath());
        }
        clipboard = new CodeMinerClipboard(clipboardFile);
        AppCtx.getInstance().setProperty(PROP_KEY_LAST_CLIPBOARD_PATH, clipboard.getFile().getCanonicalPath());
        updateUi(null);
    }

    @SneakyThrows
    private void onAddSourceFolder(ActionEvent actionEvent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select source folder");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(OsUtility.getUserHomeFolder());
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            File resourceFolder = new File(fc.getSelectedFile().getCanonicalPath());
            FolderResource folderResource = (FolderResource) DefaultResourceFactory.INSTANCE.from(null, resourceFolder);
            clipboard.getResourcesSheet().insert(null, folderResource);
            clipboard.save();
            updateUi(null);
        }
    }

    private void onInvalidate(ActionEvent e) {
        int option = JOptionPane.showConfirmDialog(null,
                "This will remove all cached resources.\nAre you sure you want to continue?",
                "Invalidation clipboard cache",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            clipboard.invalidateCache();
            clipboard.save();
        }
    }

    private void onApply(ActionEvent e) {
        wizardPane.setSelectedIndex(resourcesTabIndex);
    }

    private void onWizardTabChange(ChangeEvent changeEvent) {
        if (wizardPane.getSelectedIndex() == resourcesTabIndex) {
            loadClipboard();
        }
    }

    @SneakyThrows
    private void loadClipboard() {
        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        resourceFilter = ResourceFilter.from(patternField.getText());

        ResourceGroup resources = clipboard.getResourcesSheet().getResourceGroup();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(resources);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        resourceTree.setModel(treeModel);

        for (Resource groupEntry : resources.getChildren()) {
            DefaultMutableTreeNode groupEntryNode = new DefaultMutableTreeNode(groupEntry);
            rootNode.add(groupEntryNode);
            if (groupEntry instanceof FileOrFolderResource) {
                renderFilesystemResources(groupEntryNode);
                renderJavaResources(groupEntryNode);
                resourceTree.expandPath(new TreePath(groupEntryNode.getPath()));
            }
        }

        mainPanel.setCursor(Cursor.getDefaultCursor());
        updateUi(null);
    }

    private Resource getTreeSelection() {
        return getResource(resourceTree.getSelectionPath());
    }

    @Nullable
    private Resource getResource(TreePath path) {
        if (path != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object selectedUserObject = node.getUserObject();
            if (selectedUserObject instanceof Resource) {
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                if (parentNode != null && !(parentNode.getUserObject() instanceof ResourceGroup)) {
                    return (Resource) selectedUserObject;
                }
            }
        }
        return null;
    }

    private void onAdd(ActionEvent e) {
        DefaultListModel<Resource> listModel = (DefaultListModel<Resource>) resourceList.getModel();
        TreePath[] paths = resourceTree.getSelectionPaths();
        if (paths != null) {
            for (TreePath path : paths) {
                Resource resource = getResource(path);
                if (resource != null && !listModel.contains(resource)) {
                    listModel.addElement(resource);
                }
            }
        }
        updateUi(null);
    }

    private void onRemove(ActionEvent event) {
        DefaultListModel<Resource> listModel = (DefaultListModel<Resource>) resourceList.getModel();
        for (Resource resource : resourceList.getSelectedValuesList()) {
            listModel.removeElement(resource);
        }
        updateUi(null);
    }

    private void onRemoveAll(ActionEvent event) {
        resourceList.setModel(new DefaultListModel<>());
        updateUi(null);
    }

    private void onVisualize(ActionEvent e) {
        String xRayName = "CodeMiner-" + DateTimeUtility.toCompactIsoDateTimeString(new Date());
        File xRayFolder = new File(AppCtx.getInstance().getDefaultXrayReportsFolder(), xRayName);
        XRayReporter reporter = new XRayReporter(xRayFolder, clipboard.getResourcesSheet().getResourceGroup());
        reporter.buildReport(Collections.list(((DefaultListModel<Resource>) resourceList.getModel()).elements()));
        reporter.getXRay().save();
        OsUtility.open(reporter.getXRay().getIndexHtml());
    }

    private void renderFilesystemResources(DefaultMutableTreeNode groupEntryNode) {
        Resource groupEntry = (Resource) groupEntryNode.getUserObject();
        Cell groupCell = clipboard.getResourcesSheet().selectByDescriptor(groupEntry.getDescriptor());
        DefaultMutableTreeNode fsNode = new DefaultMutableTreeNode(FILESYSTEM_RESOURCES);
        groupEntryNode.add(fsNode);

        List<? extends Resource> clipboardResources = clipboard.getFilesSheet().getResourcesByGroup(groupCell);
        if (CollectionUtils.isEmpty(clipboardResources)) {
            fsNode.setUserObject(fsNode.getUserObject() + DOUBLE_CLICK_TO_EXPLORE);
        } else {
            clipboardResources.forEach(resource -> {
                if (resourceFilter.accept(resource)) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(resource);
                    fsNode.add(node);
                }
            });
        }
    }

    private void renderJavaResources(DefaultMutableTreeNode groupEntryNode) {
        Resource groupEntry = (Resource) groupEntryNode.getUserObject();
        Cell groupCell = clipboard.getResourcesSheet().selectByDescriptor(groupEntry.getDescriptor());
        DefaultMutableTreeNode javaNode = new DefaultMutableTreeNode(JAVA_RESOURCES);
        groupEntryNode.add(javaNode);

        List<Cell> typeCells = clipboard.getJavaTypesSheet().selectByGroup(groupCell);
        if (CollectionUtils.isEmpty(typeCells)) {
            javaNode.setUserObject(javaNode.getUserObject() + DOUBLE_CLICK_TO_EXPLORE);
        } else {
            DefaultMutableTreeNode packagesNode = new DefaultMutableTreeNode(JAVA_PACKAGES);
            javaNode.add(packagesNode);
            List<JavaPackageResource> packageResources = clipboard.getJavaPackagesSheet().getResourcesByGroup(groupCell);
            packageResources.sort(Comparator.comparing(Resource::getName));
            if (CollectionUtils.isNotEmpty(packageResources)) {
                packageResources.forEach(packageResource -> {
                    if (resourceFilter.accept(packageResource)) {
                        DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(packageResource);
                        packagesNode.add(packageNode);
                    }
                });
            }

            DefaultMutableTreeNode typesNode = new DefaultMutableTreeNode(JAVA_TYPES);
            javaNode.add(typesNode);
            List<DefaultMutableTreeNode> typeNodes = new ArrayList<>();
            typeCells.forEach(typeCell -> {
                JavaTypeResource typeResource = clipboard.getJavaTypesSheet().getResource(typeCell);
                DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(typeResource);
                List<JavaMethodResource> methodResources = clipboard.getJavaMethodsSheet().getResourcesByParent(typeCell);
                methodResources.sort(Comparator.comparing(Resource::getName));
                for (JavaMethodResource methodResource : methodResources) {
                    if (methodResource.isPublic() && resourceFilter.accept(methodResource)) {
                        DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(methodResource);
                        typeNode.add(methodNode);
                    }
                }
                if (typeNode.getChildCount() > 0 || resourceFilter.accept(typeResource)) {
                    typeNodes.add(typeNode);
                }
            });
            typeNodes.sort(Comparator.comparing(n -> ((Resource) (n.getUserObject())).getName()));
            typeNodes.forEach(typesNode::add);

            resourceTree.expandPath(new TreePath(javaNode.getPath()));
        }
    }

    private void explore(DefaultMutableTreeNode node) {
        if (node == null || node.getChildCount() > 0) {
            return;
        }
        Object userObject = node.getUserObject();
        if (userObject instanceof String) {
            String nodeText = (String) userObject;
            if (nodeText.contains(DOUBLE_CLICK_TO_EXPLORE)) {
                if (nodeText.contains(FILESYSTEM_RESOURCES)) {
                    Resource resource = (Resource) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
                    new FileResourcesManager(clipboard).explore((FileOrFolderResource) resource);
                } else if (nodeText.contains(JAVA_RESOURCES)) {
                    Resource resource = (Resource) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
                    new JavaResourcesManager(clipboard).explore((FileOrFolderResource) resource);
                }
                if (clipboard.isDirty()) {
                    clipboard.save();
                    loadClipboard();
                }
            }
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
        mainPanel.setLayout(new BorderLayout(0, 0));
        wizardPane = new JTabbedPane();
        wizardPane.setEnabled(true);
        mainPanel.add(wizardPane, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        wizardPane.addTab("Sources & Filtering", panel1);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(4, 6, 4, 4), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Clipboard file:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(label1, gbc);
        clipboardFileField = new JTextField();
        clipboardFileField.setEditable(false);
        clipboardFileField.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(clipboardFileField, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        invalidateButton = new JButton();
        invalidateButton.setText("Invalidate cache!");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        panel2.add(invalidateButton, gbc);
        addSourceFolderButton = new JButton();
        addSourceFolderButton.setText("Add source folder...");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(addSourceFolderButton, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Filter resources by:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(label2, gbc);
        patternField = new JTextField();
        patternField.setToolTipText("Use /JavaScript/ notation to specify a regular expression");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(patternField, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        clipboardDiagnostics = new JLabel();
        clipboardDiagnostics.setHorizontalAlignment(0);
        clipboardDiagnostics.setHorizontalTextPosition(0);
        clipboardDiagnostics.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 4, 2, 4);
        panel1.add(clipboardDiagnostics, gbc);
        applyButton = new JButton();
        applyButton.setText("Apply filter and go to resources selection tab   >>");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        panel1.add(applyButton, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel1.add(spacer4, gbc);
        browseButton = new JButton();
        browseButton.setText("Browse...");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(browseButton, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel3.setEnabled(true);
        wizardPane.addTab("Resources & Visualization", panel3);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(4, 6, 4, 4), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label3 = new JLabel();
        label3.setText("Resources:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel3.add(label3, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel4, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Selection:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel4.add(label4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 2.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer5, gbc);
        addButton = new JButton();
        addButton.setText("Add >>");
        addButton.setMnemonic('A');
        addButton.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(addButton, gbc);
        removeButton = new JButton();
        removeButton.setText("Remove");
        removeButton.setMnemonic('R');
        removeButton.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(removeButton, gbc);
        removeAllButton = new JButton();
        removeAllButton.setText("Remove All");
        removeAllButton.setMnemonic('M');
        removeAllButton.setDisplayedMnemonicIndex(2);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(removeAllButton, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 3.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer7, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(scrollPane1, gbc);
        resourceTree = new JTree();
        Font resourceTreeFont = this.$$$getFont$$$("Courier", Font.PLAIN, 14, resourceTree.getFont());
        if (resourceTreeFont != null) resourceTree.setFont(resourceTreeFont);
        scrollPane1.setViewportView(resourceTree);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel5, gbc);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(scrollPane2, gbc);
        resourceList = new JList();
        scrollPane2.setViewportView(resourceList);
        visualizeButton = new JButton();
        visualizeButton.setText("Visualize...");
        visualizeButton.setMnemonic('V');
        visualizeButton.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel5.add(visualizeButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
