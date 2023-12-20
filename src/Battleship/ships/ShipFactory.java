package Battleship.ships;

public class ShipFactory {
    /**
     * Creates a new instance of a ship based on the provided ship type.
     *
     * @param type The type of the ship to create.
     * @return A new instance of the specified ship type.
     */
    public static Ship create(ShipType type) {

        return switch (type) {
            case BIG_SHIP -> new BigShip();
            case CARRIER -> new Carrier();
            case CRUISER -> new Cruiser();
            case DESTROYER -> new Destroyer();
            case SUBMARINE -> new Submarine();
            case CROSS_SHIP -> new CrossShip();
            case BATTLECRUISER -> new BattleCruiser();
        };
    }
}
