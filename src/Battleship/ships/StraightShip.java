package Battleship.ships;

import Battleship.ships.ship_parts.ShipPart;

import java.util.ArrayList;
import java.util.List;

public abstract class StraightShip implements Ship {

    protected final List<ShipPart> shipParts = new ArrayList<>();
    protected boolean isRotated;
    protected final int SIZE;
    private final ShipType type;
    private boolean sinked;

    public StraightShip(int size, ShipType type) {
        isRotated = false;
        this.SIZE = size;
        this.type = type;
        sinked = false;
    }

    @Override
    public void setPosition(int row, int col) {
        if (isRotated) {
            placeVertical(row, col);
            return;
        }
        placeHorizontal(row, col);
    }

    private void placeHorizontal(int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            shipParts.add(new ShipPart(row, col + i));
        }
    }

    private void placeVertical(int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            shipParts.add(new ShipPart(row + i, col));
        }
    }

    @Override
    public void rotate() {
        isRotated = true;
    }

    @Override
    public boolean gotHit(int targetRow, int targetCol) {
        for (ShipPart shipPart : shipParts) {
            if (shipPart.getRow() == targetRow && shipPart.getCol() == targetCol) {
                shipPart.gotHit();
                checkSinked();
                return true;
            }
        }
        return false;
    }

    private void checkSinked() {
        for (ShipPart shipPart : shipParts) {
            if (!shipPart.isHit()) {
                return;
            }
        }
        sinked = true;
    }

    public ShipType getType() {
        return type;
    }

    public boolean isSinked() {
        return sinked;
    }
}
