package io.github.codingspeedup.execdoc.spring.generators;

import io.github.codingspeedup.execdoc.generators.utilities.GenConfig;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;

public class SpringGenConfig implements GenConfig, Serializable {

    @Getter
    @Setter
    private File destinationFolder;

    @Getter
    @Setter
    private boolean restMethods = true;

    @Getter
    @Setter
    private boolean serviceMethods = true;

    @Getter
    @Setter
    private boolean force = false;

    @Getter
    @Setter
    private boolean dryRun = true;

}
