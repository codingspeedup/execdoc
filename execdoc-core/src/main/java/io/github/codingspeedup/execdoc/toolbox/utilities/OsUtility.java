package io.github.codingspeedup.execdoc.toolbox.utilities;

import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import lombok.SneakyThrows;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.net.URI;

public class OsUtility {

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static String getOsVersion() {
        return System.getProperty("os.version");
    }

    public static Folder getTempFolder() {
        return new Folder(new File(System.getProperty("java.io.tmpdir")));
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static Folder getUserHomeFolder() {
        return new Folder(new File(System.getProperty("user.home")));
    }

    @SneakyThrows
    public static void open(File path) {
        Desktop.getDesktop().open(path);
    }

    @SneakyThrows
    public static void browse(String uri) {
        Desktop.getDesktop().browse(new URI(uri));
    }

    public static void copyToSystemClipboard(String text) {
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(text), null);
    }

}
