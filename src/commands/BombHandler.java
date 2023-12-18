package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.util.List;

public class BombHandler implements CommandHandler {
    private static final int POINTS_TO_USE = 4;

    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            checkPlayerPoints(playerHandler);
            Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponent.getMyMap());
            doExplosion(coordinates, playerHandler, opponent);
            playerHandler.getPlayerPoints().setPlayerPoints(playerHandler.getPlayerPoints().getPlayerPoints() - POINTS_TO_USE);
        } catch (InvalidSyntaxException | InvalidPositionException | NotEnoughPointsException e) {
            playerHandler.sendMessage(e.getMessage());
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (PlayerNotFoundException e) {
            //TODO Fechar jogo
            throw new RuntimeException(e);
        }
    }

    private void checkPlayerPoints(Battleship.PlayerHandler playerHandler) throws NotEnoughPointsException {
        if (playerHandler.getPlayerPoints().getPlayerPoints() < POINTS_TO_USE) {
            throw new NotEnoughPointsException(Messages.NOT_ENOUGH_POINTS);
        }
    }

    private void doExplosion(int[] coordinates, Battleship.PlayerHandler playerHandler, Battleship.PlayerHandler opponent) {
        int startRow = coordinates[0];
        int startCol = coordinates[1];


        for (int i = startRow; i < startRow + 2; i++) {
            for (int j = startCol; j < startCol + 2; j++) {
                if (checkInvalidCoordinate(i, j, opponent.getMyMap())) {
                    continue;
                }
                String position = opponent.getMyMap().get(i).get(j);
                if (checkInvalidPosition(position)) {
                    continue;
                }
                Ship ship = opponent.checkIfHit(i, j);
                if (ship != null) {
                    playerHandler.winPoint(ship);
                    playerHandler.getOppMap().get(i).set(j, "\u001B[31mX\u001B[0m");
                    continue;
                }
                playerHandler.getOppMap().get(i).set(j, "\u001B[34mX\u001B[0m");
            }
        }
    }

    private boolean checkInvalidPosition(String position) {
        if (position.length() > 1) {
            return true;
        }
        char positionChar = position.charAt(0);
        return positionChar == ' ' || positionChar == '*';
    }


    private boolean checkInvalidCoordinate(int row, int col, List<List<String>> map) {
        return row >= map.size() || col >= map.get(row).size();
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
        if (position == '*') {
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
        Battleship.PlayerHandler otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(playerHandler))
                .findFirst()
                .orElse(null);
        if (otherPlayer == null) {
            throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
        }
        return otherPlayer;
    }

}
