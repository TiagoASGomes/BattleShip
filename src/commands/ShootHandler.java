package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;

public class ShootHandler implements CommandHandler {


    int row;
    char charCol;


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<List<String>> map = playerHandler.getOppMap();

        String[] input = playerHandler.getMessage().split(" ");
        if (input.length != 3) {
            try {
                playerHandler.sendMessage(Messages.INVALID_SYNTAX);
                playerHandler.takeTurn();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        int col;
        charCol = input[2].charAt(0);
        try {
            validateInput(charCol);
        } catch (InvalidKeyException e) {
            playerHandler.sendMessage(Messages.INVALID_SYNTAX);
            try {
                playerHandler.takeTurn();
                return;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        col = charCol - 'A' + 1;

        row = Integer.parseInt(input[1]);

        String stringPosition = "";
        try {
            stringPosition = map.get(row).get(col);

        } catch (IndexOutOfBoundsException e) {
            playerHandler.sendMessage(Messages.INVALID_SYNTAX);
            try {
                playerHandler.takeTurn();
                return;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }


        char position;

        if (stringPosition.length() == 1) {
            position = stringPosition.charAt(0);
        } else {
            position = stringPosition.charAt(5);
        }


        try {
            checkPosition(position);
        } catch (IndexOutOfBoundsException e) {
            try {
                playerHandler.takeTurn();
                return;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }


        Battleship.PlayerHandler otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(playerHandler))
                .findFirst().orElse(null);
        if (otherPlayer == null) {
            throw new RuntimeException();
        }
        if (checkForMine(otherPlayer, row, col)) {

            otherPlayer.getMyMap().get(row).set(col, "\u001B[34mR\u001B[0m");
            playerHandler.getOppMap().get(row).set(col, "\u001B[34mR\u001B[0m");

            int randRow = (int) (Math.random() * (playerHandler.getMyMap().size() - 4 + 1) + 1);
            int randCol = (int) (Math.random() * (playerHandler.getMyMap().get(0).size() - 4 + 1) + 1);
            Ship ship = otherPlayer.checkIfHit(row, col);
            if (ship != null) {
                playerHandler.getMyMap().get(randRow).set(randCol, "\u001B[31mX\u001B[0m");
                return;
            }
            playerHandler.getMyMap().get(randRow).set(randCol, "\u001B[34mX\u001B[0m");
            return;
        }
        Ship ship = otherPlayer.checkIfHit(row, col);

        if (ship != null) {
            playerHandler.winPoint(ship);
            playerHandler.shootMessage(ship, playerHandler, row, col);
            playerHandler.getOppMap().get(row).set(col, "\u001B[31mX\u001B[0m");
            return;
        }
        playerHandler.getOppMap().get(row).set(col, "\u001B[34mX\u001B[0m");


    }

    private void checkPosition(char position) throws IndexOutOfBoundsException {
        if (position == 'X' || position == ' ' || position == '*' || position == 'R') {
            throw new IndexOutOfBoundsException("Row out of bounds");
        }
    }

    private boolean checkForMine(Battleship.PlayerHandler otherPlayer, int row, int col) {
        return otherPlayer.getMyMap().get(row).get(col).charAt(0) == 'O';
    }


    private void validateInput(char input) throws InvalidKeyException {
        if (input < 65 || input > 90) {
            throw new InvalidKeyException("Wrong letter");
        }
    }

    public int charConverter(int colNumber) {
        return colNumber = charCol - 'A' + 1;
    }

}
