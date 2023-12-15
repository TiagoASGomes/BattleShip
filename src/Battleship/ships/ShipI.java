package Battleship.ships;

public interface ShipI {
    void setPosition(int line, int column);

    void rotate();

    boolean gotHit(int targetRow, int targetCol);
}
