package io.github.codingspeedup.execdoc.apps.codeminer.clipboard;

import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import io.github.codingspeedup.execdoc.toolbox.resources.DefaultResourceFactory;
import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.resources.filesystem.FolderResource;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ClipboardSheet<T extends Resource> {

    public static final int PARENT_COL_IDX = 0;
    public static final int DESCRIPTOR_COL_IDX = 1;
    public static final int PROPERTIES_COL_IDX = 2;

    @Getter(AccessLevel.PROTECTED)
    private final CodeMinerClipboard clipboard;
    @Getter
    private final Sheet sheet;

    public ClipboardSheet(CodeMinerClipboard clipboard, Sheet sheet) {
        this.clipboard = clipboard;
        this.sheet = sheet;
    }

    protected static String normalizeFormula(String formula) {
        return formula.replace("'", "");
    }

    public void normalize() {
        sheet.setColumnWidth(DESCRIPTOR_COL_IDX, 25_000);
    }

    public Row insert(Cell parentCell, Resource resource) {
        int rowIdx = getSheet().getLastRowNum() + 1;
        Row row = getSheet().createRow(rowIdx);
        if (parentCell != null) {
            Cell cell = row.createCell(PARENT_COL_IDX);
            cell.setCellFormula(XlsxUtil.createCellReference(parentCell));
        }
        Cell cell = row.createCell(DESCRIPTOR_COL_IDX);
        cell.setCellValue(resource.getDescriptor());
        JSONObject properties = resource.getProperties();
        if (properties != null) {
            cell = row.createCell(PROPERTIES_COL_IDX);
            cell.setCellValue(properties.toString());
        }
        clipboard.setDirty(true);
        return row;
    }

    public Cell selectByDescriptor(String descriptor) {
        return selectByDescriptor(null, descriptor);
    }

    public Cell selectByDescriptor(Cell parentCell, String descriptor) {
        if (StringUtils.isBlank(descriptor)) {
            return null;
        }
        if (parentCell == null) {
            for (Row row : getSheet()) {
                Cell cell = row.getCell(PARENT_COL_IDX);
                if (StringUtils.isBlank(XlsxUtil.getCellValue(cell, String.class))) {
                    cell = row.getCell(DESCRIPTOR_COL_IDX);
                    if (StringUtils.equals(descriptor, XlsxUtil.getCellValue(cell, String.class))) {
                        return cell;
                    }
                }
            }
        } else {
            String parentFormula = normalizeFormula(XlsxUtil.createCellReference(parentCell));
            for (Row row : getSheet()) {
                Cell cell = row.getCell(PARENT_COL_IDX);
                if (StringUtils.equals(parentFormula, normalizeFormula(cell.getCellFormula()))) {
                    cell = row.getCell(DESCRIPTOR_COL_IDX);
                    if (StringUtils.equals(descriptor, XlsxUtil.getCellValue(cell, String.class))) {
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    public List<Cell> selectByParent(Cell parentCell) {
        List<Cell> cells = new ArrayList<>();
        if (parentCell == null) {
            for (Row row : getSheet()) {
                Cell cell = row.getCell(PARENT_COL_IDX);
                if (StringUtils.isBlank(XlsxUtil.getCellValue(cell, String.class))) {
                    cells.add(row.getCell(DESCRIPTOR_COL_IDX));
                }
            }
        } else {
            String formula = normalizeFormula(XlsxUtil.createCellReference(parentCell));
            for (Row row : getSheet()) {
                Cell cell = row.getCell(PARENT_COL_IDX);
                if (cell != null && formula.equals(normalizeFormula(cell.getCellFormula()))) {
                    cells.add(row.getCell(DESCRIPTOR_COL_IDX));
                }
            }
        }
        return cells;
    }

    protected abstract List<Cell> selectParentsByGroup(Cell groupCell);

    public List<Cell> selectByGroup(Cell groupCell) {
        List<Cell> parentCells = selectParentsByGroup(groupCell);
        List<Cell> cells = new ArrayList<>();
        parentCells.forEach(cell -> cells.addAll(selectByParent(cell)));
        return cells;
    }

    @SuppressWarnings({"unchecked"})
    public T getResource(Cell descriptor) {
        new FolderResource("");
        Resource resource = DefaultResourceFactory.INSTANCE.from(XlsxUtil.getCellValue(descriptor, String.class));
        Cell properties = descriptor.getRow().getCell(PROPERTIES_COL_IDX);
        String json = XlsxUtil.getCellValue(properties, String.class);
        if (StringUtils.isNotBlank(json)) {
            resource.setProperties(new JSONObject(json));
        }
        return (T) resource;
    }

    public List<T> getResourcesByParent(Cell parentCell) {
        return selectByParent(parentCell).stream().map(this::getResource).collect(Collectors.toList());
    }

    public List<T> getResourcesByGroup(Cell groupCell) {
        return selectByGroup(groupCell).stream().map(this::getResource).collect(Collectors.toList());
    }

}
