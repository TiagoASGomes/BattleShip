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

public class MineHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            checkPlayerPoints(playerHandler);
            Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponent.getMyMap());
            placeMine(playerHandler, coordinates[0], coordinates[1]);
            playerHandler.takeTurn();
        } catch (NotEnoughPointsException e) {
            try {
                playerHandler.sendMessage(Messages.NOT_ENOUGH_POINTS);
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (PlayerNotFoundException e) {
            //TODO mensagem desconecçao
            game.closeGame();
        } catch (InvalidSyntaxException | InvalidPositionException e) {
            try {
                playerHandler.sendMessage(Messages.INVALID_SYNTAX);
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void placeMine(Battleship.PlayerHandler playerHandler, int row, int col) {
        playerHandler.getMyMap().get(row).set(col, "O");
        playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.MINE.getPoints());
    }


    private void checkPlayerPoints(Battleship.PlayerHandler playerHandler) throws NotEnoughPointsException {
        if (playerHandler.getPlayerPoints() < PointValues.MINE.getPoints()) {
            throw new NotEnoughPointsException(Messages.NOT_ENOUGH_POINTS);
        }
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

    private boolean isNotNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!Character.isDigit(digit)) {
                return true;
            }
        }
        return false;
    }

    private Battleship.PlayerHandler getOpponent(Battleship game, Battleship.PlayerHandler playerHandler) throws PlayerNotFoundException {
        Optional<Battleship.PlayerHandler> opponent = game.getOtherPlayer(playerHandler);
        if (opponent.isEmpty()) {
            throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
        }
        return opponent.get();
    }
}
