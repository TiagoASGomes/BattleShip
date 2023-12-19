package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.util.List;

import static commands.CommandHelper.*;

public class ShootHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponent.getMyMap());

            if (checkForMine(opponent, coordinates[0], coordinates[1])) {
                mineExplosion(playerHandler, opponent, coordinates[0], coordinates[1]);
                return;
            }
            checkHit(playerHandler, opponent, coordinates[0], coordinates[1]);
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
        }

    }

    private void checkHit(Battleship.PlayerHandler playerHandler, Battleship.PlayerHandler opponent, int row, int col) {
        Ship ship = opponent.checkIfHit(row, col);

        if (ship != null) {
            playerHandler.winPoint(ship);
            if (ship.isSinked()) {
                playerHandler.sendMessage(Messages.KABOOM);
            } else {
                playerHandler.sendMessage(Messages.BOOM);
            }
            playerHandler.getOppMap().get(row).set(col, "\u001B[31mX\u001B[0m");
            return;
        }
        playerHandler.sendMessage(Messages.MISSED);
        playerHandler.getOppMap().get(row).set(col, "\u001B[34mX\u001B[0m");
    }

    private void mineExplosion(Battleship.PlayerHandler player, Battleship.PlayerHandler opponent, int row, int col) {
        opponent.getMyMap().get(row).set(col, "\u001B[34mR\u001B[0m");
        player.getOppMap().get(row).set(col, "\u001B[34mR\u001B[0m");

        int randRow = (int) (Math.random() * (player.getMyMap().size() - 4 + 1) + 1);
        int randCol = (int) (Math.random() * (player.getMyMap().get(0).size() - 4 + 1) + 1);
        Ship ship = opponent.checkIfHit(row, col);
        if (ship != null) {
            player.getMyMap().get(randRow).set(randCol, "\u001B[31mX\u001B[0m");
            return;
        }
        player.getMyMap().get(randRow).set(randCol, "\u001B[34mX\u001B[0m");
    }

    private int[] getCoordinates(String message, List<List<String>> map) throws InvalidSyntaxException, InvalidPositionException {
        String[] separated = message.split(" ");
        checkValidInput(separated);

        int[] coordinates = new int[2];
        coordinates[0] = Integer.parseInt(separated[1]);
        coordinates[1] = separated[2].charAt(0) - 'A' + 1;

        if (checkInvalidPosition(coordinates[0], coordinates[1], map)) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }


        return coordinates;
    }


    private boolean checkForMine(Battleship.PlayerHandler otherPlayer, int row, int col) {
        return otherPlayer.getMyMap().get(row).get(col).charAt(0) == 'O';
    }


}
