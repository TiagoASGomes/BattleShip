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

public class MineHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            checkPlayerPoints(playerHandler, PointValues.MINE);
            Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponent.getMyMap());
            placeMine(playerHandler, coordinates[0], coordinates[1]);
            playerHandler.takeTurn();
        } catch (NotEnoughPointsException e) {
            try {
                playerHandler.sendMessage(Messages.NOT_ENOUGH_POINTS);
                playerHandler.takeTurn();
            } catch (IOException ex) {
                game.closeGame();
            }
        } catch (PlayerNotFoundException e) {
            game.closeGame();
        } catch (InvalidSyntaxException | InvalidPositionException e) {
            try {
                playerHandler.sendMessage(Messages.INVALID_SYNTAX);
                playerHandler.takeTurn();
            } catch (IOException ex) {
                game.closeGame();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void placeMine(Battleship.PlayerHandler playerHandler, int row, int col) {
        playerHandler.getMyMap().get(row).set(col, "O");
        playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.MINE.getPoints());
    }


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
