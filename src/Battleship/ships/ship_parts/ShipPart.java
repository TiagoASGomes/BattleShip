package Battleship.ships.ship_parts;


public class ShipPart {

    private final int row;
    private final int col;
    private boolean isHit;

    /**
     * Constructs a new ShipPart with the specified row and column positions.
     *
     * @param row The row position of the ship part on the game grid.
     * @param col The column position of the ship part on the game grid.
     */
    public ShipPart(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Sets the ship part as hit.
     * Marks the ship part to indicate that it has been hit.
     */
    public void setHit() {
        isHit = true;
    }

    /**
     * Checks whether the ship part has been hit.
     *
     * @return True if the ship part has been hit, false otherwise.
     */
    public boolean isHit() {
        return isHit;
    }

    /**
     * Retrieves the row position of the ship part on the game grid.
     *
     * @return The row position of the ship part.
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieves the column position of the ship part on the game grid.
     *
     * @return The column position of the ship part.
     */
    public int getCol() {
        return col;
    }
}
