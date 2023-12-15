package commands;

import Battleship.Battleship;
import Messages.Messages;

public class CommandNotFoundHandler implements CommandHandler {
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        playerHandler.sendMessage(Messages.NO_SUCH_COMMAND);
    }
}
