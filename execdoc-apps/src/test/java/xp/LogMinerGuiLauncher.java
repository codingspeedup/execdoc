package xp;

import io.github.codingspeedup.execdoc.toolbox.resources.ResourceGroup;
import io.github.codingspeedup.execdoc.apps.logminer.LogMinerGUI;

import java.io.File;

public class LogMinerGuiLauncher {

    public static void main(String[] args) {
        Workplace.initialize();
        ResourceGroup group = ResourceGroup.builder()
                .name("Default")
                .res(new File(System.getenv("PWD")))
                .build();
        new LogMinerGUI(group);
    }

}
