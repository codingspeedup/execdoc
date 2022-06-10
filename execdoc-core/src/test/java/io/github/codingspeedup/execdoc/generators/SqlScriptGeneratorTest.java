package io.github.codingspeedup.execdoc.generators;

import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBase;
import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBaseColumn;
import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBaseTable;
import io.github.codingspeedup.execdoc.miners.jdbc.SqlEngine;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlScriptGeneratorTest {

    @Test
    void simpleXlsxBase() {
        System.setProperty("log4j2.loggerContextFactory","org.apache.logging.log4j.simple.SimpleLoggerContextFactory");


        File xlsxBaseFile = new File("./src/test/resources/testing/xlsx-base-sample.xlsx");
        XlsxBase xlsxBase = new XlsxBase(xlsxBaseFile);
        XlsxBaseTable tProvider = xlsxBase.getTable("PROVIDER");
        XlsxBaseColumn tProviderId = tProvider.getColumn(0);
        assertTrue(tProviderId.isPk());
        assertTrue(tProviderId.isMandatory());

        SqlScriptGenerator sqlGen = new SqlScriptGenerator(xlsxBase);
        String sqlScript = sqlGen.getSqlScript(null, SqlEngine.PGSQL);
        assertEquals(3, sqlGen.getStatementsOnly().size());
    }

}