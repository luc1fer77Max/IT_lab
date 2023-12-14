package org.dbms.database;

import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.dbms.database.component.Column;
import org.dbms.database.component.Database;
import org.dbms.database.component.Row;
import org.dbms.database.component.Table;
import org.dbms.database.component.column.CharColumn;
import org.dbms.database.component.column.ColumnType;
import org.dbms.database.component.column.EmailColumn;
import org.dbms.database.component.column.EnumColumn;
import org.dbms.database.component.column.IntegerColumn;
import org.dbms.database.component.column.RealColumn;
import org.dbms.database.component.column.StringColumn;
import org.dbms.database.file.DatabaseExporter;
import org.dbms.database.file.DatabaseImporter;
import org.dbms.database.ui.DBManagementSystem;
import org.dbms.database.ui.MyTable;
import org.dbms.database.ui.MyTableModel;

public class DBManager {
    private static DBManager instance;
    public static DBManagementSystem instanceCSW;

    private DBManager(){
    }

    public static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
            instanceCSW = DBManagementSystem.getInstance();
        }
        return instance;
    }

    public static Database database;

    public void openDB(String path){
        DatabaseImporter.importDatabase(path);
    }

    public void renameDB(String name){
        if (name != null && !name.isEmpty()) {
            database.setName(name);
            instanceCSW.databaseLabel.setText(database.name);
        }
    }

    public void saveDB(String path) {
        DatabaseExporter.exportDatabase(database, path);
    }

    public void deleteDB() {
        database = null;
        while (instanceCSW.tabbedPane.getTabCount() > 0) {
            instanceCSW.tabbedPane.removeTabAt(0);
        }
    }

    public void createDB(String name) {
        database = new Database(name);
        instanceCSW.databaseLabel.setText(database.name);
    }

    public boolean existDB(){
        return database != null;
    }

    public void addTable(String name){
        if (name != null && !name.isEmpty()) {
            JPanel tablePanel = instanceCSW.createTablePanel();

            DBManagementSystem.getInstance().tabbedPane.addTab(name, tablePanel);
            Table table = new Table(name);
            database.addTable(table);
        }
    }

    public void renameTable(int tableIndex, String name){
        if (name != null && !name.isEmpty() && tableIndex != -1) {
            instanceCSW.tabbedPane.setTitleAt(tableIndex,name);
            database.tables.get(tableIndex).setName(name);
        }
    }

    public void deleteTable(int tableIndex){

        if (tableIndex != -1) {
            instanceCSW.tabbedPane.removeTabAt(tableIndex);

            database.deleteTable(tableIndex);
        }
    }
    public void addColumn(int tableIndex, String columnName, ColumnType columnType){
        if (columnName != null && !columnName.isEmpty()) {
            if (tableIndex != -1) {
                JPanel tablePanel = (JPanel) instanceCSW.tabbedPane.getComponentAt(tableIndex);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                MyTableModel tableModel = (MyTableModel) table.getModel();

                tableModel.addColumn(columnName + " (" + columnType.name() + ")");

                switch (columnType) {
                    case INT -> {
                        Column columnInt = new IntegerColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnInt);
                    }
                    case REAL -> {
                        Column columnReal = new RealColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnReal);
                    }
                    case STRING -> {
                        Column columnStr = new StringColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnStr);
                    }
                    case CHAR -> {
                        Column columnChar = new CharColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnChar);
                    }
                    case EMAIL -> {
                        Column emailColumn = new EmailColumn(columnName);
                        database.tables.get(tableIndex).addColumn(emailColumn);
                    }
                    case ENUM -> {
                        Column enumColumn = new EnumColumn(columnName);
                        database.tables.get(tableIndex).addColumn(enumColumn);
                    }
                }
                for (Row row: database.tables.get(tableIndex).rows) {
                    row.values.add("");
                }

            }
        }
    }

    public void renameColumn(int tableIndex, int columnIndex, String columnName, JTable table){
        if (columnName != null && !columnName.isEmpty()) {
            if (tableIndex != -1 && columnIndex != -1) {
                table.getColumnModel().getColumn(columnIndex).setHeaderValue(columnName + " (" + database.tables.get(tableIndex).columns.get(columnIndex).type + ")");
                table.getTableHeader().repaint();

                database.tables.get(tableIndex).columns.get(columnIndex).setName(columnName);
            }
        }
    }
    public void changeColumnType(int tableIndex, int columnIndex, ColumnType columnType, JTable table){
        if (tableIndex != -1 && columnIndex != -1) {
            table.getColumnModel().getColumn(columnIndex).setHeaderValue(database.tables.get(tableIndex).columns.get(columnIndex).name + " (" + columnType.name() + ")");
            table.getTableHeader().repaint();

            switch (columnType) {
                case INT -> {
                    Column columnInt = new IntegerColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnInt);
                }
                case REAL -> {
                    Column columnReal = new RealColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnReal);
                }
                case STRING -> {
                    Column columnStr = new StringColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnStr);
                }
                case CHAR -> {
                    Column columnChar = new CharColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnChar);
                }
                case EMAIL -> {
                    Column emailColumn = new EmailColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, emailColumn);
                }
                case ENUM -> {
                    Column enumColumn = new EnumColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, enumColumn);
                }
            }
            for (Row row: database.tables.get(tableIndex).rows) {
                row.values.set(columnIndex,"");
            }
            for (int i = 0; i < database.tables.get(tableIndex).rows.size(); i++) {
                table.setValueAt("", i, columnIndex);
            }
        }
    }

    public void deleteColumn(int tableIndex, int columnIndex, MyTableModel tableModel){

        if (columnIndex != -1) {
            tableModel.removeColumn(columnIndex);
            database.tables.get(tableIndex).deleteColumn(columnIndex);
        }
    }

    public void addRow(int tableIndex, Row row){

        if (tableIndex != -1) {
            JPanel tablePanel = (JPanel) instanceCSW.tabbedPane.getComponentAt(tableIndex);
            JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
            JTable table = (JTable) scrollPane.getViewport().getView();
            MyTableModel tableModel = (MyTableModel) table.getModel();
            tableModel.addRow(new Object[tableModel.getColumnCount()]);

            database.tables.get(tableIndex).addRow(row);
            for (int i = row.values.size(); i < database.tables.get(tableIndex).columns.size(); i++){
                row.values.add("");
            }
        }
    }

    public void deleteRow(int tableIndex, int rowIndex, MyTableModel tableModel){

        if (rowIndex != -1) {
            tableModel.removeRow(rowIndex);

            database.tables.get(tableIndex).deleteRow(rowIndex);
        }
    }

    public void updateCellValue(String value, int tableIndex, int columnIndex, int rowIndex, MyTable table){
        if (database.tables.get(tableIndex).columns.get(columnIndex).validate(value)){
            database.tables.get(tableIndex).rows.get(rowIndex).setAt(columnIndex,value.trim());
        }
        else {
            String data = database.tables.get(tableIndex).rows.get(rowIndex).getAt(columnIndex);
            if (data != null){
                table.setValueAt(data, rowIndex, columnIndex);
            }
            else {
                table.setValueAt("", rowIndex, columnIndex);
            }

            JFrame frame = new JFrame("Error!!!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JOptionPane.showMessageDialog(
                    frame,
                    "Invalid value",
                    "Error!!!",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public Boolean sortByColumn(int tableIndex, int columnIndex) {
        if (tableIndex != -1) {
            Table table = database.tables.get(tableIndex);
            switch (ColumnType.valueOf(table.columns.get(columnIndex).type)) {
                case INT -> {
                    Collections.sort(table.rows, (row1, row2) -> {
                        Integer value1 = null;
                        Integer value2 = null;
                        try {
                            value1 = Integer.valueOf(row1.getAt(columnIndex));
                        } catch (NumberFormatException e) {
                            value1 = 0;
                        }
                        try {
                            value2 = Integer.valueOf(row2.getAt(columnIndex));
                        } catch (NumberFormatException e) {
                            value2 = 0;
                        }
                        // For different data types, you might need different comparison logic
                        return value2.compareTo(value1);
                    });
                }
                case REAL -> {
                    Collections.sort(table.rows, (row1, row2) -> {
                        Double value1 = null;
                        Double value2 = null;
                        try {
                            value1 = Double.valueOf(row1.getAt(columnIndex));
                        } catch (NumberFormatException e) {
                            value1 = (double) 0;
                        }
                        try {
                            value2 = Double.valueOf(row2.getAt(columnIndex));
                        } catch (NumberFormatException e) {
                            value2 = (double) 0;
                        }
                        // For different data types, you might need different comparison logic
                        return value2.compareTo(value1);
                    });
                }
                case ENUM, EMAIL, CHAR, STRING -> {
                    Collections.sort(table.rows, (row1, row2) -> {
                        String value1 = row1.getAt(columnIndex);
                        String value2 = row2.getAt(columnIndex);
                        // For different data types, you might need different comparison logic
                        return value2.compareTo(value1);
                    });
                }
            }
        }
        return true;
    }

}
