package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Exceptions.InvalidSyntaxException;
import Messages.Messages;
import MessagesAndPrinter.Messages;

import java.util.List;

public class RotateHandler implements CommandHandler {

    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<Ship> shipList = playerHandler.getCharacter().getPlayerShips();
        int boatIndex;
        try {
            boatIndex = getBoatIndex(playerHandler.getMessage());
        } catch (InvalidSyntaxException e) {
            playerHandler.sendMessage(e.getMessage());
            return;
        }
        shipList.get(boatIndex).rotate();

    }

    private int getBoatIndex(String message) throws InvalidSyntaxException {
        String[] separated = message.split(" ");

        checkValidInput(separated);

        return Integer.parseInt(separated[1]);
    }

    private void checkValidInput(String[] message) throws InvalidSyntaxException {
        if (message.length != 2) {
            throw new InvalidSyntaxException(Messages.INVALID_SYNTAX);
        }
        if (!Character.isDigit(message[1].charAt(0))) {
            throw new InvalidSyntaxException(Messages.INVALID_SYNTAX);
        }
    }

}
