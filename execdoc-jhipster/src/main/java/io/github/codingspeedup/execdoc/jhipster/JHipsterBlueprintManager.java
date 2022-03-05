package io.github.codingspeedup.execdoc.jhipster;

import io.github.codingspeedup.execdoc.apps.AppCtx;
import io.github.codingspeedup.execdoc.apps.bpmanager.BlueprintManagerGUI;
import io.github.codingspeedup.execdoc.jhipster.blueprint.JHipsterBlueprint;
import io.github.codingspeedup.execdoc.jhipster.generators.JHipsterGenConfig;
import io.github.codingspeedup.execdoc.jhipster.generators.JHipsterGenerator;
import io.github.codingspeedup.execdoc.toolbox.files.FileNode;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;

import java.io.File;

public class JHipsterBlueprintManager {

    public static Folder getLocation() {
        return new Folder(FileNode.extend(AppCtx.getInstance().getTempFolder(), "execdoc"));
    }

    public static void main(String[] args) {
        Workplace.initialize();

        JHipsterGenConfig genCfg = new JHipsterGenConfig();
        genCfg.setDestinationFolder(new File(AppCtx.getInstance().getTempFolder(), "project"));
        genCfg.setForce(true);

        new BlueprintManagerGUI<>(JHipsterBlueprint.class, getLocation())
                .addListener((blueprint) -> new JHipsterGenerator(blueprint, genCfg).generateApp());
    }

}
