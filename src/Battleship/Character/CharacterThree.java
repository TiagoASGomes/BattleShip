package Battleship.Character;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.util.List;

/**
 * Represents a specific type of character in the Battleship game - Character Three.
 * Extends the abstract class Character.
 */
public class CharacterThree extends Character {
    /**
     * Constructor for CharacterOne class.
     *
     * @param ships An array of ShipType objects representing the initial ships for Character Three.
     */
    public CharacterThree(ShipType[] ships) {
        super(CharacterType.ONE);

        for (ShipType shipType : ships) {
            playerShips.add(ShipFactory.create(shipType));
        }
    }

    /**
     * Overrides the special action for Character Three
     * Retrieves the opponent player handler, extracts the position from the player's message, and performs the special action.
     *
     * @param playerHandler The player handler for the current player.
     * @param game          The Battleship game instance.
     * @throws PlayerNotFoundException  If the opponent player is not found.
     * @throws InvalidSyntaxException   If there is an issue with the syntax of the player's message.
     * @throws InvalidPositionException If there is an issue with the position extracted from the player's message.
     */
    @Override
    public void special(Battleship.PlayerHandler playerHandler, Battleship game) throws PlayerNotFoundException, InvalidSyntaxException, InvalidPositionException {
        Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
        int[] position = getPosition(playerHandler);
        doSpecial(position, opponent, playerHandler);
    }

    /**
     * Performs the special action for Character Three.
     *
     * @param position      An array of integers representing the row and column positions.
     *                      position[0] corresponds to the row, and position[1] corresponds to the column.
     * @param opponent      The opponent player handler.
     * @param playerHandler The player handler for the current player.
     */
    private void doSpecial(int[] position, Battleship.PlayerHandler opponent, Battleship.PlayerHandler playerHandler) {
        for (int i = position[1] - 2; i < position[1] + 3; i++) {
            checkHit(opponent, playerHandler, position[0], i);
        }
        for (int i = position[0] - 2; i < position[0] + 3; i++) {
            checkHit(opponent, playerHandler, i, position[1]);
        }
    }

    /**
     * Checks if the specified position on the opponent's map is a hit and updates the player's map accordingly.
     *
     * @param opponent      The opponent player handler.
     * @param playerHandler The player handler for the current player.
     * @param row           The row position to be checked.
     * @param col           The column position to be checked.
     */
    private void checkHit(Battleship.PlayerHandler opponent, Battleship.PlayerHandler playerHandler, int row, int col) {
        List<List<String>> playerMap = playerHandler.getOppMap();
        List<List<String>> opponentMap = opponent.getMyMap();
        String positionString;

        if (checkInvalidPosition(row, col, opponentMap)) {
            return;
        }
        Ship ship = opponent.checkIfHit(row, col);
        if (ship != null) {
            playerHandler.winPoint(ship);
            playerMap.get(row).set(col, "\u001B[31mX\u001B[0m");
            return;
        }
        playerMap.get(row).set(col, "\u001B[34mX\u001B[0m");
    }

    /**
     * Checks if the specified row and column positions on the opponent's map are invalid.
     *
     * @param row         The row position to be checked.
     * @param col         The column position to be checked.
     * @param opponentMap The opponent's map to validate the positions against.
     * @return True if the position is ' ' or '*'.
     */
    private boolean checkInvalidPosition(int row, int col, List<List<String>> opponentMap) {
        if (row < 1 || row >= opponentMap.size() - 2 || col < 1 || col >= opponentMap.get(1).size() - 2) {
            return true;
        }
        String position = opponentMap.get(row).get(col);
        if (position.length() > 1) {
            return true;
        }
        char positionChar = position.charAt(0);
        return positionChar == ' ' || positionChar == '*';
    }

    /**
     * Extracts the row and column positions from the player's message and validates them.
     *
     * @param playerHandler The player handler containing the message and map information.
     * @return An array of integers representing the row and column positions.
     * positions[0] corresponds to the row, and positions[1] corresponds to the column.
     * @throws InvalidSyntaxException   If there is an issue with the syntax of the player's message.
     * @throws InvalidPositionException If the extracted positions are out of bounds on the game map.
     */
    private int[] getPosition(Battleship.PlayerHandler playerHandler) throws InvalidSyntaxException, InvalidPositionException {
        String[] message = playerHandler.getMessage().split(" ");
        checkValidInput(message);
        int[] positions = new int[2];
        positions[0] = Integer.parseInt(message[1]);
        positions[1] = message[2].charAt(0) - 'A' + 1;
        checkValidOutOfBounds(positions, playerHandler.getMyMap());
        return positions;
    }

    /**
     * Checks the validity of ship placement positions to ensure they are within the bounds of the game map.
     *
     * @param positions An array representing the row and column positions.
     *                  positions[0] corresponds to the row, and positions[1] corresponds to the column.
     * @param myMap     The game map to validate the positions against.
     * @throws InvalidPositionException If the positions are out of bounds.
     */
    private void checkValidOutOfBounds(int[] positions, List<List<String>> myMap) throws InvalidPositionException {
        if (positions[0] < 1 || positions[0] >= myMap.size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        if (positions[1] < 1 || positions[1] >= myMap.get(1).size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
    }

    /**
     * Checks the validity of the input message for ship placement.
     *
     * @param message The input message to be checked, expected to be an array of strings.
     * @throws InvalidSyntaxException If there is an issue with the syntax of the ship placement message.
     */
    private void checkValidInput(String[] message) throws InvalidSyntaxException {
        if (message.length != 3) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(message[1])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (message[2].charAt(0) < 65 || message[2].charAt(0) > 90) {
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
