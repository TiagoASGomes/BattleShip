package Battleship.ships;

import Battleship.ships.ship_parts.ShipPart;

public class Cruiser extends StraightShip {

    public Cruiser() {
        super(ShipType.CRUISER.getSIZE(), ShipType.CRUISER);
    }

    @Override
    public void setPosition(int row, int col) {
        if (isRotated) {
            placeVertical(row, col);
            placed = true;
            return;
        }
        placeHorizontal(row, col);
        placed = true;
    }

    private void placeHorizontal(int row, int col) {
        shipParts.add(new ShipPart(row, col));
        shipParts.add(new ShipPart(row, col + 1));
        shipParts.add(new ShipPart(row + 1, col + 1));
        shipParts.add(new ShipPart(row + 1, col + 2));
    }

    private void placeVertical(int row, int col) {
        shipParts.add(new ShipPart(row, col));
        shipParts.add(new ShipPart(row + 1, col));
        shipParts.add(new ShipPart(row + 1, col - 1));
        shipParts.add(new ShipPart(row + 2, col - 1));
    }
}
