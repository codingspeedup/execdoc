package io.github.codingspeedup.execdoc.spring.generators.spec.impl;

import io.github.codingspeedup.execdoc.spring.generators.spec.SpringEntityFieldSpec;
import lombok.Builder;

@Builder
public class SpringEntityFieldImpl implements SpringEntityFieldSpec {

    private String packageName;
    private String typeLemma;
    private String tableName;
    private String fieldTypeHint;
    private String fieldName;
    private String columnName;

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getTypeName() {
        return typeLemma;
    }

    @Override
    public String getTypeLemma() {
        return typeLemma;
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getFieldTypeHint() {
        return fieldTypeHint;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

}
