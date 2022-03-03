package io.github.codingspeedup.execdoc.spring.generators;

import io.github.codingspeedup.execdoc.generators.utilities.GenCfg;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;

public class SpringBootGenCfg implements GenCfg, Serializable {

    @Getter
    @Setter
    private File destinationFolder;

    @Getter
    @Setter
    private boolean restMethods = true;

    @Getter
    @Setter
    private boolean dryRun = true;

}
