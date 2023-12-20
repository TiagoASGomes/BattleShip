package commands;

import Battleship.Battleship;
import MessagesAndPrinter.Messages;

public class CommandNotFoundHandler implements CommandHandler {

    /**
     * Implements the CommandHandler and send message to player if an invalid command is used as an input,
     * @param playerHandler provided access to the PlayerHandler methods and properties,
     * @param game represents an instance of a class game.
     */
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        playerHandler.sendMessage(Messages.NO_SUCH_COMMAND);
    }
}
