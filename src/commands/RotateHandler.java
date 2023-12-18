package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Exceptions.InvalidSyntaxException;
import MessagesAndPrinter.Messages;

import java.util.List;

public class RotateHandler implements CommandHandler {

    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<Ship> shipList = playerHandler.getCharacter().getPlayerShips();
        int boatIndex;
        try {
            boatIndex = getBoatIndex(playerHandler.getMessage());
            shipList.get(boatIndex).rotate();
            playerHandler.sendMessage(Messages.ROTATED_SHIP + boatIndex);
        } catch (InvalidSyntaxException e) {
            playerHandler.sendMessage(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            playerHandler.sendMessage(Messages.SHIP_DOESNT_EXIST);
        }
    }

    private int getBoatIndex(String message) throws InvalidSyntaxException {
        String[] separatedMessage = message.split(" ");

        checkValidInput(separatedMessage);

        return Integer.parseInt(separatedMessage[1]);
    }

    private void checkValidInput(String[] message) throws InvalidSyntaxException {
        if (message.length != 2) {
            throw new InvalidSyntaxException(Messages.INVALID_ROTATE_SYNTAX);
        }
        if (!Character.isDigit(message[1].charAt(0))) {
            throw new InvalidSyntaxException(Messages.INVALID_ROTATE_SYNTAX);
        }
    }

}
