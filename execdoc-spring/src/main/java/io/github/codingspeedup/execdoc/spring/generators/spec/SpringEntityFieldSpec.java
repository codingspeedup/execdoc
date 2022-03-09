package io.github.codingspeedup.execdoc.spring.generators.spec;

public interface SpringEntityFieldSpec extends SpringEntitySpec {

    String getFieldTypeHint();

    String getFieldName();

    String getColumnName();

    default boolean isRequired() {
        return false;
    }

    default boolean isUnique() {
        return false;
    }

    default Integer getMinSize() {
        return null;
    }

    default Integer getMaxSize() {
        return null;
    }


}
