package commands;

import Battleship.Battleship;
import Messages.Messages;

public class CommandNotFoundHandler implements CommandHandler {
    @Override
    public void execute(Battleship.PlayerHandler playerHandler) {
        playerHandler.send(Messages.NO_SUCH_COMMAND);
    }
}
