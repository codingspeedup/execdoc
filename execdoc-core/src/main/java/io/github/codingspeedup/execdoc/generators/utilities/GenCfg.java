package io.github.codingspeedup.execdoc.generators.utilities;

import java.io.File;


public interface GenCfg {

    File getDestinationFolder();

    default boolean isForceRun() {
        return false;
    }

    default boolean isDryRun() {
        return true;
    }

}
