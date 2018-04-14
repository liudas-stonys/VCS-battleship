package lt.liudas_stonys.laivu_musis.entities;

public class Coordinate {

    public static final int START = 0;
    public static final int END = 1;

    private String columnAsString;
    private Integer column;
    private Integer row;

    public Coordinate(String columnAsString, Integer column, Integer row) {
        this.columnAsString = columnAsString;
        this.row = row;
        this.column = column;
    }

    // Getter
    public String getColumnAsString() {
        return columnAsString;
    }
    public Integer getColumn() {
        return column;
    }
    public Integer getRow() {
        return row;
    }

    // Setter
    public void setColumnAsString(String columnAsString) {
        this.columnAsString = columnAsString;
    }
    public void setColumn(Integer column) {
        this.column = column;
    }
    public void setRow(Integer row) {
        this.row = row;
    }
}
