package io.github.codingspeedup.execdoc.generators;

import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBase;
import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBaseColumn;
import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBaseTable;
import io.github.codingspeedup.execdoc.miners.jdbc.SqlEngine;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlScriptGeneratorXP {

    public static void main(String[] args) {
        XlsxBase xlsxBase = new XlsxBase(new File("/Users/iulian/temp/parameters.xlsx"));
        XlsxBaseTable tOffer = xlsxBase.getTable("OFFER");
        XlsxBaseColumn tOfferID = tOffer.getColumn(0);
        assertTrue(tOfferID.isPk());
        assertTrue(tOfferID.isMandatory());

        SqlScriptGenerator sqlGen = new SqlScriptGenerator(xlsxBase);
        String sqlScript = sqlGen.getSqlScript(null, SqlEngine.PGSQL);
        System.out.println(sqlScript);
    }

}