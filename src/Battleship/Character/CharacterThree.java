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

import static commands.CommandHelper.checkForMine;
import static commands.CommandHelper.mineExplosion;

public class CharacterThree extends Character {
    /**
     * Constructs a CharacterThree instance with the specified array of ShipTypes,
     * initializing the player's fleet with the corresponding ships.
     *
     * @param ships An array of ShipTypes representing the initial fleet composition for CharacterThree.
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
            shootPosition(opponent, playerHandler, position[0], i);
        }
        for (int i = position[0] - 2; i < position[0] + 3; i++) {
            shootPosition(opponent, playerHandler, i, position[1]);
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


    /**
     * Retrieves the row and column positions from the player's message, validates the input syntax,
     * and checks if the positions are within the bounds of the player's map.
     *
     * @param playerHandler The player handler for the current player.
     * @return An array of integers representing the row and column positions.
     * @throws InvalidSyntaxException   If there is an issue with the syntax of the player's message.
     * @throws InvalidPositionException If there is an issue with the extracted positions.
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
     * Checks if the specified row and column positions are within the valid bounds of the map.
     *
     * @param positions An array of integers representing the row and column positions to be checked.
     * @param myMap     The map to validate the positions against.
     * @throws InvalidPositionException If the positions are outside the valid bounds.
     */
    private void checkValidOutOfBounds(int[] positions, List<List<String>> myMap) throws InvalidPositionException {
        if (positions[0] < 1 || positions[0] >= myMap.size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        if (positions[1] < 1 || positions[1] >= myMap.get(1).size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
    }

}
