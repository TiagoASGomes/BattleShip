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

public class CharacterOne extends Character {
    /**
     * Constructs a CharacterOne instance with the specified array of ShipTypes,
     * initializing the player's fleet with the corresponding ships.
     *
     * @param ships An array of ShipTypes representing the initial fleet composition for CharacterOne.
     */
    public CharacterOne(ShipType[] ships) {
        super(CharacterType.ONE);

        for (ShipType shipType : ships) {
            playerShips.add(ShipFactory.create(shipType));
        }
    }

    /**
     * Overrides the special action for character one,
     * targeting a row on the opponent's map based on the player's message.
     *
     * @param playerHandler The player handler for the current player.
     * @param game          The Battleship game instance.
     * @throws PlayerNotFoundException  If the opponent player is not found.
     * @throws InvalidSyntaxException   If there is an issue with the syntax of the player's message.
     * @throws InvalidPositionException If there is an issue with the row extracted from the player's message.
     */
    @Override
    public void special(Battleship.PlayerHandler playerHandler, Battleship game) throws PlayerNotFoundException, InvalidSyntaxException, InvalidPositionException {
        Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
        int row = getRow(playerHandler.getMessage());
        checkValidRow(row, opponent.getMyMap());
        doSpecial(row, opponent, playerHandler);
    }

    /**
     * Performs the special action for character one, targeting a row on the opponent's map.
     * Checks for mines, handles mine explosions, and updates the player's map based on the hit result.
     *
     * @param row           The row position to be targeted.
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
            checkHit(playerHandler, opponent, row, i);
        }
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

}
