package io.github.codingspeedup.execdoc.spring.generators;

import io.github.codingspeedup.execdoc.generators.utilities.GenCfg;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class SpringBootGenCfg implements GenCfg {

    @Getter
    @Setter
    private File destinationFolder;

}
