package io.github.codingspeedup.execdoc.apps.codeminer;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;

public class ResourceRendering {

    public static final int ICON_SIZE = 16;

    public static final Color ROOT_ICON_COLOR = new Color(0, 0, 0);
    public static final Color GROUP_ICON_COLOR = new Color(96, 96, 96);

    public static final Color FILE_ICON_COLOR = new Color(128, 128, 128);
    public static final Color FOLDER_ICON_COLOR = new Color(255, 175, 175);

    public static final Color ANNOTATION_ICON_COLOR = new Color(227, 102, 74);
    public static final Color ABSTRACT_ICON_COLOR = new Color(168, 220, 223);
    public static final Color CLASS_ICON_COLOR = new Color(173, 209, 178);
    public static final Color ENUM_ICON_COLOR = new Color(237, 147, 126);
    public static final Color INTERFACE_ICON_COLOR = new Color(180, 167, 228);
    public static final Color METHOD_ICON_COLOR = new Color(255, 128, 192);
    public static final Color PACKAGE_ICON_COLOR = new Color(200, 172, 128);

    public static final Icon ROOT_ICON = IconFontSwing.buildIcon(FontAwesome.SNOWFLAKE_O, ICON_SIZE, ROOT_ICON_COLOR);
    public static final Icon GROUP_FOLDER_ICON = IconFontSwing.buildIcon(FontAwesome.FOLDER, ICON_SIZE, GROUP_ICON_COLOR);
    public static final Icon GROUP_FILE_ICON = IconFontSwing.buildIcon(FontAwesome.FILE, ICON_SIZE, GROUP_ICON_COLOR);

    public static final Icon FS_FOLDER_ICON = IconFontSwing.buildIcon(FontAwesome.FOLDER, ICON_SIZE, FOLDER_ICON_COLOR);
    public static final Icon FS_FILE_ICON = IconFontSwing.buildIcon(FontAwesome.FILE_O, ICON_SIZE, FILE_ICON_COLOR);

    public static final Icon JAVA_PACKAGE_ICON = IconFontSwing.buildIcon(FontAwesome.ARCHIVE, ICON_SIZE, PACKAGE_ICON_COLOR);
    public static final Icon JAVA_ANNOTATION_ICON = IconFontSwing.buildIcon(FontAwesome.TAG, ICON_SIZE, ANNOTATION_ICON_COLOR);
    public static final Icon JAVA_CLASS_ICON = IconFontSwing.buildIcon(FontAwesome.CIRCLE, ICON_SIZE, CLASS_ICON_COLOR);
    public static final Icon JAVA_CLASS_ABSTRACT_ICON = IconFontSwing.buildIcon(FontAwesome.CIRCLE_O, ICON_SIZE, ABSTRACT_ICON_COLOR);
    public static final Icon JAVA_ENUM_ICON = IconFontSwing.buildIcon(FontAwesome.BARS, ICON_SIZE, ENUM_ICON_COLOR);
    public static final Icon JAVA_INTERFACE_ICON = IconFontSwing.buildIcon(FontAwesome.SQUARE, ICON_SIZE, INTERFACE_ICON_COLOR);
    public static final Icon JAVA_METHOD_ICON = IconFontSwing.buildIcon(FontAwesome.ANGLE_DOUBLE_RIGHT, ICON_SIZE, METHOD_ICON_COLOR);

}
