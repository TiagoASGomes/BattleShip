package commands;

import Battleship.Battleship;
import Battleship.PointValues;
import Battleship.ships.Ship;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Colors;
import MessagesAndPrinter.Messages;

import java.util.List;
import java.util.Optional;

public class CommandHelper {

    /**
     * Checks if a String is not a number.
     *
     * @param number Receives a String
     * @return Returns true if that String is not a number
     */
    public static boolean isNotNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!Character.isDigit(digit)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Receives a String array of inputs,
     *
     * @param separated receives an array of String as parameter.
     * @throws InvalidSyntaxException throws InvalidSyntaxException if that array is not of length 3,
     *                                if the second index of that array is not a number,
     *                                if the third index of that array is not a char between A and Z.
     */
    public static void checkValidInput(String[] separated) throws InvalidSyntaxException {
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
     * @param game          receives a Battleship game as parameter
     * @param playerHandler receives a PlayerHandler as parameter
     * @return returns a PlayerHandler that is not himself, if there is one,
     * @throws PlayerNotFoundException and throws a PlayerNotFoundException if there is none.
     */
    public static Battleship.PlayerHandler getOpponent(Battleship game, Battleship.PlayerHandler playerHandler) throws PlayerNotFoundException {
        Optional<Battleship.PlayerHandler> opponent = game.getOtherPlayer(playerHandler);
        if (opponent.isEmpty()) {
            throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
        }
        return opponent.get();
    }

    /**
     * Checks if a player has enough points to call a certain command.
     *
     * @param playerHandler receives PlayerHandler representing a player.
     * @param type          receives PointValues representing the cost in player points for each command.
     * @throws NotEnoughPointsException if the player does not have enough points to call that command.
     */
    public static void checkPlayerPoints(Battleship.PlayerHandler playerHandler, PointValues type) throws NotEnoughPointsException {
        if (playerHandler.getPlayerPoints() < type.getPoints()) {
            throw new NotEnoughPointsException(Messages.NOT_ENOUGH_POINTS);
        }
    }

    /**
     * Check's if the coordinates received for row and column are valid for the map boundaries.
     * Row must not exceed the List size.
     * Column must not exceed the size of the List within the List (index represented by row).
     *
     * @param row         receives int representing row on the map.
     * @param col         receives int representing column on the map.
     * @param opponentMap receives List of a List of Strings representing a map.
     * @return returns true if either row or column are bigger than map size, thus exceeding the boundaries of the map.
     */
    public static boolean checkInvalidPosition(int row, int col, List<List<String>> opponentMap) {
        if (row < 1 || row >= opponentMap.size() - 2 || col < 1 || col >= opponentMap.get(1).size() - 2) {
            return true;
        }
        String position = opponentMap.get(row).get(col);
        if (position.length() > 1) {
            return true;
        }
        char positionChar = position.charAt(0);
        return positionChar == ' ' || positionChar == '*' || positionChar == 'R';
    }

    /**
     * @param otherPlayer Receives a PlayerHandler
     * @param row         receives an int that represents a row as parameter
     * @param col         receives another int that represents a column as parameter
     * @return returns true if that coordinate in that PlayerHandler's Map has the char 'O'
     */
    public static boolean checkForMine(Battleship.PlayerHandler otherPlayer, int row, int col) {
        return otherPlayer.getMyMap().get(row).get(col).charAt(0) == 'O';
    }

    /**
     * Marks the mine at opponent's map has hit, and shoots randomly at player's own map as backfire.
     *
     * @param player   receives a PlayerHandler as the one who shot the opponent's mine.
     * @param opponent receives a PlayerHandler as the one who's mine was targeted.
     * @param row      receives int representing the row of that mine.
     * @param col      receives int representing the column of that mine.
     */
    public static void mineExplosion(Battleship.PlayerHandler player, Battleship.PlayerHandler opponent, int row, int col) {
        opponent.getMyMap().get(row).set(col, Colors.BLUE + "R" + Colors.RESET);
        player.getOppMap().get(row).set(col, Colors.BLUE + "R" + Colors.RESET);

        int randRow = (int) (Math.random() * (player.getMyMap().size() - 2 + 1) + 1);
        int randCol = (int) (Math.random() * (player.getMyMap().getFirst().size() - 2 + 1) + 1);
        while (checkInvalidPosition(randRow, randCol, opponent.getMyMap())) {
            randRow = (int) (Math.random() * (player.getMyMap().size() - 2 + 1) + 1);
            randCol = (int) (Math.random() * (player.getMyMap().getFirst().size() - 2 + 1) + 1);
        }

        checkHit(opponent, player, randRow, randCol);
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
    public static void checkHit(Battleship.PlayerHandler playerHandler, Battleship.PlayerHandler opponent, int row, int col) {
        Ship ship = opponent.checkIfHit(row, col);

        if (ship != null) {
            playerHandler.winPoint(ship);
            if (ship.isSinked()) {
                playerHandler.sendMessage(Messages.KABOOM);
                playerHandler.sendMessage(Messages.BOOM_COMMAND);
            } else {
                playerHandler.sendMessage(Messages.BOOM);
                playerHandler.sendMessage(Messages.BOOM_COMMAND);
            }
            playerHandler.getOppMap().get(row).set(col, Colors.RED + "X" + Colors.RESET);
            return;
        }
        playerHandler.sendMessage(Messages.MISSED_COMMAND);
        playerHandler.getOppMap().get(row).set(col, Colors.BLUE + "X" + Colors.RESET);
    }

}
