/*
 * Copyright 2004-2007 H2 Group. Licensed under the H2 License, Version 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.table;

import org.h2.command.dml.Select;
import org.h2.value.Value;

public class SingleColumnResolver implements ColumnResolver {

    private final Column column;
    private Value value;

    SingleColumnResolver(Column column) {
        this.column = column;
    }

    public String getTableAlias() {
        return null;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Value getValue(Column column) {
        return value;
    }

    public Column[] getColumns() {
        return new Column[] { column };
    }

    public String getSchemaName() {
        return null;
    }

    public TableFilter getTableFilter() {
        return null;
    }

    public Select getSelect() {
        return null;
    }

    public Column[] getSystemColumns() {
        return null;
    }

}