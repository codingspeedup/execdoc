package io.github.codingspeedup.execdoc.spring;

import io.github.codingspeedup.execdoc.apps.AppCtx;
import io.github.codingspeedup.execdoc.apps.bpmanager.BlueprintManagerGUI;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;
import io.github.codingspeedup.execdoc.toolbox.files.Folder;

import java.io.File;

/**
 * https://start.spring.io/
 */
public class SpringBlueprintManager {

    public static void main(String[] args) {
        Workplace.initialize();
        File initialLocation = Folder.extend(AppCtx.getInstance().getTempFolder(), "demo");
        new BlueprintManagerGUI<>(SpringBlueprint.class, initialLocation).addListener(blueprint -> {
            throw new UnsupportedOperationException();
        });
    }

}
