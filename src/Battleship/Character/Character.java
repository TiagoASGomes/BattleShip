package Battleship.Character;

import Battleship.ships.StraightShip;

import java.util.List;

public abstract class Character {

    protected List<StraightShip> playerShips;

    private CharacterType type;


    public Character(CharacterType type) {
        this.type = type;

    }

    public void setPlayerShips(List<StraightShip> playerShips) {
        this.playerShips = playerShips;
    }

    public List<StraightShip> getPlayerShips() {
        return playerShips;
    }
}
