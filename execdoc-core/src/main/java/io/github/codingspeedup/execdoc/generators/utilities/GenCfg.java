package io.github.codingspeedup.execdoc.generators.utilities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@NoArgsConstructor
@Getter
@Setter
public abstract class GenCfg {

    private File destinationFolder;
    private boolean force;

}
