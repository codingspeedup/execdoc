package io.github.codingspeedup.execdoc.apps.folderdiff;

import io.github.codingspeedup.execdoc.miners.diff.folders.FolderDiffMiner;

public interface FolderDiffGuiConfiguration {

    default FolderDiffMiner createMiner() {
        return new FolderDiffMiner();
    }

}
