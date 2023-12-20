package Battleship.Character;

import Battleship.Battleship;
import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.util.List;

import static commands.CommandHelper.*;

public class CharacterTwo extends Character {
    /**
     * Constructs a CharacterTwo instance with the specified array of ShipTypes,
     * initializing the player's fleet with the corresponding ships.
     *
     * @param ships An array of ShipTypes representing the initial fleet composition for CharacterTwo.
     */
    public CharacterTwo(ShipType[] ships) {
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
     * Get and validates the position from the player's input message.
     *
     * @param playerHandler The player handler for the current player.
     * @return An array of integers representing the extracted row and column positions.
     * @throws InvalidSyntaxException   If the input message has an invalid syntax.
     * @throws InvalidPositionException If the extracted position is out of bounds.
     */
    private int[] getPosition(Battleship.PlayerHandler playerHandler) throws InvalidSyntaxException, InvalidPositionException {
        String[] message = playerHandler.getMessage().split(" ");
        checkValidInput(message);
        int[] positions = new int[2];
        positions[0] = Integer.parseInt(message[1]);
        positions[1] = message[2].charAt(0) - 'A' + 1;
        checkOutOfBounds(positions, playerHandler.getMyMap());
        return positions;
    }

    /**
     * Checks if the specified positions are within the bounds of the player's map.
     *
     * @param positions An array of integers representing row and column positions.
     * @param myMap     The player's map represented as a list of lists of strings.
     * @throws InvalidPositionException If the positions are out of bounds.
     */
    private void checkOutOfBounds(int[] positions, List<List<String>> myMap) throws InvalidPositionException {
        if (positions[0] < 1 || positions[0] >= myMap.size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        if (positions[1] < 1 || positions[1] >= myMap.get(1).size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
    }

    /**
     * Performs the special action for Character Two.
     * checking for hits and updating the player's map accordingly.
     *
     * @param position      An array of integers representing the row and column positions.
     * @param opponent      The opponent player handler.
     * @param playerHandler The player handler for the current player.
     */
    private void doSpecial(int[] position, Battleship.PlayerHandler opponent, Battleship.PlayerHandler playerHandler) {
        int row = position[0] - 2;
        for (int col = position[1] - 2; col < position[1] + 3; col++) {
            shootPosition(opponent, playerHandler, row++, col);
        }
        row = position[0] + 2;
        for (int col = position[1] - 2; col < position[1] + 3; col++) {
            shootPosition(opponent, playerHandler, row--, col);
        }
    }

    /**
     * Checks if the specified position on the opponent's map is a hit, handles mine explosions,
     * and updates the player's map accordingly.
     *
     * @param opponent      The opponent player handler.
     * @param playerHandler The player handler for the current player.
     * @param row           The row position to be checked.
     * @param col           The column position to be checked.
     */
    private void shootPosition(Battleship.PlayerHandler opponent, Battleship.PlayerHandler playerHandler, int row, int col) {
        List<List<String>> opponentMap = opponent.getMyMap();

        if (checkInvalidPosition(row, col, opponentMap)) {
            return;
        }
        if (checkForMine(opponent, row, col)) {
            mineExplosion(playerHandler, opponent, row, col);
            return;
        }
        checkHit(playerHandler, opponent, row, col);
    }


}
