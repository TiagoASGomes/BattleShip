package Battleship.ships;

public class ShipFactory {

    public static StraightShip create(ShipType type) {

        return switch (type) {
            case BIG_SHIP -> new BigShip();
            case CARRIER -> new Carrier();
            case CRUISER -> new Cruiser();
            case DESTROYER -> new Destroyer();
            case SUBMARINE -> new Submarine();

        };
    }
}
