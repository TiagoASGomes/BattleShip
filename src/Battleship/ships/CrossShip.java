package Battleship.ships;

import Battleship.ships.ship_parts.ShipPart;

import java.util.ArrayList;

public class CrossShip extends Ship {

    public CrossShip() {
        super(ShipType.CROSS_SHIP.getSIZE(), ShipType.CROSS_SHIP);
    }

    @Override
    public void setPosition(int row, int col) {
        shipParts = new ArrayList<>();

        place(row, col);
        placed = true;
    }

    private void place(int row, int col) {
        for (int i = -1; i < 2; i++) {
            shipParts.add(new ShipPart(row, col + i));
        }
        shipParts.add(new ShipPart(row - 1, col));
        shipParts.add(new ShipPart(row + 1, col));
    }
}
