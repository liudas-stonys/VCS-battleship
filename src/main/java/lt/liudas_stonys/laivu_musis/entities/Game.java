package lt.liudas_stonys.laivu_musis.entities;

import java.util.List;

public class Game {

    private String status;
    private List<String> columns;
    private List<Integer> rows;
    private String gameId;

    private String nextTurnForUserId;
    private String winnerUserId;
    private List<Event> events;

    public Game(List<String> columns, List<Integer> rows, String gameId, String nextTurnForUserId, String status, String winnerUserId, List<Event> events) {
        this.columns = columns;
        this.rows = rows;
        this.gameId = gameId;
        this.nextTurnForUserId = nextTurnForUserId;
        this.status = status;
        this.winnerUserId = winnerUserId;
        this.events = events;
    }

    // Getter
    public List<Event> getEvents() {
        return events;
    }
    public List<String> getColumns() {
        return columns;
    }
    public List<Integer> getRows() {
        return rows;
    }

    public String getStatus() {
        return status;
    }
    public String getGameId() {
        return gameId;
    }
    public String getNextTurnForUserId() {
        return nextTurnForUserId;
    }
    public String getWinnerUserId() {
        return winnerUserId;
    }

    // Setter
    public void setEvents(List<Event> events) {
        this.events = events;
    }
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
    public void setRows(List<Integer> rows) {
        this.rows = rows;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    public void setNextTurnForUserId(String nextTurnForUserId) {
        this.nextTurnForUserId = nextTurnForUserId;
    }
    public void setWinnerUserId(String winnerUserId) {
        this.winnerUserId = winnerUserId;
    }
}
