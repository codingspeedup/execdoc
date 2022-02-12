package io.github.codingspeedup.execdoc.apps.codeminer.clipboard;

import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaPackageResource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class JavaPackagesSheet extends ClipboardSheet<JavaPackageResource> {

    public static final String NAME = "JAVA PACKAGES";

    public JavaPackagesSheet(CodeMinerClipboard clipboard, Sheet sheet) {
        super(clipboard, sheet);
    }

    @Override
    protected List<Cell> selectParentsByGroup(Cell groupCell) {
        List<Cell> parents = new ArrayList<>();
        parents.add(null);
        return parents;
    }

}
