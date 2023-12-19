package Battleship.Character;

import Battleship.ships.ShipType;

public class CharacterFactory {

    public static Character create(CharacterType type) {

        return switch (type) {
            case ONE ->
                    new CharacterOne(new ShipType[]{ShipType.BIG_SHIP, ShipType.CRUISER, ShipType.CARRIER, ShipType.DESTROYER, ShipType.SUBMARINE});
            case TWO -> new CharacterTwo(new ShipType[]{ShipType.DESTROYER});
            case THREE -> new CharacterThree(new ShipType[]{ShipType.DESTROYER});
        };
    }
}
