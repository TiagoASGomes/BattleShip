package Battleship.Character;

import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;

import java.util.ArrayList;

public class CharacterOne extends Character {


    public CharacterOne(ShipType[] ships) {
        super(CharacterType.ONE);
        playerShips = new ArrayList<>();

        for (ShipType shipType : ships) {
            playerShips.add(ShipFactory.create(shipType));
        }
    }
}
