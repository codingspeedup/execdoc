package io.github.codingspeedup.execdoc.blueprint.metamodel;

import io.github.codingspeedup.execdoc.kb.KbTermBuilder;
import it.unibo.tuprolog.core.Struct;
import it.unibo.tuprolog.core.Var;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BpTermBuilderTest {

    private static final KbTermBuilder TERM_BUILDER = new BpTermBuilder();

    @Test
    void structOf_xlsx() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("start");
        Cell cell = sheet.createRow(0).createCell(0);
        Pair<Struct, List<Var>> structVar;
        structVar = TERM_BUILDER.structOf(true, "contains", sheet, cell);
        assertEquals("contains(s0, s0A1)", structVar.getLeft().toString());
    }

}