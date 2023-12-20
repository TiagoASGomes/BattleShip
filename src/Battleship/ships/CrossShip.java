package Battleship.ships;

import Battleship.ships.ship_parts.ShipPart;

import java.util.ArrayList;

public class CrossShip extends Ship {
    /**
     * Constructs a new CrossShip.
     * Initializes the ship with the size and type specific to CrossShip.
     */
    public CrossShip() {
        super(ShipType.CROSS_SHIP.getSIZE(), ShipType.CROSS_SHIP);
    }

    /**
     * Sets the position of the CrossShip on the game grid.
     * Overrides the setPosition method in the Ship class to define the specific positioning logic for CrossShip.
     *
     * @param row The row position where the ship will be placed.
     * @param col The column position where the ship will be placed.
     */
    @Override
    public void setPosition(int row, int col) {
        shipParts = new ArrayList<>();

        place(row, col);
        placed = true;
    }

    /**
     * Places the CrossShip on the game grid.
     *
     * @param row The row position where the center of the ship will be placed.
     * @param col The column position where the center of the ship will be placed.
     */
    private void place(int row, int col) {
        for (int i = -1; i < 2; i++) {
            shipParts.add(new ShipPart(row, col + i));
        }
        shipParts.add(new ShipPart(row - 1, col));
        shipParts.add(new ShipPart(row + 1, col));
    }
}
