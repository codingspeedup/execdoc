package io.github.codingspeedup.execdoc.apps.codeminer;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileResource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import io.github.codingspeedup.execdoc.toolbox.resources.java.*;

import javax.swing.*;
import java.awt.*;

public class ResourceListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Resource resource = (Resource) value;
        setToolTipText(resource.getDescription());
        if (resource instanceof FolderResource) {
            setIcon(ResourceRendering.FS_FOLDER_ICON);
        } else if (resource instanceof FileResource) {
            setIcon(ResourceRendering.FS_FILE_ICON);
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
        return this;
    }

}
