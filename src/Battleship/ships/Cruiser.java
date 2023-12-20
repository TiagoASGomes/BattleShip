package Battleship.ships;

import Battleship.ships.ship_parts.ShipPart;

import java.util.ArrayList;

public class Cruiser extends Ship {
    /**
     * Constructs a new Cruiser ship.
     * Initializes the ship with the size and type specific to Cruiser.
     */
    public Cruiser() {
        super(ShipType.CRUISER.getSIZE(), ShipType.CRUISER);
    }

    /**
     * Sets the position of the Cruiser ship on the game grid.
     * Overrides the setPosition method in the Ship class to define the specific positioning logic for Cruiser.
     *
     * @param row The row position where the ship will be placed.
     * @param col The column position where the ship will be placed.
     */
    @Override
    public void setPosition(int row, int col) {
        shipParts = new ArrayList<>();
        if (isRotated) {
            placeVertical(row, col);
            placed = true;
            return;
        }
        placeHorizontal(row, col);
        placed = true;
    }

    /**
     * Places the Cruiser ship horizontally on the game grid.
     *
     * @param row The row position where the leftmost part of the ship will be placed.
     * @param col The column position where the leftmost part of the ship will be placed.
     */
    private void placeHorizontal(int row, int col) {
        shipParts.add(new ShipPart(row, col));
        shipParts.add(new ShipPart(row, col + 1));
        shipParts.add(new ShipPart(row + 1, col + 1));
        shipParts.add(new ShipPart(row + 1, col + 2));
    }

    /**
     * Places the Cruiser ship vertically on the game grid.
     *
     * @param row The row position where the topmost part of the ship will be placed.
     * @param col The column position where the topmost part of the ship will be placed.
     */
    private void placeVertical(int row, int col) {
        shipParts.add(new ShipPart(row, col));
        shipParts.add(new ShipPart(row + 1, col));
        shipParts.add(new ShipPart(row + 1, col - 1));
        shipParts.add(new ShipPart(row + 2, col - 1));
    }
}
