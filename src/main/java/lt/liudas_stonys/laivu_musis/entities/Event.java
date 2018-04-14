package lt.liudas_stonys.laivu_musis.entities;

public class Event {

    private Long date;
    private Coordinate coordinate;
    private String userId;
    private Boolean hit;

    public Event(Long date, Coordinate coordinate, String userId, Boolean hit) {
        this.date = date;
        this.coordinate = coordinate;
        this.userId = userId;
        this.hit = hit;
    }

    // Getter
    public Long getDate() {
        return date;
    }
    public Coordinate getCoordinate() {
        return coordinate;
    }
    public String getUserId() {
        return userId;
    }
    public boolean isHit() {
        return hit;
    }

    // Setter
    public void setDate(Long date) {
        this.date = date;
    }
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
