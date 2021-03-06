package io.github.codingspeedup.execdoc.apps;

import io.github.codingspeedup.execdoc.toolbox.files.Folder;
import io.github.codingspeedup.execdoc.toolbox.utilities.OsUtility;

public interface AppCtxProvider {

    default String getDotPath() {
        return "";
    }

    String getContextMarker();

    default Folder getBpProjectRoot() {
        return Folder.of(".");
    }

    default Folder getConfigFolder() {
        return Folder.of(Folder.extend(OsUtility.getUserHomeFolder(), ".config/cs-blueprint", getContextMarker()));
    }

    default Folder getTempFolder() {
        return Folder.of(Folder.extend((OsUtility.getTempFolder()), getContextMarker()));
    }

    default Folder getLocalCopyLogFolder() {
        return Folder.of(Folder.extend(getTempFolder(), "log-files-cache"));
    }

}
