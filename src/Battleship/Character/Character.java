package Battleship.Character;

import Battleship.ships.Ship;

import java.util.List;

public abstract class Character {

    protected List<Ship> playerShips;

    private CharacterType type;


    public Character(CharacterType type) {
        this.type = type;

    }

    public void setPlayerShips(List<Ship> playerShips) {
        this.playerShips = playerShips;
    }

    public List<Ship> getPlayerShips() {
        return playerShips;
    }
}