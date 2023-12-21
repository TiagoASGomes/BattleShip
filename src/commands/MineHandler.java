package commands;

import Battleship.Battleship;
import Battleship.PointValues;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.util.List;

import static commands.CommandHelper.checkPlayerPoints;
import static commands.CommandHelper.checkValidInput;

public class MineHandler implements CommandHandler {

    /**
     * Executes the GameCommand MINE. Checks if player has points and subtracts its cost.
     * Calls the methods getCoordinates and placeMine, and does validations on the points and position.
     *
     * @param playerHandler receives a PlayerHandler representing the player.
     * @param game          receives a Battleship representing the game.
     */
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            checkPlayerPoints(playerHandler, PointValues.MINE);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), playerHandler.getMyMap());
            placeMine(playerHandler, coordinates[0], coordinates[1]);
            playerHandler.takeTurn();
        } catch (NotEnoughPointsException e) {
            try {
                playerHandler.sendMessage(Messages.NOT_ENOUGH_POINTS);
                playerHandler.takeTurn();
            } catch (IOException | PlayerNotFoundException ex) {
                game.closeGame();
            }
        } catch (PlayerNotFoundException | IOException e) {
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
     * Places the mine on the map replacing the default char for an 'O' on the map at the given coordinates.
     * Subtracts the cost of placing a mine in player points.
     *
     * @param playerHandler receives a PlayerHandler representing a player.
     * @param row           receives an int representing a row on the map.
     * @param col           receives an int representing the column on the map.
     */
    private void placeMine(Battleship.PlayerHandler playerHandler, int row, int col) {
        playerHandler.getMyMap().get(row).set(col, "O");
        playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.MINE.getPoints());
    }

    /**
     * Splits the input message by spaces and takes two ints out of that array of Strings to get the coordinates
     * representing row and column.
     *
     * @param map     receives a List of a List of Strings representing a map.
     * @param message receives a String message.
     * @return returns an array of ints representing two coordinates (row and column) on the map.
     * @throws InvalidSyntaxException   if the input doesn't have the right syntax,
     * @throws InvalidPositionException if the coordinates are bigger than row size (List size)
     *                                  or column size (size of list within the list, as in size of given index of the List).
     *                                  Also throws this exception if the char on that position is not a '~' representing the water.
     */
    private int[] getCoordinates(String message, List<List<String>> map) throws InvalidSyntaxException, InvalidPositionException {
        String[] separated = message.split(" ");
        checkValidInput(separated);

        int[] coordinates = new int[2];
        coordinates[0] = Integer.parseInt(separated[1]);
        coordinates[1] = separated[2].charAt(0) - 'A' + 1;

        if (coordinates[0] >= map.size() - 1 || coordinates[1] >= map.get(coordinates[0]).size() - 1) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        char position = map.get(coordinates[0]).get(coordinates[1]).charAt(0);
        if (position != '~') {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        return coordinates;
    }


}
