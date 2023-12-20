package Battleship;

public enum PointValues {
    HIT(1),
    SINK(2),
    MINE(0),
    SONAR(5),
    BOMB(4),
    SPECIAL(0);

    private final int points;

    /**
     * Constructor method of the enum PointValues that accepts one argument,
     *
     * @param points receives points as a parameter.
     */
    PointValues(int points) {
        this.points = points;

    }

    /**
     * Getter for PointValues,
     *
     * @return points.
     */
    public int getPoints() {
        return points;
    }

}
