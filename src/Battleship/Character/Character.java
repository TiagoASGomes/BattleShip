package Battleship.Character;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract base class representing a character in the Battleship game.
 */
public abstract class Character {
    /**
     * The list of ships owned by the player.
     */
    protected List<Ship> playerShips = new ArrayList<>();
    /**
     * The type of the character.
     */
    private CharacterType type;

    /**
     * Constructor for the Character class.
     *
     * @param type The type of the character.
     */
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

    /**
     * Performs a special action in the game.
     *
     * @param playerHandler The player handler responsible for managing players.
     * @param game          The Battleship game instance.
     * @throws PlayerNotFoundException  If the player is not found.
     * @throws InvalidSyntaxException   If there is an invalid syntax in the action.
     * @throws InvalidPositionException If there is an invalid position in the action.
     */
    public abstract void special(Battleship.PlayerHandler playerHandler, Battleship game) throws PlayerNotFoundException, InvalidSyntaxException, InvalidPositionException;
}
