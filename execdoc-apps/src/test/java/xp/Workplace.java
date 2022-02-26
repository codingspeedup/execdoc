package xp;

import io.github.codingspeedup.execdoc.apps.AppCtx;
import io.github.codingspeedup.execdoc.apps.AppCtxProvider;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;

import java.io.File;

public class Workplace implements AppCtxProvider {

    private static final String MARKER = "blueprint-xp";

    public static void initialize() {
        AppCtx.registerEnvironmentProvider(new Workplace());
    }

    @Override
    public Folder getTempFolder() {
        return Folder.of(new File(OsUtility.getUserHomeFolder(), "temp/" + MARKER));
    }

    @Override
    public String getDotPath() {
        return "/usr/local/bin/dot";
    }

    @Override
    public String getContextMarker() {
        return MARKER;
    }

}