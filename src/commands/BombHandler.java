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

import static commands.CommandHelper.*;

public class BombHandler implements CommandHandler {
    /**
     * Receives a message with coordinates to place a bomb in opponent map. Calls doExplosion method.
     * Throws exceptions if the input is not correct (syntax , invalid position or not enough points).
     * @param playerHandler provided access to the PlayerHandler methods and properties,
     * @param game represents an instance of a class game.
     */
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            checkPlayerPoints(playerHandler, PointValues.BOMB);
            Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponent.getMyMap());
            doExplosion(coordinates, playerHandler, opponent);
            playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.BOMB.getPoints());
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
     * This method handles the logic of the consequences of an explosion. It takes a coordinate and if valid,
     * set´s an explosion at the opponent's map.
     * @param coordinates receives coordinates as parameter,
     * @param playerHandler receives a playerHandler as a parameter,
     * @param opponent receives an opponent as a parameter.
     */
    private void doExplosion(int[] coordinates, Battleship.PlayerHandler playerHandler, Battleship.PlayerHandler opponent) {
        int startRow = coordinates[0];
        int startCol = coordinates[1];


        for (int i = startRow; i < startRow + 2; i++) {
            for (int j = startCol; j < startCol + 2; j++) {
                if (checkInvalidPosition(i, j, opponent.getMyMap())) {
                    continue;
                }
                if (checkForMine(opponent, i, j)) {
                    mineExplosion(playerHandler, opponent, i, j);
                    continue;
                }

                checkHit(playerHandler, opponent, i, j);
            }
        }
    }

    /**
     * Receives an input String, and after validation turns it into an array of int to become the coordinates,
     * @param message input message as parameter,
     * @param map receives a map as parameter,
     * @return the coordinates after validation, in which it will be performed some action.
     * @throws InvalidSyntaxException if the input doesn't have the right syntax,
     * @throws InvalidPositionException if the input doesn't have a valid position.
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
