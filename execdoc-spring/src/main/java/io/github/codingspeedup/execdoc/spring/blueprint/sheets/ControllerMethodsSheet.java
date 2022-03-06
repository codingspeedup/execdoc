package io.github.codingspeedup.execdoc.spring.blueprint.sheets;

import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellComment;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.AbstractMethodsSheet;
import io.github.codingspeedup.execdoc.blueprint.utilities.NormReport;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpController;
import io.github.codingspeedup.execdoc.spring.blueprint.metamodel.individuals.code.BpControllerMethod;
import io.github.codingspeedup.execdoc.spring.generators.spec.HttpRequestMethod;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerMethodsSheet extends AbstractMethodsSheet<BpController, BpControllerMethod> {

    public static final String NAME_MARKER = "CONTROLLERs" + BlueprintMaster.INSTANTIABLE_SHEET_MARKER;
    public static final String TOC_CHAPTER = "Presentation";

    public ControllerMethodsSheet(BlueprintMaster bp, Sheet sheet) {
        super(bp, sheet, BpController.class, BpControllerMethod.class);
    }

    public static Set<String> findHttpMethods(Map<String, String> annotations) {
        return annotations.keySet().stream().filter(aName -> {
            try {
                HttpRequestMethod.valueOf(aName);
                return true;
            } catch (RuntimeException rEx) {
                return false;
            }
        }).collect(Collectors.toSet());
    }

    @SneakyThrows
    @Override
    public void normalize(NormReport normReport) {
        super.normalize(normReport);
        BpController classUnit = null;
        for (int rowIdx = getAnchors().getLastAnchorRow() + 1; rowIdx <= getSheet().getLastRowNum(); ++rowIdx) {
            Row row = getSheet().getRow(rowIdx);
            if (row == null) {
                continue;
            }

            final Cell nameCell = row.getCell(getAnchors().getColumn(ANCHOR_NAME));
            String nameString = XlsxUtil.getCellValue(nameCell, String.class);
            if (StringUtils.isBlank(nameString)) {
                classUnit = null;
                continue;
            }

            if (classUnit == null) {
                classUnit = componentClass.getConstructor(Cell.class).newInstance(nameCell);
            } else {
                CellComment comment = getCellComment(nameCell).orElse(null);
                if (comment == null) {
                    comment = setCellComment(nameCell, new CellComment());
                }
                Set<String> httpMethods = findHttpMethods(comment.getAnnotations());
                if (httpMethods.isEmpty()) {
                    normReport.addInfo(nameCell, "No HTTP method specified");
                } else if (httpMethods.size() >= 2) {
                    normReport.addError(nameCell, "Too many HTTP methods specified " + httpMethods);
                }
            }
        }
    }

}
