package Battleship.ships;

import Battleship.ships.ship_parts.ShipPart;

import java.util.ArrayList;

public class BattleCruiser extends Ship {

    public BattleCruiser() {
        super(ShipType.BATTLECRUISER.getSIZE(), ShipType.BATTLECRUISER);
    }

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

    private void placeHorizontal(int row, int col) {
        for (int i = 0; i < 3; i++) {
            shipParts.add(new ShipPart(row, col + i));
        }
        shipParts.add(new ShipPart(row + 1, col + 1));
        shipParts.add(new ShipPart(row + 1, col + 2));
    }

    private void placeVertical(int row, int col) {
        for (int i = 0; i < 3; i++) {
            shipParts.add(new ShipPart(row + i, col));
        }
        shipParts.add(new ShipPart(row + 1, col - 1));
        shipParts.add(new ShipPart(row + 2, col - 1));
    }

}
