package Battleship.ships;

public class BigShip extends Ship {
    /**
     * Constructs a new BigShip.
     * Initializes the ship with the size and type specific to BigShip.
     */
    public BigShip() {
        super(ShipType.BIG_SHIP.getSIZE(), ShipType.BIG_SHIP);
    }
}
