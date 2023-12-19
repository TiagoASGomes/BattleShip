package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Colors;
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
            //TODO mensagem desconecçao
            game.closeGame();
        } catch (InvalidSyntaxException | InvalidPositionException e) {
            try {
                playerHandler.sendMessage(Messages.INVALID_SYNTAX);
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    /**
     * Checks if a ship was hit by one player's shot, and marks it as hit if true.
     * If a ship was not hit, it also marks the spot on the map.
     *
     * @param playerHandler receives a PlayerHandler as the one who shot.
     * @param opponent      receives a PlayerHandler as the one who was targeted.
     * @param row           receives int representing the row of that shot.
     * @param col           receives int representing the column of that shot.
     */
    private void checkHit(Battleship.PlayerHandler playerHandler, Battleship.PlayerHandler opponent, int row, int col) {
        Ship ship = opponent.checkIfHit(row, col);

        if (ship != null) {
            playerHandler.winPoint(ship);
            if (ship.isSinked()) {
                playerHandler.sendMessage(Messages.KABOOM);
            } else {
                playerHandler.sendMessage(Messages.BOOM);
            }
            playerHandler.getOppMap().get(row).set(col, Colors.RED + "X"+ Colors.RESET);
            return;
        }
        playerHandler.sendMessage(Messages.MISSED);
        playerHandler.getOppMap().get(row).set(col, Colors.BLUE +"X"+ Colors.RESET);
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
