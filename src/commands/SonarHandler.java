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

public class SonarHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<List<String>> map = playerHandler.getOppMap();

        try {
            checkPlayerPoints(playerHandler, PointValues.SONAR);
            List<List<String>> opponentMap = getOpponent(game, playerHandler).getMyMap();
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponentMap);
            placeSonar(coordinates, map, opponentMap);
            playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.SONAR.getPoints());
        } catch (InvalidSyntaxException | InvalidPositionException | NotEnoughPointsException e) {
            playerHandler.sendMessage(e.getMessage());
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (PlayerNotFoundException e) {
            //TODO mensagem desconecçao
            game.closeGame();
        }
    }


    private void placeSonar(int[] coordinates, List<List<String>> map, List<List<String>> opponentMap) {
        int startRow = coordinates[0] - 1;
        int startCol = coordinates[1] - 1;


        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (checkInvalidPosition(i, j, opponentMap)) {
                    continue;
                }
                if (opponentMap.get(i).get(j).equals("~")) {
                    continue;
                }

                putMarkOnMap(i, j, map);
            }
        }
    }

    private void putMarkOnMap(int row, int col, List<List<String>> map) {
        if (map.get(row).get(col).length() > 1) {
            return;
        }
        map.get(row).set(col, "\033[0;35m?\033[0m");
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
