package Battleship.Character;

import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;

public class CharacterOne extends Character {


    public CharacterOne(ShipType[] ships) {
        super(CharacterType.ONE);

        for (ShipType shipType : ships) {
            playerShips.add(ShipFactory.create(shipType));
        }
    }
}
