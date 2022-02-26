package io.github.codingspeedup.execdoc.bootstrap.sql.metamodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SqlColumn extends SqlElement {

    private int ordinalPosition;

    public SqlColumn(String name, SqlElement owner) {
        super(name, owner);
    }

}
