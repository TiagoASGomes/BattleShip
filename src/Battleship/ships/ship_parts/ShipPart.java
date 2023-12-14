package Battleship.ships.ship_parts;

public class ShipPart {
    private final int row;
    private final int col;
    private boolean isHit;

    public ShipPart(int row, int col) {
        this.row = row;
        this.col = col;
    }


    public void gotHit() {
        isHit = true;
    }

    public boolean isHit() {
        return isHit;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
