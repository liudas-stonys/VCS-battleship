package lt.liudas_stonys.laivu_musis;

import lt.liudas_stonys.laivu_musis.entities.*;
import lt.liudas_stonys.laivu_musis.services.LocalGameService;
import lt.liudas_stonys.laivu_musis.services.UserService;
import lt.liudas_stonys.laivu_musis.services.WebGameService;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {

    private static Scanner input = new Scanner(System.in);
    private static UserService userService = new UserService();
    private static WebGameService webGameService = new WebGameService();
    private static LocalGameService localGameService = new LocalGameService();

    private static List<Coordinate> myBoardsUsedCoordinates = new ArrayList<>();
    private static List<Coordinate> enemyBoardsUsedCoordinates = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Board myBoard = new Board();
        Board enemyBoard = new Board();
        Ship[] shipyard = new Ship[LocalGameService.SHIPYARD_SIZE];
        Coordinate[] shipCoordinates = new Coordinate[2];

        User user = userService.createUser("Erikas", "erikas69@gmail.com");
        System.out.printf("\nHello, %s!\n", user.getName());

        Game game = webGameService.joinGame(user.getUserId());
        System.out.printf("\nGame id: %s\n", game.getGameId());
        webGameService.waitForGameStatusChange(game, "READY_FOR_SHIPS");

        for (int shipNum = 0; shipNum < LocalGameService.SHIPYARD_SIZE; shipNum++) {
             shipCoordinates = readShipFromTheConsole(shipCoordinates, shipNum);
             shipyard[shipNum] = new Ship(shipCoordinates[Coordinate.START], shipCoordinates[Coordinate.END]);
        }

        webGameService.setupGame(game.getGameId(), user.getUserId(), shipyard);
        webGameService.waitForGameStatusChange(game,"READY_TO_PLAY");

        localGameService.placeShipsOnBoard(myBoard.getBoard(), shipyard);
        game = webGameService.getGameStatus(game);

        int myTurnCount = 0;
        int enemyTurnCount = 0;

        while (!game.getStatus().equals("FINISHED")) {
            String turnForUserId = game.getNextTurnForUserId();

            if (turnForUserId.equals(user.getUserId())) {
                myTurnCount++;
                System.out.printf("\n%s, it's your %s turn. Destroy some ships, mate!", user.getName(), myTurnCount);
                System.out.print("\nEnter wich square is doomed: ");
                Coordinate attackCoordinate = askUserInput();
                enemyBoardsUsedCoordinates.add(attackCoordinate);

                webGameService.sendMyTurn(game.getGameId(), user.getUserId(), attackCoordinate);
                game = webGameService.getGameStatus(game);
                localGameService.updateGameBoard(enemyBoard.getBoard(), game.getEvents(), turnForUserId);

                printGameBoard(myBoard.getBoard(), enemyBoard.getBoard(), game.getColumns(), game.getRows());
            }
            else {
                enemyTurnCount++;
                System.out.printf("\nBot's %s turn!\n", enemyTurnCount);
                while (turnForUserId.equals(game.getNextTurnForUserId())) {
                    TimeUnit.SECONDS.sleep(2);
                    game = webGameService.getGameStatus(game);
                }
                localGameService.updateGameBoard(myBoard.getBoard(), game.getEvents(), turnForUserId);
                printGameBoard(myBoard.getBoard(), enemyBoard.getBoard(), game.getColumns(), game.getRows());
            }
            TimeUnit.SECONDS.sleep(1);
        }
        if (user.getUserId().equals(game.getWinnerUserId())) {
            System.out.println("\nCongradulations! You have won the Battle of Seven Seas!");
        } else {
            System.out.println("\nYou lost, but don't cry... Not everyone is perfect.");
        }
        input.close();
    }

    public static Coordinate[] readShipFromTheConsole(Coordinate[] shipCoord, int shipNum) {
        System.out.println();
        for (int coordNum = Coordinate.START; coordNum <= Coordinate.END; coordNum++) {
            do {
                try {
                    System.out.print("Enter ship " + (shipNum + 1) + " coordinate " + (coordNum + 1) + ": ");
                    shipCoord[coordNum] = askUserInput();
                    if (localGameService.checkCoordinateIntersection(shipCoord[coordNum], myBoardsUsedCoordinates)) {
                        throw new Exception("Coordinate intersects!");
                    }
                    break;
                } catch (Exception error) {
                    System.out.println("Error: " + error.getMessage() + " Try again.");
                }
            } while (true);
        }
        myBoardsUsedCoordinates.add(shipCoord[Coordinate.START]);
        myBoardsUsedCoordinates.add(shipCoord[Coordinate.END]);
        return shipCoord;
    }

    private static Coordinate askUserInput() {
        Coordinate coordinate;
        do {
            try {
                String coordinateAsString = localGameService.checkIfCoordinateIsValid(input.next().toUpperCase());
                coordinate = localGameService.parseCoordinate(coordinateAsString);
                localGameService.checkIfIveShotHere(coordinate, enemyBoardsUsedCoordinates);
                break;
            } catch (Exception error) {
                System.out.print("Error: " + error.getMessage() + " Try again: ");
            }
        } while (true);
        return coordinate;
    }

    private static void printGameBoard(String[][] myBoard, String[][] enemyBoard, List<String> columns, List<Integer> rows) { // TODO Make as Stringbuilder
        String square = " %-1s ";
        System.out.println();
        System.out.print("============ MY TABLE ===========      ");
        System.out.println("========= OPPONENT'S TABLE ======");
        System.out.printf(square, "");

        // Prints KILOMETRAS
        for (int boardNum = 0; boardNum < Board.QUANTITY; boardNum++) {
            for (String column : columns) {
                System.out.printf(square, column);
            }
            System.out.print("         ");
        }
        for (int coordY = 0; coordY < Board.HEIGHT; coordY++) {
            System.out.println();
            System.out.printf(square, rows.get(coordY));

            // Prints myBoard
            for (int coordX = 0; coordX < Board.WIDTH; coordX++) {
                System.out.printf(square, myBoard[coordX][coordY]);
            }
            System.out.print("      ");
            System.out.printf(square, rows.get(coordY));

            // Prints enemyBoard
            for (int coordX = 0; coordX < Board.WIDTH; coordX++) {
                System.out.printf(square, enemyBoard[coordX][coordY]);
            }
        }
        System.out.println();
    }
}