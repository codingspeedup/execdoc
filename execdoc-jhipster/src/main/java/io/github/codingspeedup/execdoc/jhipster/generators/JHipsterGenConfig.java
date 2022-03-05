package io.github.codingspeedup.execdoc.jhipster.generators;

import io.github.codingspeedup.execdoc.generators.utilities.GenConfig;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class JHipsterGenConfig implements GenConfig {

    @Getter
    @Setter
    private File destinationFolder;

    @Getter
    @Setter
    private boolean force;

}
