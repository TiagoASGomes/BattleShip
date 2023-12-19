package Battleship;

public enum PointValues {
    HIT(1),
    SINK(2),
    MINE(0),
    SONAR(5),
    BOMB(4),
    SPECIAL(7);

    private final int points;

    PointValues(int points) {
        this.points = points;

    }

    public int getPoints() {
        return points;
    }

}
