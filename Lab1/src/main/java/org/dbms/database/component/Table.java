package org.dbms.database.component;

import java.util.ArrayList;
import java.util.List;
import org.dbms.database.component.column.*;

public class Table {
    public String name;
    public List<Row> rows = new ArrayList<>();
    public List<Column> columns = new ArrayList<>();

    public Table(String name){
        this.name = name;
    }

  public Table(Table table) {
    this.name = table.name;
    for (Row row : table.rows) {
      Row newRow = new Row();
      for (String data : row.values) {
        newRow.values.add(data);
      }
      this.rows.add(newRow);
    }
    for (Column column : table.columns) {
      switch (ColumnType.valueOf(column.type)) {
        case INT -> {
          Column columnInt = new IntegerColumn(column.name);
          this.columns.add(columnInt);
        }
        case REAL -> {
          Column columnReal = new RealColumn(column.name);
          this.columns.add(columnReal);
        }
        case STRING -> {
          Column columnStr = new StringColumn(column.name);
          this.columns.add(columnStr);
        }
        case CHAR -> {
          Column columnChar = new CharColumn(column.name);
          this.columns.add(columnChar);
        }
        case EMAIL -> {
          Column emailColumn = new EmailColumn(column.name);
          this.columns.add(emailColumn);
        }
        case ENUM -> {
          Column enumColumn = new EnumColumn(column.name);
          this.columns.add(enumColumn);
        }
      }
    }
  }

  public void addRow(Row row) {
        rows.add(row);
    }

    public void deleteRow(int rowIndex) {
        rows.remove(rowIndex);
    }

    public void deleteColumn(int columnIndex) {
        columns.remove(columnIndex);
        for (Row row: rows) {
            row.values.remove(columnIndex);
        }
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void setName(String name) {
        this.name = name;
    }
}
