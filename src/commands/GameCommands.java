package commands;

public enum GameCommands {
    SHOOT("shoot", new ShootHandler()),
    SONAR("sonar", new SonarHandler()),
    BOMB("bomb", new BombHandler()),
    SPECIAL("special", new SpecialHandler()),
    MINE("mine", new MineHandler()),
    NOT_FOUND("Command not found", new CommandNotFoundHandler());


    private final String description;
    private final CommandHandler handler;

    GameCommands(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static GameCommands getCommandFromDescription(String description) {
        for (GameCommands command : values()) {
            if (description.equals(command.description)) {
                return command;
            }
        }
        return NOT_FOUND;
    }

    public CommandHandler getHandler() {
        return handler;
    }

    public String getDescription() {
        return description;
    }
}
