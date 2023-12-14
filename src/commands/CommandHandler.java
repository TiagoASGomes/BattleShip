package commands;

public interface CommandHandler {
    void execute(Server server, Server.PlayerHandler playerHandler);
}


