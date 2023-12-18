package commands;

public enum PreparationCommand {
    PLACE("place", new PlaceHandler()),

    ROTATE("rotate", new RotateHandler()),

    READY("ready", new ReadyHandler()),
    NOT_FOUND("Command not found", new CommandNotFoundHandler());
    private final String description;
    private final CommandHandler handler;

    PreparationCommand(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static PreparationCommand getCommandFromDescription(String description) {
        for (PreparationCommand command : values()) {
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

