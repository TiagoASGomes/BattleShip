package commands;

import Battleship.Battleship;
import Battleship.PointValues;
import Battleship.ships.Ship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.util.List;

import static commands.CommandHelper.*;

public class BombHandler implements CommandHandler {

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
                throw new RuntimeException(ex);
            }
        } catch (PlayerNotFoundException e) {
            //TODO mensagem de desconecção
            game.closeGame();
        }
    }


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
