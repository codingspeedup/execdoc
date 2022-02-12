package xp;

import io.github.codingspeedup.execdoc.apps.codeminer.CodeMinerGUI;

public class CodeMinerGuiLauncher {

    public static void main(String[] args) {
        Workplace.initialize();
        new CodeMinerGUI(null);
    }

}
