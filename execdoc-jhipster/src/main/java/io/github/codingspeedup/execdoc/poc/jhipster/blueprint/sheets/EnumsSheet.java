package io.github.codingspeedup.execdoc.poc.jhipster.blueprint.sheets;

import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.individuals.code.JdlEnum;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import io.github.codingspeedup.execdoc.toolbox.utilities.NamingUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.JHipsterSheet;
import io.github.codingspeedup.execdoc.poc.jhipster.blueprint.metamodel.individuals.code.JdlEnumEntry;

public class EnumsSheet extends JHipsterSheet {

    public static final String NAME_MARKER = "ENUMs" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Business";

    public static final String ANCHOR_NAME = CellMarkers.ANCHOR_MARKER + "EnumName / KEY_NAME";
    public static final String ANCHOR_LABEL = CellMarkers.ANCHOR_MARKER + "Key value";

    public EnumsSheet(BlueprintMaster master, Sheet sheet) {
        super(master, sheet);
    }

    @Override
    public int initialize() {
        int rowIdx = 0;
        int colIdx = 0;
        setCellValue(rowIdx, ++colIdx, ANCHOR_NAME);
        setCellValue(rowIdx, ++colIdx, ANCHOR_LABEL);
        autoSizeColumns("0-" + colIdx);
        getSheet().createFreezePane(0, rowIdx + 1);

        rowIdx = getAnchors().getLastAnchorRow() + 2;
        colIdx = getAnchors().getColumn(ANCHOR_NAME);
        getSheet().setActiveCell(new CellAddress(rowIdx, colIdx));

        return rowIdx;
    }

    @Override
    public void normalize() {
        Cell enumCell = null;
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Cell nameCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME));
            String nameString = XlsxUtil.getCellValue(nameCell, String.class);
            if (StringUtils.isBlank(nameString)) {
                enumCell = null;
                continue;
            }

            getCellComment(nameCell).ifPresent(cc -> setCellComment(nameCell, cc));

            if (enumCell == null) {
                enumCell = nameCell;
                String normalizedNameString = NamingUtility.toUpperCamel(nameString);
                if (!nameString.equals(normalizedNameString)) {
                    nameString = normalizedNameString;
                    XlsxUtil.setCellValue(nameCell, nameString);
                }
            } else {
                String normalizedNameString = NamingUtility.toUpperUnderscore(nameString);
                if (!nameString.equals(normalizedNameString)) {
                    nameString = normalizedNameString;
                    XlsxUtil.setCellValue(nameCell, nameString);
                }
            }
        }
        autoSizeColumns(
                getAnchors().getColumn(ANCHOR_NAME),
                getAnchors().getColumn(ANCHOR_LABEL)
        );
    }

    @Override
    public void expand(Kb kb) {
        BpSheet owner = new BpSheet(getSheet());
        JdlEnum enumType = null;
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Cell nameCell = getCell(rowIdx, getAnchors().getColumn(ANCHOR_NAME));
            if (XlsxUtil.isBlank(nameCell)) {
                kb.learn(enumType);
                enumType = null;
                continue;
            }

            if (enumType == null) {
                enumType = new JdlEnum(nameCell);
                enumType.setOwner(owner);
            } else {
                JdlEnumEntry enumConstant = new JdlEnumEntry(nameCell);
                String label = XlsxUtil.getCellValue(getCell(rowIdx, getAnchors().getColumn(ANCHOR_LABEL)), String.class);
                if (StringUtils.isNotBlank(label)) {
                    enumConstant.setExt(label);
                }
                enumType.getValue().add(enumConstant);
            }
        }
        kb.learn(enumType);
    }

}
