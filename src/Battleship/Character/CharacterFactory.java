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
            case ONE -> new CharacterOne(new ShipType[]{ShipType.BIG_SHIP, ShipType.BATTLECRUISER});
//                    new CharacterOne(new ShipType[]{ShipType.BIG_SHIP, ShipType.BATTLECRUISER, ShipType.SUBMARINE, ShipType.CRUISER, ShipType.SUBMARINE});
            case TWO ->
                    new CharacterTwo(new ShipType[]{ShipType.BIG_SHIP, ShipType.BATTLECRUISER, ShipType.DESTROYER, ShipType.DESTROYER, ShipType.CRUISER}); //18
            case THREE ->
                    new CharacterThree(new ShipType[]{ShipType.CROSS_SHIP, ShipType.BIG_SHIP, ShipType.CARRIER, ShipType.DESTROYER, ShipType.SUBMARINE});//19
        };
    }
}
