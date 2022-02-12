package io.github.codingspeedup.execdoc.apps.codeminer.clipboard;

import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaTypeResource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class JavaTypesSheet extends ClipboardSheet<JavaTypeResource> {

    public static final String NAME = "JAVA TYPES";

    public JavaTypesSheet(CodeMinerClipboard clipboard, Sheet sheet) {
        super(clipboard, sheet);
    }

    @Override
    protected List<Cell> selectParentsByGroup(Cell groupCell) {
        FilesSheet parentSheet = new FilesSheet(getClipboard(), getSheet().getWorkbook().getSheet(FilesSheet.NAME));
        return parentSheet.selectByParent(groupCell);
    }

}
