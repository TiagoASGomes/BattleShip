package commands;
import Messages.Messages;
public class CommandNotFoundHandler implements CommandHandler{
    @Override
    public void execute(Server server, Battleship.PlayerHandler playerHandler) {
        playerHandler.send(Messages.NO_SUCH_COMMAND);
    }
}
