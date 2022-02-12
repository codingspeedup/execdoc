package xp;

import io.github.codingspeedup.execdoc.apps.folderdiff.FolderDiffGUI;

public class FolderDiffGuiLauncher {

    public static void main(String[] args) {
        Workplace.initialize();
        new FolderDiffGUI(null, null);
    }

}
