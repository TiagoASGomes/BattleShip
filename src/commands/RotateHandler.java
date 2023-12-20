package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Exceptions.InvalidSyntaxException;
import MessagesAndPrinter.Messages;

import java.util.List;

public class RotateHandler implements CommandHandler {
    /**
     * Executes the Rotate Command in the PreparationCommand,
     * Rotates a ship based on the player's message. It handles exceptions related to invalid syntax and out-of-bounds
     * indices, sending appropriate error messages to the player,
     * @param playerHandler object representing the player's handler in the Battleship game,
     * @param game object representing a game in the Battleship game.
     */
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

    /**
     * Receives a String message as input and validates it,
     * @param message String message as parameter,
     * @return the index from a given input message.
     * @throws InvalidSyntaxException throws InvalidSyntaxException if input is not valid.
     */
    private int getBoatIndex(String message) throws InvalidSyntaxException {
        String[] separatedMessage = message.split(" ");

        checkValidInput(separatedMessage);

        return Integer.parseInt(separatedMessage[1]);
    }

    /**
     * Receives an Array String,
     * @param message String array as parameter,
     * @throws InvalidSyntaxException throws InvalidSyntaxException if the array´s length is different from 2,
     *                                throws InvalidSyntaxException if the first character of the string at index 1 of
     *                                the message array is not a digit.
     */
    private void checkValidInput(String[] message) throws InvalidSyntaxException {
        if (message.length != 2) {
            throw new InvalidSyntaxException(Messages.INVALID_ROTATE_SYNTAX);
        }
        if (!Character.isDigit(message[1].charAt(0))) {
            throw new InvalidSyntaxException(Messages.INVALID_ROTATE_SYNTAX);
        }
    }

}
