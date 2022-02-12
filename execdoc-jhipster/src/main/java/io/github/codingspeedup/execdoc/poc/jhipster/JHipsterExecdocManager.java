package io.github.codingspeedup.execdoc.poc.jhipster;

import io.github.codingspeedup.execdoc.apps.bpmanager.BlueprintManagerGUI;
import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.toolbox.bpctx.BpCtx;
import io.github.codingspeedup.execdoc.toolbox.files.FileNode;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.JHipsterBlueprint;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.JHipsterGenCfg;

import java.io.File;

public class JHipsterExecdocManager {

    public static Blueprint<?> getBlueprint() {
        return new JHipsterBlueprint(getLocation());
    }

    public static Folder getLocation() {
        return new Folder(FileNode.extend(BpCtx.getInstance().getTempFolder(), "execdoc"));
    }

    public static void main(String[] args) {
        Workplace.initialize();

        JHipsterGenCfg genCfg = new JHipsterGenCfg();
        genCfg.setDestinationFolder(new File(BpCtx.getInstance().getTempFolder(), "project"));
        genCfg.setForce(true);

        new BlueprintManagerGUI<>(JHipsterBlueprint.class, getLocation(), genCfg);
    }

}
