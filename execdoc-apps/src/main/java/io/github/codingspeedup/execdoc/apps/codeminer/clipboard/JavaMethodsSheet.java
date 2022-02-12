package io.github.codingspeedup.execdoc.apps.codeminer.clipboard;

import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import io.github.codingspeedup.execdoc.toolbox.resources.java.JavaMethodResource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class JavaMethodsSheet extends ClipboardSheet<JavaMethodResource> {

    public static final String NAME = "JAVA METHODS";

    public JavaMethodsSheet(CodeMinerClipboard clipboard, Sheet sheet) {
        super(clipboard, sheet);
    }

    @Override
    protected List<Cell> selectParentsByGroup(Cell groupCell) {
        FilesSheet filesSheet = new FilesSheet(getClipboard(), getSheet().getWorkbook().getSheet(FilesSheet.NAME));
        List<Cell> parentFiles = filesSheet.selectByParent(groupCell);
        JavaTypesSheet typesSheet = new JavaTypesSheet(getClipboard(), getSheet().getWorkbook().getSheet(JavaTypesSheet.NAME));
        List<Cell> parentTypes = new ArrayList<>();
        for (Cell fileCell : parentFiles) {
            parentTypes.addAll(typesSheet.selectByParent(fileCell));
        }
        return parentTypes;
    }

    @Override
    public JavaMethodResource getResource(Cell descriptor) {
        JavaMethodResource resource = super.getResource(descriptor);
        JavaTypesSheet parentSheet = new JavaTypesSheet(getClipboard(), getSheet().getWorkbook().getSheet(JavaTypesSheet.NAME));
        resource.setParent(parentSheet.getResource(XlsxUtil.backtraceCellBySimpleFormulaReference(descriptor.getRow().getCell(PARENT_COL_IDX))));
        return resource;
    }

}
