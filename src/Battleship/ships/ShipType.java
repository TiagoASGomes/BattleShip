package Battleship.ships;

public enum ShipType {
    BIG_SHIP(5, "B"),
    CARRIER(4, "C"),
    SUBMARINE(3, "S"),
    DESTROYER(2, "D"),
    CRUISER(4, "Z");

    private final int SIZE;
    private final String ICON;

    ShipType(int SIZE, String ICON) {
        this.SIZE = SIZE;
        this.ICON = ICON;
    }

    public int getSIZE() {
        return SIZE;
    }

    public String getICON() {
        return ICON;
    }
}
