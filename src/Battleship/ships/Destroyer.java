package Battleship.ships;

public class Destroyer extends Ship {
    /**
     * Constructs a new Destroyer ship.
     * Initializes the ship with the size and type specific to Destroyer.
     */
    public Destroyer() {
        super(ShipType.DESTROYER.getSIZE(), ShipType.DESTROYER);
    }
}
