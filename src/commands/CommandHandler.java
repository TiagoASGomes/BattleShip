package commands;

import Battleship.Battleship;

public interface CommandHandler {
    void execute(Battleship.PlayerHandler playerHandler, Battleship game);
}


