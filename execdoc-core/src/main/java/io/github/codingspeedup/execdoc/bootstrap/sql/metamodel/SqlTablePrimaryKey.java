package io.github.codingspeedup.execdoc.bootstrap.sql.metamodel;

public class SqlTablePrimaryKey extends SqlTableKey {

    public SqlTablePrimaryKey(String name, SqlElement owner) {
        super(name, owner);
    }

    @Override
    public String toString() {
        return "+" + super.toString();
    }

    @Override
    public void addColumn(SqlTableColumn column, int keySeq) {
        super.addColumn(column, keySeq);
    }

}
