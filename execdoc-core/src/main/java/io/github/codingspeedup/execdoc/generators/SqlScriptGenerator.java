package io.github.codingspeedup.execdoc.generators;

import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBase;
import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBaseColumn;
import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBaseTable;
import io.github.codingspeedup.execdoc.bootstrap.sql.XlsxBaseType;
import io.github.codingspeedup.execdoc.miners.jdbc.SqlEngine;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxUtil;
import io.github.codingspeedup.execdoc.toolbox.utilities.DateTimeUtility;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SqlScriptGenerator {

    private static final String DERBY_TIME_PATTERN = "HH.mm.ss";

    private final XlsxBase xlsxBase;

    public SqlScriptGenerator(XlsxBase xlsxBase) {
        this.xlsxBase = xlsxBase;
    }

    @Getter
    private List<String> statementsOnly;

    public synchronized String getSqlScript(String tablePrefix, SqlEngine sqlEngine) {
        StringBuilder sqlScript = new StringBuilder();
        statementsOnly = new ArrayList<>();
        for (String tableName : xlsxBase.getTableNames()) {
            XlsxBaseTable table = xlsxBase.getTable(tableName);
            appendTableDeclaration(sqlScript, table);
            String fqTableName = StringUtils.isBlank(tablePrefix) ? tableName : tablePrefix + "." + tableName;
            List<XlsxBaseColumn> columns = table.getColumnList();
            StringBuilder insertStatement;
            for (int rowIdx = XlsxBase.FIRST_DATA_ROW_INDEX; rowIdx <= table.getTableSheet().getLastRowNum(); ++rowIdx) {
                Row record = table.getTableSheet().getRow(rowIdx);
                if (!XlsxUtil.isEmpty(record)) {
                    List<String> values = fetchSqlValues(record, columns, sqlEngine);
                    insertStatement = openInsertStatement(fqTableName, columns, sqlEngine);
                    extendInsertStatement(insertStatement, values);
                    if (SqlEngine.PGSQL == sqlEngine) {
                        appendOnConflict(insertStatement, columns, sqlEngine);
                        appendDoUpdateSet(insertStatement, columns, values, sqlEngine);
                    }
                    statementsOnly.add(insertStatement.toString());
                    sqlScript.append("\n").append(closeInsertStatement(insertStatement));
                }
            }
        }
        return sqlScript.toString();
    }

    private String quoteIdentifier(String identifier, SqlEngine sqlEngine) {
        String identifierQuoteString = sqlEngine == SqlEngine.PGSQL ? "\"" : "`";
        return identifierQuoteString + identifier + identifierQuoteString;
    }

    private void appendTableDeclaration(StringBuilder sql, XlsxBaseTable table) {
        if (sql.length() > 0) {
            sql.append("\n");
        }
        int commentLength = table.getName().length() + 6;
        sql.append("\n").append(StringUtils.repeat("-", commentLength)).append("\n");
        sql.append("-- ").append(table.getName()).append(" --\n");
        sql.append(StringUtils.repeat("-", commentLength));
    }

    private StringBuilder openInsertStatement(String tableName, List<XlsxBaseColumn> columns, SqlEngine sqlEngine) {
        StringBuilder statement = new StringBuilder("Insert into ").append(tableName).append(" ");
        statement.append(columns.stream()
                .map(XlsxBaseColumn::getName)
                .map(n -> quoteIdentifier(n, sqlEngine))
                .collect(Collectors.joining(",", "(", ")")));
        return statement.append(" values ");
    }

    private void extendInsertStatement(StringBuilder statement, List<String> values) {
        statement.append(values.stream().collect(Collectors.joining(",", "(", ")")));
    }

    private void appendOnConflict(StringBuilder statement, List<XlsxBaseColumn> columns, SqlEngine sqlEngine) {
        statement.append("\n  on conflict(");
        for (XlsxBaseColumn column : columns) {
            if (column.isPk()) {
                statement.append(quoteIdentifier(column.getName(), sqlEngine)).append(",");
            }
        }
        statement.setLength(statement.length() - 1);
        statement.append(")");
    }

    private void appendDoUpdateSet(
            StringBuilder statement, List<XlsxBaseColumn> columns, List<String> values, SqlEngine sqlEngine) {
        statement.append("\n  do update set ");
        for (XlsxBaseColumn column : columns) {
            if (!column.isPk()) {
                statement.append(quoteIdentifier(column.getName(), sqlEngine));
                statement.append("=");
                statement.append(values.get(column.getIndex()));
                statement.append(",");
            }
        }
        statement.setLength(statement.length() - 1);
    }

    private String closeInsertStatement(StringBuilder statement) {
        statement.append(";");
        return statement.toString();
    }

    private List<String> fetchSqlValues(Row record, List<XlsxBaseColumn> columns, SqlEngine sqlEngine) {
        String nullValue = "NULL";
        List<String> values = new ArrayList<>();
        columns.forEach(column -> {
            Cell cell = record.getCell(column.getIndex());
            if (XlsxUtil.isEmpty(cell)) {
                if (column.isMandatory()) {
                    throw new UnsupportedOperationException("Sheet " + record.getSheet().getSheetName() + " row " + record.getRowNum() + " column " + column.getIndex() + " cannot be empty!");
                } else {
                    values.add(nullValue);
                }
            } else {
                switch (column.getType()) {
                    case STRING:
                        values.add(formatStringValue(cell, sqlEngine));
                        break;
                    case NUMERIC:
                        values.add(formatNumericValue(cell, sqlEngine));
                        break;
                    case DATE:
                        values.add(formatDateValue(cell, sqlEngine));
                        break;
                    case TIMESTAMP:
                        values.add(formatTimestampValue(cell, sqlEngine));
                        break;
                    case BOOLEAN:
                        values.add(formatBooleanValue(cell, sqlEngine));
                        break;
                    default:
                        throw new UnsupportedOperationException("Undefined conversion for " + XlsxBaseType.class.getSimpleName() + " " + column.getType());
                }
            }
        });
        return values;
    }

    private String formatBooleanValue(Cell cell, SqlEngine sqlEngine) {
        Boolean value = XlsxUtil.getCellValue(cell, Boolean.class);
        switch (sqlEngine) {
            case DERBY:
            case MYSQL:
                return value ? "TRUE" : "FALSE";
            default:
                throw newConversionException(cell, XlsxBaseType.BOOLEAN);
        }
    }

    private String formatDateValue(Cell cell, SqlEngine sqlEngine) {
        Date value = XlsxUtil.getCellValue(cell, Date.class);
        switch (sqlEngine) {
            case DERBY:
            case MYSQL:
                return "DATE('" + DateTimeUtility.toIsoDateString(value) + "')";
            case PGSQL:
                return "'" + DateTimeUtility.toIsoDateString(value)  + "'";
            default:
                throw newConversionException(cell, XlsxBaseType.DATE);
        }
    }

    private String formatNumericValue(Cell cell, SqlEngine sqlEngine) {
        BigDecimal value = XlsxUtil.getCellValue(cell, BigDecimal.class);
        switch (sqlEngine) {
            case DERBY:
            case MYSQL:
            case PGSQL:
                String strValue = value.toString();
                if (strValue.contains(".")) {
                    while (strValue.endsWith("0")) {
                        strValue = strValue.substring(0, strValue.length() - 1);
                    }
                    if (strValue.endsWith(".")) {
                        strValue = strValue.substring(0, strValue.length() - 1);
                    }
                }
                return strValue;
            default:
                throw newConversionException(cell, XlsxBaseType.BOOLEAN);
        }
    }

    private String formatStringValue(Cell cell, SqlEngine sqlEngine) {
        String value = XlsxUtil.getCellValue(cell, String.class);
        switch (sqlEngine) {
            case DERBY:
            case MYSQL:
            case PGSQL:
                value = value.replace("'", "''");
                break;
            default:
                throw newConversionException(cell, XlsxBaseType.BOOLEAN);
        }
        return "'" + value + "'";
    }

    private String formatTimestampValue(Cell cell, SqlEngine sqlEngine) {
        Date value = XlsxUtil.getCellValue(cell, Date.class);
        switch (sqlEngine) {
            case DERBY:
                return "TIMESTAMP('" + DateTimeUtility.toIsoDateString(value) + "','" + new SimpleDateFormat(DERBY_TIME_PATTERN).format(value) + "')";
            case MYSQL:
                return "TIMESTAMP('" + DateTimeUtility.toIsoDateTimeString(value) + "')";
            default:
                throw newConversionException(cell, XlsxBaseType.BOOLEAN);
        }
    }

    private RuntimeException newConversionException(Cell cell, XlsxBaseType type) {
        return new UnsupportedOperationException("Undefined conversion to " + type + " for " + cell.getRow().getSheet().getSheetName() + " row " + cell.getRow().getRowNum() + " column " + cell.getColumnIndex());
    }

}
