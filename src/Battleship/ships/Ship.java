package Battleship.ships;

import Battleship.ships.ship_parts.ShipPart;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship implements ShipI {

    protected List<ShipPart> shipParts;
    protected boolean isRotated;
    protected final int SIZE;
    private final ShipType type;
    private boolean sinked;
    protected boolean placed;

    /**
     * Constructs a new Ship with the specified size and type.
     *
     * @param size The size of the ship.
     * @param type The type of the ship.
     */
    public Ship(int size, ShipType type) {
        isRotated = false;
        this.SIZE = size;
        this.type = type;
        sinked = false;
        placed = false;
    }

    /**
     * Sets the position of the ship on the game grid.
     * Abstract method to be implemented by concrete ship classes.
     * Initializes the ship's parts based on the specified row and column coordinates.
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
     * Places the ship horizontally on the game grid.
     * Initializes the ship's parts in a horizontal arrangement based on the specified row and column coordinates.
     *
     * @param row The row position where the leftmost part of the ship will be placed.
     * @param col The column position where the leftmost part of the ship will be placed.
     */
    private void placeHorizontal(int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            shipParts.add(new ShipPart(row, col + i));
        }
    }

    /**
     * Places the ship vertically on the game grid.
     * Initializes the ship's parts in a vertical arrangement based on the specified row and column coordinates.
     *
     * @param row The row position where the topmost part of the ship will be placed.
     * @param col The column position where the topmost part of the ship will be placed.
     */
    private void placeVertical(int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            shipParts.add(new ShipPart(row + i, col));
        }
    }

    /**
     * Rotates the ship.
     * Changes the orientation of the ship from horizontal to vertical or vice versa.
     * Overrides the rotate method in the ShipI interface.
     */
    @Override
    public void rotate() {
        isRotated = true;
    }

    /**
     * Checks if the ship got hit at the specified target coordinates.
     * Overrides the gotHit method in the ShipI interface.
     *
     * @param targetRow The row position of the target.
     * @param targetCol The column position of the target.
     * @return True if the ship got hit, false otherwise.
     */
    @Override
    public boolean gotHit(int targetRow, int targetCol) {
        for (ShipPart shipPart : shipParts) {
            if (shipPart.getRow() == targetRow && shipPart.getCol() == targetCol) {
                shipPart.setHit();
                checkSinked();
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the ship is sunk (all parts are hit).
     * If all ship parts are hit, sets the 'sinked' flag to true.
     */
    private void checkSinked() {
        for (ShipPart shipPart : shipParts) {
            if (!shipPart.isHit()) {
                return;
            }
        }
        sinked = true;
    }

    /**
     * Removes the ship from the game grid.
     * Resets shipParts to null and placed to false.
     */
    public void removeShip() {
        shipParts = null;
        placed = false;
    }

    /**
     * Gets the type of the ship.
     *
     * @return The type of the ship.
     */
    public ShipType getType() {
        return type;
    }

    /**
     * Checks if the ship is sunk.
     *
     * @return True if the ship is sunk, false otherwise.
     */
    public boolean isSinked() {
        return sinked;
    }

    /**
     * Checks if the ship is placed on the game grid.
     *
     * @return True if the ship is placed, false otherwise.
     */
    public boolean isPlaced() {
        return placed;
    }

    /**
     * Gets the list of ShipPart objects representing the individual parts of the ship.
     *
     * @return The list of ShipPart objects.
     */
    public List<ShipPart> getShipParts() {
        return shipParts;
    }

    /**
     * Checks if the ship is rotated.
     *
     * @return True if the ship is rotated, false otherwise.
     */
    public boolean isRotated() {
        return isRotated;
    }
}
