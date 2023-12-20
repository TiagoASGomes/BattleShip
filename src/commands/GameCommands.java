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

    /**
     * Constructor method of the enum GameCommands that accepts two arguments,
     * @param description provided String description as parameter,
     * @param handler provided CommandHandler handler as parameter.
     */
    GameCommands(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }
    /**
     * This method is responsible for mapping a given description string to the corresponding enumeration constant of
     * type PreparationCommand,
     * @param description provided String description as parameter,
     * @return command or command not found,
     */
    public static GameCommands getCommandFromDescription(String description) {
        for (GameCommands command : values()) {
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
