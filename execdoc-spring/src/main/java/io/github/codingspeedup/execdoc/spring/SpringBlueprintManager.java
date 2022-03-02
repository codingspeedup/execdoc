package io.github.codingspeedup.execdoc.spring;

import io.github.codingspeedup.execdoc.apps.AppCtx;
import io.github.codingspeedup.execdoc.apps.bpmanager.BlueprintManagerGUI;
import io.github.codingspeedup.execdoc.generators.utilities.GenUtility;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;
import io.github.codingspeedup.execdoc.spring.generators.SpringBootGenCfg;
import io.github.codingspeedup.execdoc.spring.generators.SpringBootGenerator;
import io.github.codingspeedup.execdoc.toolbox.documents.TextFileWrapper;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * https://start.spring.io/
 */
public class SpringBlueprintManager {

    public static void main(String[] args) {
        Workplace.initialize();
        File initialLocation = Folder.extend(AppCtx.getInstance().getTempFolder(), "demo");
        new BlueprintManagerGUI<>(SpringBlueprint.class, initialLocation).addListener(blueprint -> {
            if (blueprint.isEmbedded()) {

                SpringBootGenCfg genCfg = new SpringBootGenCfg();
                genCfg.setDestinationFolder(blueprint.getWrappedFile());

                SpringBootGenerator generator = new SpringBootGenerator(blueprint, genCfg);
                Map<String, TextFileWrapper> artifacts = generator.generateArtifacts();

                for (TextFileWrapper artifact : artifacts.values()) {
                    if (genCfg.isDryRun()) {
                        System.out.println(GenUtility.toString(List.of(artifact)));
                    } else {
                        artifact.save();
                    }
                }

            } else {
                throw new UnsupportedOperationException();
            }
        });
    }

}
