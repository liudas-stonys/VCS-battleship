package lt.liudas_stonys.laivu_musis.entities;

public class Ship {

    private Coordinate start;
    private Coordinate end;

    public Ship(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

    // Getter
    public Coordinate getStart() {
        return start;
    }
    public Coordinate getEnd() {
        return end;
    }

    // Setter
    public void setStart(Coordinate start) {
        this.start = start;
    }
    public void setEnd(Coordinate end) {
        this.end = end;
    }
}
