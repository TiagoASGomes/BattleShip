package Battleship.Character;

import Battleship.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {

    protected List<Ship> playerShips = new ArrayList<>();

    private CharacterType type;


    public Character(CharacterType type) {
        this.type = type;

    }

    /**
     * @param playerShips Receives a list of Ships and sets it has that Player's playerShips.
     */
    public void setPlayerShips(List<Ship> playerShips) {
        this.playerShips = playerShips;
    }


    /**
     * @return returns that Player's list of Ship objects.
     */
    public List<Ship> getPlayerShips() {
        return playerShips;
    }
}
