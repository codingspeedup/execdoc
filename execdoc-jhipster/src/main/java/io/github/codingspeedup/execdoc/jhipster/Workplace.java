package io.github.codingspeedup.execdoc.jhipster;

import io.github.codingspeedup.execdoc.apps.AppCtx;
import io.github.codingspeedup.execdoc.apps.AppCtxProvider;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;

import java.io.File;

public class Workplace implements AppCtxProvider {

    private static final String MARKER = "jhipster-poc";

    public static void initialize() {
        AppCtx.registerEnvironmentProvider(new Workplace());
    }

    @Override
    public Folder getTempFolder() {
        return Folder.of(new File(OsUtility.getUserHomeFolder(), "temp/" + MARKER));
    }

    @Override
    public String getDotPath() {
        return "";
    }

    @Override
    public String getContextMarker() {
        return MARKER;
    }

}