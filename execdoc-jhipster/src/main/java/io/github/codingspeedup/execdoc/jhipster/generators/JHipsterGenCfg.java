package io.github.codingspeedup.execdoc.jhipster.generators;

import io.github.codingspeedup.execdoc.generators.utilities.GenCfg;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class JHipsterGenCfg implements GenCfg {

    @Getter
    @Setter
    private File destinationFolder;

    @Getter
    @Setter
    private boolean force;

}
