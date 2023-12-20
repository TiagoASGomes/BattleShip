package Battleship.ships;

public class Submarine extends Ship {
    /**
     * Constructs a new Submarine ship.
     * Initializes the ship with the size and type specific to Submarine.
     */
    public Submarine() {
        super(ShipType.SUBMARINE.getSIZE(), ShipType.SUBMARINE);
    }
}
