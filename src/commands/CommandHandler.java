package commands;

import Battleship.Battleship;

public interface CommandHandler {

    /**
     * This method represents the action each player can have,
     * @param playerHandler provided access to the PlayerHandler methods and properties,
     * @param game represents an instance of a class game.
     */
    void execute(Battleship.PlayerHandler playerHandler, Battleship game);
}


