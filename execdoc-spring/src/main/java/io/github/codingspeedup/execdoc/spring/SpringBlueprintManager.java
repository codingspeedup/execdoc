package io.github.codingspeedup.execdoc.spring;

import io.github.codingspeedup.execdoc.apps.bpmanager.BlueprintManagerGUI;
import io.github.codingspeedup.execdoc.spring.blueprint.SpringBlueprint;

/**
 * https://start.spring.io/
 */
public class SpringBlueprintManager {

    public static void main(String[] args) {
        Workplace.initialize();
        new BlueprintManagerGUI<>(SpringBlueprint.class, null).addListener(blueprint -> {
            throw new UnsupportedOperationException();
        });
    }

}
