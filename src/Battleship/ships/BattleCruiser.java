package Battleship.ships;

import Battleship.ships.ship_parts.ShipPart;

import java.util.ArrayList;


public class BattleCruiser extends Ship {
    /**
     * Constructs a new BattleCruiser ship.
     * Initializes the ship with the size and type specific to BattleCruiser.
     */
    public BattleCruiser() {
        super(ShipType.BATTLECRUISER.getSIZE(), ShipType.BATTLECRUISER);
    }

    /**
     * Sets the position of the BattleCruiser ship on the game grid.
     * Overrides the setPosition method in the Ship class to define the specific positioning logic for BattleCruiser.
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
     * Places the BattleCruiser ship horizontally on the game grid.
     *
     * @param row The row position where the leftmost part of the ship will be placed.
     * @param col The column position where the leftmost part of the ship will be placed.
     */
    private void placeHorizontal(int row, int col) {
        for (int i = 0; i < 3; i++) {
            shipParts.add(new ShipPart(row, col + i));
        }
        shipParts.add(new ShipPart(row + 1, col + 1));
        shipParts.add(new ShipPart(row + 1, col + 2));
    }

    /**
     * Places the BattleCruiser ship vertically on the game grid.
     *
     * @param row The row position where the topmost part of the ship will be placed.
     * @param col The column position where the topmost part of the ship will be placed.
     */
    private void placeVertical(int row, int col) {
        for (int i = 0; i < 3; i++) {
            shipParts.add(new ShipPart(row + i, col));
        }
        shipParts.add(new ShipPart(row + 1, col - 1));
        shipParts.add(new ShipPart(row + 2, col - 1));
    }

}
