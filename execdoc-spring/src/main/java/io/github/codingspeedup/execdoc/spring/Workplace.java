package io.github.codingspeedup.execdoc.spring;

import io.github.codingspeedup.execdoc.apps.AppCtx;
import io.github.codingspeedup.execdoc.apps.AppCtxProvider;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;

import java.io.File;

public class Workplace implements AppCtxProvider {

    private static final String MARKER = "execdoc-spring";

    public static void initialize() {
        AppCtx.initialize(new Workplace());
    }

    @Override
    public Folder getTempFolder() {
        return Folder.of(new File(OsUtility.getUserHomeFolder(), "temp/" + MARKER));
    }

    @Override
    public String getContextMarker() {
        return MARKER;
    }
}
