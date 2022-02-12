package io.github.codingspeedup.execdoc.poc.jhipster.blueprint;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.blueprint.BlueprintGenCfg;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.generators.SolutionGenerator;

import java.io.File;

public class JHipsterBlueprint extends Blueprint<JHipsterMaster> {

    public JHipsterBlueprint(File repository) {
        super(JHipsterMaster.class, repository);
    }

    @Override
    public void generate(BlueprintGenCfg bpGenCfg) {
        new SolutionGenerator(this, (JHipsterGenCfg) bpGenCfg).generateApp();
    }

}
