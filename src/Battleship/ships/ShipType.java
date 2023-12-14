package Battleship.ships;

public enum ShipType {
    BIG_SHIP(5),
    CARRIER(4),
    SUBMARINE(3),
    DESTROYER(2),
    CRUISER(4);

    private final int SIZE;

    ShipType(int SIZE) {
        this.SIZE = SIZE;
    }

    public int getSIZE() {
        return SIZE;
    }
}
