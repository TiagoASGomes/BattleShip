package commands;

import Battleship.Battleship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.util.List;

import static commands.CommandHelper.*;

public class ShootHandler implements CommandHandler {

    /**
     * Implements the GameCommand SHOOT.
     * Calls getCoordinates method on the message input by the user and checks if it hit Ship, water or Mine
     * on the opponents map.
     *
     * @param playerHandler receives a PlayerHandler as the one who is targeted.
     * @param game          receives a Battleship game being disputed, as the outer class of PlayerHandler.
     */
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponent.getMyMap());

            if (checkForMine(opponent, coordinates[0], coordinates[1])) {
                mineExplosion(playerHandler, opponent, coordinates[0], coordinates[1]);
                return;
            }
            checkHit(playerHandler, opponent, coordinates[0], coordinates[1]);
        } catch (PlayerNotFoundException e) {
            game.closeGame();
        } catch (InvalidSyntaxException | InvalidPositionException e) {
            try {
                playerHandler.sendMessage(Messages.INVALID_SYNTAX);
                playerHandler.takeTurn();
            } catch (IOException | PlayerNotFoundException ex) {
                game.closeGame();
            }
        }

    }


    /**
     * Creates an array of String by splitting the String message by spaces.
     * Takes two ints from that array index 1 and 2, and stores them in an array of length 2.
     *
     * @param message receives a String as parameter.
     * @param map     receives a List of Lists of Strings as parameter.
     * @return returns an array of int type that represents two coordinates of the map.
     * @throws InvalidSyntaxException   if the input is not allowed.
     * @throws InvalidPositionException if the coordinates are off of the map size, if the length is bigger than 1,
     *                                  and if there is already a space, an '*', or 'R' in that coordinate on the map.
     */
    private int[] getCoordinates(String message, List<List<String>> map) throws InvalidSyntaxException, InvalidPositionException {
        String[] separated = message.split(" ");
        checkValidInput(separated);

        int[] coordinates = new int[2];
        coordinates[0] = Integer.parseInt(separated[1]);
        coordinates[1] = separated[2].charAt(0) - 'A' + 1;

        if (checkInvalidPosition(coordinates[0], coordinates[1], map)) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }


        return coordinates;
    }

}
