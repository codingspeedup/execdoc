package io.github.codingspeedup.execdoc.blueprint.master.sheets.core;

import io.github.codingspeedup.execdoc.blueprint.utilities.NormReport;
import io.github.codingspeedup.execdoc.kb.Kb;
import io.github.codingspeedup.execdoc.blueprint.metamodel.individuals.BpSheet;
import io.github.codingspeedup.execdoc.blueprint.metamodel.IsOwned;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.CClassUnit;
import io.github.codingspeedup.execdoc.blueprint.metamodel.vocabulary.concepts.code.CMethodUnit;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellMarkers;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.BlueprintSheet;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;

import java.util.List;

public abstract class AbstractMethodsSheet<C extends CClassUnit, M extends CMethodUnit> extends BlueprintSheet {

    public static final String ANCHOR_NAME = CellMarkers.ANCHOR_MARKER + "ClassName / methodName";

    protected final Class<C> componentClass;
    protected final Class<M> methodClass;

    protected AbstractMethodsSheet(BlueprintMaster bp, Sheet sheet, Class<C> componentClass, Class<M> methodClass) {
        super(bp, sheet);
        this.componentClass = componentClass;
        this.methodClass = methodClass;
    }

    @Override
    public int initialize() {
        int rowIdx = 0;
        int colIdx = 0;
        setCellValue(rowIdx, ++colIdx, ANCHOR_NAME);
        autoSizeColumns("0-" + colIdx);
        getSheet().createFreezePane(0, rowIdx + 1);

        rowIdx = getAnchors().getLastAnchorRow() + 2;
        colIdx = getAnchors().getColumn(ANCHOR_NAME);
        getSheet().setActiveCell(new CellAddress(rowIdx, colIdx));

        return rowIdx;
    }

    @Override
    public void normalize(NormReport normReport) {
        autoSizeColumns(getAnchors().getColumn(ANCHOR_NAME));
    }

    @SneakyThrows
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void expand(Kb kb) {
        BpSheet owner = new BpSheet(getSheet());
        C classUnit = null;
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Row row = getSheet().getRow(rowIdx);
            if (row == null) {
                continue;
            }

            Cell nameCell = row.getCell(getAnchors().getColumn(ANCHOR_NAME));
            String nameString = XlsxUtil.getCellValue(nameCell, String.class);
            if (StringUtils.isBlank(nameString)) {
                kb.learn(classUnit);
                classUnit = null;
                continue;
            }

            if (classUnit == null) {
                classUnit = componentClass.getConstructor(Cell.class).newInstance(nameCell);
                ((IsOwned) classUnit).setOwner(owner);
            } else {
                M methodUnit = methodClass.getConstructor(Cell.class).newInstance(nameCell);
                ((List) classUnit.getCodeElement()).add(methodUnit);
                kb.learn(methodUnit);
            }
        }
        kb.learn(classUnit);
    }

}
