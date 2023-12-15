package commands;

import Battleship.Battleship;

import java.security.InvalidKeyException;

public interface CommandHandler {
    void execute(Battleship.PlayerHandler playerHandler, Battleship game) throws InvalidKeyException;
}


