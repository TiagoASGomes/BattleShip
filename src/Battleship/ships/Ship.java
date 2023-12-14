package Battleship.ships;

public interface Ship {
    void setPosition(int line, int column);

    void rotate();

    boolean gotHit(int targetRow, int targetCol);
}
