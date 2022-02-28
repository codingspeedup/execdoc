package io.github.codingspeedup.execdoc.jhipster.blueprint;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;

import java.io.File;

public class JHipsterBlueprint extends Blueprint<JHipsterMaster> {

    public JHipsterBlueprint(File repository) {
        super(JHipsterMaster.class, repository);
    }

}
