package io.github.codingspeedup.execdoc.apps.codeminer;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class ResourceTreeRenderer extends DefaultTreeCellRenderer {

    public ResourceTreeRenderer() {
        setLeafIcon(null);
        setClosedIcon(null);
        setOpenIcon(null);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.getUserObject() instanceof Resource) {
            Resource resource = (Resource) node.getUserObject();
            setToolTipText(resource.getDescription());

            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            if (resource instanceof ResourceGroup) {
                setIcon(ResourceRendering.ROOT_ICON);
            } else if (resource instanceof FolderResource) {
                if (parentNode.getUserObject() instanceof ResourceGroup) {
                    setIcon(ResourceRendering.GROUP_FOLDER_ICON);
                } else {
                    setIcon(ResourceRendering.FS_FOLDER_ICON);
                }
            } else if (resource instanceof FileResource) {
                if (parentNode.getUserObject() instanceof ResourceGroup) {
                    setIcon(ResourceRendering.GROUP_FILE_ICON);
                } else {
                    setIcon(ResourceRendering.FS_FILE_ICON);
                }
            } else if (resource instanceof JavaPackageResource) {
                setIcon(ResourceRendering.JAVA_PACKAGE_ICON);
            } else if (resource instanceof JavaMethodResource) {
                setIcon(ResourceRendering.JAVA_METHOD_ICON);
            } else if (resource instanceof JavaClassResource) {
                if (((JavaClassResource) resource).isAbstract()) {
                    setIcon(ResourceRendering.JAVA_CLASS_ABSTRACT_ICON);
                } else {
                    setIcon(ResourceRendering.JAVA_CLASS_ICON);
                }
            } else if (resource instanceof JavaInterfaceResource) {
                setIcon(ResourceRendering.JAVA_INTERFACE_ICON);
            } else if (resource instanceof JavaEnumResource) {
                setIcon(ResourceRendering.JAVA_ENUM_ICON);
            } else if (resource instanceof JavaAnnotationResource) {
                setIcon(ResourceRendering.JAVA_ANNOTATION_ICON);
            }
        }
        return this;
    }

}
