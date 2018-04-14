package lt.liudas_stonys.laivu_musis.services;

import lt.liudas_stonys.laivu_musis.entities.Coordinate;
import lt.liudas_stonys.laivu_musis.entities.Game;
import lt.liudas_stonys.laivu_musis.entities.Ship;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WebGameService extends WebService {

    private static final String JOIN_GAME_METHOD = "join?";
    private static final String SETUP_GAME_METHOD = "setup?";
    private static final String SEND_TURN_METHOD = "turn?";
    private static final String GET_STATUS_METHOD = "status?";

    public void waitForGameStatusChange(Game game, String statusToChangeTo) throws IOException, ParseException, InterruptedException {
        System.out.print("\nWaiting for server response...");
        while (!statusToChangeTo.equals(game.getStatus())) {
            System.out.print(".");
            game = getGameStatus(game);
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(game.getStatus());
    }

    public Game joinGame(String userId) throws IOException, ParseException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append(JOIN_GAME_METHOD);
        url.append("user_id=").append(userId);

        return performHttpRequest(url.toString());
    }

    public Game setupGame(String gameId, String userId, Ship[] shipyard) throws IOException, ParseException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append(SETUP_GAME_METHOD);
        url.append("game_id=").append(gameId).append("&");
        url.append("user_id=").append(userId).append("&");
        url.append("data=").append(convertToString(shipyard));

        return performHttpRequest(url.toString());
    }
    private String convertToString(Ship[] shipyard) {
        StringBuilder result = new StringBuilder();
        for (Ship ship : shipyard) {
            result.append(ship.getStart().getColumnAsString()).append(ship.getStart().getRow()).append("-");
            result.append(ship.getEnd().getColumnAsString()).append(ship.getEnd().getRow()).append("!");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public Game sendMyTurn(String gameId, String userId, Coordinate attackCoordinate) throws IOException, ParseException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append(SEND_TURN_METHOD);
        url.append("game_id=").append(gameId).append("&");
        url.append("user_id=").append(userId).append("&");
        url.append("data=").append(attackCoordinate.getColumnAsString()).append(attackCoordinate.getRow());

        return performHttpRequest(url.toString());
    }

    public Game getGameStatus(Game game) throws IOException, ParseException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append(GET_STATUS_METHOD);
        url.append("game_id=").append(game.getGameId());

        return performHttpRequest(url.toString());
    }
}
