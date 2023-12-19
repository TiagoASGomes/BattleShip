package Battleship.Character;

import Battleship.ships.ShipType;

public class CharacterFactory {

    public static Character create(CharacterType type) {

        return switch (type) {
            case ONE -> new CharacterOne(new ShipType[]{ShipType.BIG_SHIP});
            case TWO -> new CharacterTwo(new ShipType[]{ShipType.BIG_SHIP});
            case THREE -> new CharacterThree(new ShipType[]{ShipType.BIG_SHIP});
        };
    }
}
