package io.github.codingspeedup.execdoc.apps.bpmanager;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;

public interface BlueprintManagerListener<B extends Blueprint<?>> {

    void onGenerate(B blueprint);

}
