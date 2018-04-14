package lt.liudas_stonys.laivu_musis.services;

import lt.liudas_stonys.laivu_musis.entities.*;

import java.util.Arrays;
import java.util.List;

public class LocalGameService {

    public static final int SHIPYARD_SIZE = 10;

    private static final String[] COLUMN = {"K", "I", "L", "O", "M", "E", "T", "R", "A", "S"};
    private static final Integer[] ROW = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    private Ship[] shipyard = {
            new Ship(createCoordinate("I",2), createCoordinate("M",2)),

            new Ship(createCoordinate("I",4), createCoordinate("O",4)),
            new Ship(createCoordinate("T",8), createCoordinate("A",8)),

            new Ship(createCoordinate("I",6), createCoordinate("L",6)),
            new Ship(createCoordinate("R",2), createCoordinate("A",2)),
            new Ship(createCoordinate("R",6), createCoordinate("A",6)),

            new Ship(createCoordinate("I",8), createCoordinate("I",8)),
            new Ship(createCoordinate("T",4), createCoordinate("T",4)),
            new Ship(createCoordinate("A",4), createCoordinate("A",4)),
            new Ship(createCoordinate("E",6), createCoordinate("E",6)),
    };

    public void updateGameBoard(String[][] board, List<Event> events, String turnForUserId) {
        for (Event event : events) {
            if (turnForUserId.equals(event.getUserId())) {
                if (event.isHit()) {
                    board[event.getCoordinate().getColumn()][event.getCoordinate().getRow()] = Board.HIT;
                } else {
                    board[event.getCoordinate().getColumn()][event.getCoordinate().getRow()] = Board.FIRED;
                }
            }
        }
    }

    public void placeShipsOnBoard(String[][] board, Ship[] shipyard) {
        for (Ship ship : shipyard) {
            placeShipOnBoard(board, ship);
        }
    }
    private void placeShipOnBoard(String[][] board, Ship ship) {
        if (isShipVertical(ship)) {
            for (int i = ship.getStart().getRow(); i <= ship.getEnd().getRow(); i++) {
                board[ship.getStart().getColumn()][i] = Board.SHIP;
            }
        } else {
            for (int i = ship.getStart().getColumn(); i <= ship.getEnd().getColumn(); i++) {
                board[i][ship.getStart().getRow()] = Board.SHIP;
            }
        }
    }
    private boolean isShipVertical(Ship ship) {
        return ship.getStart().getColumn() == ship.getEnd().getColumn();
    }

    public void checkIfIveShotHere (Coordinate coordinate, List<Coordinate> enemyBoardsUsedCoordinates) throws Exception {
        if (enemyBoardsUsedCoordinates.contains(coordinate)) {
            throw new Exception("You have already shot to this field!");
        }
    }

    public String checkIfCoordinateIsValid(String coordinate) throws Exception {
        if (coordinate.length() > 2) {
            throw new Exception("Coordinates must contain only two characters!");
        }
        if (!Arrays.asList(COLUMN).contains(Character.toString(coordinate.charAt(0)))) {
            throw new Exception("Invalid X Coordinate!");
        }
        if (!Arrays.asList(ROW).contains(Character.getNumericValue(coordinate.charAt(1)))) {
            throw new Exception("Invalid Y Coordinate!");
        }
        return coordinate;
    }
    public boolean checkCoordinateIntersection(Coordinate testCoordinate, List<Coordinate> usedCoordinates) {
        for (Coordinate usedCoordinate : usedCoordinates) {
            if (checkTwoCoordinatesIntersection(testCoordinate, usedCoordinate)) {
                return true;
            }
        }
        return false;
    }
    private boolean checkTwoCoordinatesIntersection(Coordinate testCoordinate, Coordinate usedCoordinate) {
        int testX = testCoordinate.getRow();
        int testY = testCoordinate.getColumn();

        int usedX = usedCoordinate.getRow();
        int usedY = usedCoordinate.getColumn();

        if (testX == usedX && (testY == usedY || testY - 1 == usedY || testY + 1 == usedY) ||
                testY == usedY && (testX - 1 == usedX || testX + 1 == usedX) ||
                testX - 1 == usedX && testY - 1 == usedY ||
                testX + 1 == usedX && testY - 1 == usedY ||
                testX + 1 == usedX && testY + 1 == usedY ||
                testX - 1 == usedX && testY + 1 == usedY) {
            return true;
        }
        return false;
    }

    public Coordinate parseCoordinate(String coordinate) throws Exception {
        Integer row;
        String columnAsString = coordinate.substring(0, 1);
        String rowAsString = coordinate.substring(1, 2);
        try {
            row = Integer.parseInt(rowAsString);
        } catch (Exception error) {
            throw new Exception("Coordinates must contain only two characters!");
        }

        return createCoordinate(columnAsString, row);
    }
    public Coordinate createCoordinate(String columnAsString, Integer row) {
        return new Coordinate(columnAsString, getCoordinateFromString(columnAsString),row);
    }
    private int getCoordinateFromString(String str) {
        for (int i = 0; i < COLUMN.length; i++) {
            if (COLUMN[i].equalsIgnoreCase(str)) {
                return i;
            }
        }
        return -1;
    }
    public String getCoordinateFromInt(int index) {
        return COLUMN[index];
    }

    // Getter
    public Ship[] getShipyard() {
        return shipyard;
    }

    // Setter
    public void setShipyard(Ship[] shipyard) {
        this.shipyard = shipyard;
    }
}