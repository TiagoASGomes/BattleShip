package commands;

public enum PreparationCommand {
    PLACE("place", new PlaceHandler()),

    ROTATE("rotate", new RotateHandler()),

    READY("ready", new ReadyHandler()),
    NOT_FOUND("Command not found", new CommandNotFoundHandler());
    private final String description;
    private final CommandHandler handler;

    /**
     * Constructor method of the enum PreparationCommand that accepts two arguments,
     * @param description provided String description as parameter,
     * @param handler provided CommandHandler handler as parameter.
     */
    PreparationCommand(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }

    /**
     * This method is responsible for mapping a given description string to the corresponding enumeration constant of
     * type PreparationCommand,
     * @param description provided String description as parameter,
     * @return command or command not found,
     */
    public static PreparationCommand getCommandFromDescription(String description) {
        for (PreparationCommand command : values()) {
            if (description.equals(command.description)) {
                return command;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Getter for CommandHandler,
     * @return handler.
     */
    public CommandHandler getHandler() {
        return handler;
    }

}

