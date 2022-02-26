package io.github.codingspeedup.execdoc.jhipster.blueprint;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.generators.GenCfg;
import io.github.codingspeedup.execdoc.jhipster.generators.JHipsterGenCfg;
import io.github.codingspeedup.execdoc.jhipster.generators.SolutionGenerator;

import java.io.File;

public class JHipsterBlueprint extends Blueprint<JHipsterMaster> {

    public JHipsterBlueprint(File repository) {
        super(JHipsterMaster.class, repository);
    }

    @Override
    public void generate(GenCfg bpGenCfg) {
        new SolutionGenerator(this, (JHipsterGenCfg) bpGenCfg).generateApp();
    }

}
