package Battleship;

public enum PointCosts {
    HIT(1),
    SINK(2),
    MINE(2),
    SONAR(5),
    BOMB(4),
    SPECIAL(7);

    private final int pointCost;

    PointCosts(int pointCost) {
        this.pointCost = pointCost;

    }

    public int getPointCost() {
        return pointCost;
    }

}
