package lt.liudas_stonys.laivu_musis.entities;

public class Board {

    public static final String EMPTY = "~";
    public static final String SHIP = "#";
    public static final String FIRED = "o";
    public static final String HIT = "x";

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    public static final int QUANTITY = 2;

    private String[][] board = new String[WIDTH][HEIGHT];

    public Board() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = Board.EMPTY;
            }
        }
    }

    // Getter
    public String[][] getBoard() {
        return board;
    }

    // Setter
    public void setBoard(String[][] board) {
        this.board = board;
    }
}
