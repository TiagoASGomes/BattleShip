package commands;

import Battleship.Battleship;
import Battleship.PointValues;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Colors;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.util.List;

import static commands.CommandHelper.*;

public class SonarHandler implements CommandHandler {

    /**
     * Check's if the player has points,executes the SONAR GameCommand and pays its cost in player points.
     * Calls placeSonar method.
     *
     * @param playerHandler receives a PlayerHandler representing the player.
     * @param game          receives a Battleship representing the game.
     */
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<List<String>> map = playerHandler.getOppMap();

        try {
            checkPlayerPoints(playerHandler, PointValues.SONAR);
            List<List<String>> opponentMap = getOpponent(game, playerHandler).getMyMap();
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponentMap);
            placeSonar(coordinates, map, opponentMap);
            playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.SONAR.getPoints());
        } catch (InvalidSyntaxException | InvalidPositionException | NotEnoughPointsException e) {
            playerHandler.sendMessage(e.getMessage());
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                game.closeGame();
            }
        } catch (PlayerNotFoundException e) {
            game.closeGame();
        }
    }

    /**
     * Check's in the opponent's representation of his own map if the coordinates are valid.
     * Calls method putMarkOnMap to place the sonar on our representation of opponent's map.
     *
     * @param coordinates receives an array of ints representing coordinates.
     * @param map         receives a List of a List of Strings representing a map.
     * @param opponentMap receives another List of a List of Strings representing the opponent's map.
     */
    private void placeSonar(int[] coordinates, List<List<String>> map, List<List<String>> opponentMap) {
        int startRow = coordinates[0] - 1;
        int startCol = coordinates[1] - 1;


        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (checkInvalidPosition(i, j, opponentMap)) {
                    continue;
                }
                if (opponentMap.get(i).get(j).equals("~")) {
                    continue;
                }

                putMarkOnMap(i, j, map);
            }
        }
    }

    /**
     * It marks the coordinate on which the sonar is placed on one's representation of opponent's map.
     *
     * @param row receives int representing a row on the map.
     * @param col receives int representing a column on the map.
     * @param map receives a List of a List of String representing a map.
     */
    private void putMarkOnMap(int row, int col, List<List<String>> map) {
        if (map.get(row).get(col).length() > 1) {
            return;
        }
        map.get(row).set(col, Colors.PURPLE + "?" + Colors.RESET);
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

        if (coordinates[0] >= map.size() - 1 || coordinates[1] >= map.get(coordinates[0]).size() - 1) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        char position = map.get(coordinates[0]).get(coordinates[1]).charAt(0);
        if (position == '*') {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        return coordinates;
    }

}
