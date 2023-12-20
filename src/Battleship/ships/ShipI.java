package Battleship.ships;

public interface ShipI {
    /**
     * Sets the position of the ship on the game grid.
     *
     * @param line   The line position where the ship will be placed.
     * @param column The column position where the ship will be placed.
     */
    void setPosition(int line, int column);

    /**
     * Rotates the ship.
     * Changes the orientation of the ship from horizontal to vertical or vice versa.
     */
    void rotate();

    /**
     * Checks if the ship got hit at the specified target coordinates.
     *
     * @param targetRow The row position of the target.
     * @param targetCol The column position of the target.
     * @return True if the ship got hit, false otherwise.
     */
    boolean gotHit(int targetRow, int targetCol);
}
