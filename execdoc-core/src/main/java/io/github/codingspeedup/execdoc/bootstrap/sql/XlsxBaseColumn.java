package io.github.codingspeedup.execdoc.bootstrap.sql;

import io.github.codingspeedup.execdoc.bootstrap.sql.metamodel.SqlTableColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;

@NoArgsConstructor
@Getter
@Setter
public class XlsxBaseColumn {

    private int index = -1;
    private String name;
    private XlsxBaseType type;
    private boolean mandatory;
    private boolean pk;

    public static XlsxBaseColumn from(SqlTableColumn jdbcColumn) {
        XlsxBaseColumn column = new XlsxBaseColumn();
        column.setName(XlsxBase.normalizeName(jdbcColumn.getName()));
        column.setType(XlsxBaseType.from(jdbcColumn.getDataType(), jdbcColumn.getTypeName()));
        column.setMandatory(!BooleanUtils.toBoolean(jdbcColumn.getNullable()));
        return column;
    }

}
