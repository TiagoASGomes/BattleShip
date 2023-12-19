package commands;

public enum MineCommand {

    NOT_FOUND("Command not found", new CommandNotFoundHandler()),
    MINE("mine", new MineHandler());


    private final String description;
    private final CommandHandler handler;

    MineCommand(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static MineCommand getCommandFromDescription(String description) {
        for (MineCommand command : values()) {
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

