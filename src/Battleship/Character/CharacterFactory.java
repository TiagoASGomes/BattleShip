package Battleship.Character;

import Battleship.ships.ShipType;

/**
 * A factory class for creating instances of the Character class based on the specified CharacterType.
 */
public class CharacterFactory {
    /**
     * Creates a new Character instance based on the provided CharacterType.
     *
     * @param type The type of character to be created.
     * @return A new Character instance corresponding to the specified type.
     */
    public static Character create(CharacterType type) {

        return switch (type) {
            case ONE -> new CharacterOne(new ShipType[]{ShipType.BATTLECRUISER});
            case TWO -> new CharacterTwo(new ShipType[]{ShipType.BIG_SHIP});
            case THREE -> new CharacterThree(new ShipType[]{ShipType.BIG_SHIP});
        };
    }
}
