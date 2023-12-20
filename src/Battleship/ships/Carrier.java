package Battleship.ships;

public class Carrier extends Ship {
    /**
     * Constructs a new Carrier ship.
     * Initializes the ship with the size and type specific to Carrier.
     */
    public Carrier() {
        super(ShipType.CARRIER.getSIZE(), ShipType.CARRIER);
    }
}
