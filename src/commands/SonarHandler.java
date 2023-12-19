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
import java.util.Optional;

public class SonarHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<List<String>> map = playerHandler.getOppMap();

        try {
            checkPlayerPoints(playerHandler);
            List<List<String>> opponentMap = getOpponentMap(game, playerHandler);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponentMap);
            placeSonar(coordinates, map, opponentMap);
            playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.SONAR.getPoints());
        } catch (InvalidSyntaxException | InvalidPositionException | NotEnoughPointsException e) {
            playerHandler.sendMessage(e.getMessage());
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (PlayerNotFoundException e) {
            //TODO mensagem desconecçao
            game.closeGame();
        }
    }

    private void checkPlayerPoints(Battleship.PlayerHandler playerHandler) throws NotEnoughPointsException {
        if (playerHandler.getPlayerPoints() < PointValues.SONAR.getPoints()) {
            throw new NotEnoughPointsException(Messages.NOT_ENOUGH_POINTS);
        }
    }

    private void placeSonar(int[] coordinates, List<List<String>> map, List<List<String>> opponentMap) {
        int startRow = coordinates[0] - 1;
        int startCol = coordinates[1] - 1;


        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (checkInvalidCoordinate(i, j, map)) {
                    continue;
                }
                char position = opponentMap.get(i).get(j).charAt(0);
                if (position == '~' || position == ' ' || position == '*') {
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
        map.get(row).set(col, "\033[0;35mO\033[0m");
    }

    /**
     * @param row receives int representing row on the map.
     * @param col receives int representing column on the map.
     * @param map receives List of a List of Strings representing a map.
     * @return returns true if either row or column are bigger than map size, thus exceeding the boundaries of the map.
     * Row must not exceed the List size.
     * Column must not exceed the size of the List within the List (index represented by row).
     */
    private boolean checkInvalidCoordinate(int row, int col, List<List<String>> map) {
        return row >= map.size() || col >= map.get(row).size();
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

    /**
     * @param separated receives an array of String as parameter.
     * @throws InvalidSyntaxException throws InvalidSyntaxException if that array is not of length 3,
     *                                if the second index of that array is not a number,
     *                                if the third index of that array is not a char between A and Z.
     */
    private void checkValidInput(String[] separated) throws InvalidSyntaxException {
        if (separated.length != 3) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(separated[1])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (separated[2].charAt(0) < 65 || separated[2].charAt(0) > 90) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
    }

    /**
     * @param number Receives a String as parameter.
     * @return Returns true if that String is not a number.
     */
    private boolean isNotNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!Character.isDigit(digit)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param game          receives a Battleship game as parameter.
     * @param playerHandler receives a PlayerHandler as parameter that represents the opponent.
     * @return returns a List of a List of Strings that is the opponent's representation of his own map.
     * @throws PlayerNotFoundException if the PlayerHandler representing the opponent is null.
     */
    private List<List<String>> getOpponentMap(Battleship game, Battleship.PlayerHandler playerHandler) throws PlayerNotFoundException {
        Optional<Battleship.PlayerHandler> otherPlayer = game.getOtherPlayer(playerHandler);
        if (otherPlayer.isEmpty()) {
            throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
        }
        return otherPlayer.get().getMyMap();
    }
}
