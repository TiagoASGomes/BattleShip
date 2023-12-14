package Battleship.Character;

import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;

public class CharacterTwo extends Character {

    public CharacterTwo(ShipType[] ships) {
        super(CharacterType.TWO);

        for (ShipType shipType : ships) {
            playerShips.add(ShipFactory.create(shipType));
        }
    }
}
