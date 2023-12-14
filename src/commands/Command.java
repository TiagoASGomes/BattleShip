package commands;

public enum Command {
    PLACE("place", new PlaceHandler()),

    ROTATE("rotate", new RotateHandler()),

    READY("ready", new ReadyHandler()),
    SHOOT("shoot", new ShootHandler()),
    NOT_FOUND("Command not found", new CommandNotFoundHandler());
    private String description;
    private CommandHandler handler;

    Command(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    public static Command getCommandFromDescription(String description) {
        for (Command command : values()) {
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

