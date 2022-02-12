package io.github.codingspeedup.execdoc.poc.jhipster;

import io.github.codingspeedup.execdoc.toolbox.bpctx.BpCtx;
import io.github.codingspeedup.execdoc.toolbox.bpctx.BpCtxProvider;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;

import java.io.File;

public class Workplace implements BpCtxProvider {

    private static final String MARKER = "jhipster-poc";

    public static void initialize() {
        BpCtx.registerEnvironmentProvider(new Workplace());
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