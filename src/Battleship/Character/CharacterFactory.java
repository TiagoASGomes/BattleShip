package Battleship.Character;

import Battleship.ships.ShipType;

public class CharacterFactory {
    /**
     * Creates a new instance of the Character class based on the specified CharacterType.
     *
     * @param type The type of character to create.
     * @return A new instance of the Character class corresponding to the given type.
     */
    public static Character create(CharacterType type) {

        return switch (type) {
            case ONE -> new CharacterOne(new ShipType[]{ShipType.CROSS_SHIP, ShipType.BATTLECRUISER});
            case TWO -> new CharacterTwo(new ShipType[]{ShipType.BIG_SHIP});
            case THREE -> new CharacterThree(new ShipType[]{ShipType.BIG_SHIP});
        };
    }
}
