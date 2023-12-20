package Battleship.ships;


public enum ShipType {
    BIG_SHIP(5, "B"),
    CARRIER(4, "C"),
    SUBMARINE(3, "S"),
    DESTROYER(2, "D"),
    CRUISER(4, "Z"),
    CROSS_SHIP(5, "K"),
    BATTLECRUISER(5, "P");
    private final int SIZE;
    private final String ICON;

    /**
     * Constructs a ShipType with the specified size and icon.
     *
     * @param SIZE The size of the ship type.
     * @param ICON The icon representing the ship type.
     */
    ShipType(int SIZE, String ICON) {
        this.SIZE = SIZE;
        this.ICON = ICON;
    }

    /**
     * Gets the size of the ship type.
     *
     * @return The size of the ship type.
     */
    public int getSIZE() {
        return SIZE;
    }

    /**
     * Gets the icon representing the ship type.
     *
     * @return The icon representing the ship type.
     */
    public String getICON() {
        return ICON;
    }
}
