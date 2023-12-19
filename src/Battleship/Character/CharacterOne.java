package Battleship.Character;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Colors;
import MessagesAndPrinter.Messages;
import static commands.CommandHelper.*;
import java.util.List;

/**
 * Represents a specific type of character in the Battleship game - Character One.
 * Extends the abstract class Character.
 */


public class CharacterOne extends Character {
    /**
     * Constructor for CharacterOne class.
     *
     * @param ships An array of ShipType objects representing the initial ships for Character One.
     */
    public CharacterOne(ShipType[] ships) {
        super(CharacterType.ONE);

        for (ShipType shipType : ships) {
            playerShips.add(ShipFactory.create(shipType));
        }
    }

    /**
     * Overrides the special action for Character Three
     * Performs the special action for Character One.
     *
     * @param playerHandler The player handler responsible for managing players.
     * @param game          The Battleship game instance.
     * @throws PlayerNotFoundException  If the opponent player is not found.
     * @throws InvalidSyntaxException   If there is an invalid syntax in the action.
     * @throws InvalidPositionException If there is an invalid position in the action.
     */
    @Override
    public void special(Battleship.PlayerHandler playerHandler, Battleship game) throws PlayerNotFoundException, InvalidSyntaxException, InvalidPositionException {
        Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
        int row = getRow(playerHandler.getMessage());
        checkValidRow(row, opponent.getMyMap());
        doSpecial(row, opponent, playerHandler);
    }

    /**
     * Performs the special action for Character One.
     *
     * @param row           The row on the opponent's map where the special action is applied.
     * @param opponent      The opponent player handler.
     * @param playerHandler The player handler for the current player.
     */
    private void doSpecial(int row, Battleship.PlayerHandler opponent, Battleship.PlayerHandler playerHandler) {
        List<List<String>> playerMap = playerHandler.getOppMap();
        List<List<String>> opponentMap = opponent.getMyMap();
        for (int i = 1; i < playerMap.get(row).size() - 2; i++) {
            if (checkInvalidPosition(row, i, opponentMap)) {
                continue;
            }
            if (checkForMine(opponent, row, i)) {
                mineExplosion(playerHandler, opponent, row, i);
                continue;
            }
            Ship ship = opponent.checkIfHit(row, i);
            if (ship != null) {
                playerHandler.winPoint(ship);
                playerMap.get(row).set(i, Colors.RED + "X" + Colors.RESET);
                continue;
            }
            playerMap.get(row).set(i, Colors.BLUE + "X" + Colors.RESET);
        }
    }

    /**
     * Checks if the position is invalid.
     *
     * @param position The position to be checked.
     * @return True if the position is ' ' or '*'.
     */
    private boolean checkInvalidPosition(String position) {
        if (position.length() > 1) {
            return true;
        }
        char positionChar = position.charAt(0);
        return positionChar == ' ' || positionChar == '*';
    }

    /**
     * Checks if the specified row is valid within the given map.
     *
     * @param row   The row to be checked.
     * @param myMap The map to validate the row against.
     * @throws InvalidPositionException If the row is not valid.
     */
    private void checkValidRow(int row, List<List<String>> myMap) throws InvalidPositionException {
        if (row < 1 || row >= myMap.size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
    }

    /**
     * Extracts the row number from the provided message.
     *
     * @param message The message containing the row information.
     * @return The extracted row number.
     * @throws InvalidSyntaxException If there is an issue with the syntax of the message.
     */
    private int getRow(String message) throws InvalidSyntaxException {
        String[] splitMessage = message.split(" ");
        checkValidInput(splitMessage);
        return Integer.parseInt(splitMessage[1]);
    }

    /**
     * Checks the validity of the input message.
     *
     * @param message The input message to be checked.
     * @throws InvalidSyntaxException If there is an issue with the syntax of the message.
     */
    private void checkValidInput(String[] message) throws InvalidSyntaxException {
        if (message.length != 2) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(message[1])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
    }

    /**
     * Gets the opponent player handler.
     *
     * @param game          The Battleship game instance.
     * @param playerHandler The player handler for the current player.
     * @return The opponent player handler.
     * @throws PlayerNotFoundException If the opponent player is not found.
     */
    private Battleship.PlayerHandler getOpponent(Battleship game, Battleship.PlayerHandler playerHandler) throws PlayerNotFoundException {
        Battleship.PlayerHandler otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(playerHandler))
                .findFirst()
                .orElse(null);
        if (otherPlayer == null) {
            throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
        }
        return otherPlayer;
    }

    /**
     * Checks if the given string is not a number.
     *
     * @param number The string to be checked.
     * @return True if the string is not a number, false otherwise.
     */
    private boolean isNotNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!java.lang.Character.isDigit(digit)) {
                return true;
            }
        }
        return false;
    }
}
