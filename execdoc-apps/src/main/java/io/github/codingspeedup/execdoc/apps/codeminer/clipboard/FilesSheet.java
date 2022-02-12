package io.github.codingspeedup.execdoc.apps.codeminer.clipboard;

import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FileOrFolderResource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class FilesSheet extends ClipboardSheet<FileOrFolderResource> {

    public static final String NAME = "FILES";

    public FilesSheet(CodeMinerClipboard clipboard, Sheet sheet) {
        super(clipboard, sheet);
    }

    @Override
    protected List<Cell> selectParentsByGroup(Cell groupCell) {
        List<Cell> parents = new ArrayList<>();
        parents.add(groupCell);
        return parents;
    }

}
