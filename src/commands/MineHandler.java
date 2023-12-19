package commands;

import Battleship.Battleship;
import Battleship.PointValues;
import Exceptions.NotEnoughPointsException;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;

public class MineHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            checkPlayerPoints(playerHandler);
        } catch (NotEnoughPointsException e) {
            playerHandler.sendMessage(Messages.NOT_ENOUGH_POINTS);
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }


        List<List<String>> map = playerHandler.getMyMap();

        String[] message = playerHandler.getMessage().split(" ");

        int row = Integer.parseInt(message[1]);
        char charCol = message[2].charAt(0);
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

        int col = charCol - 'A' + 1;

        String stringPosition;
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

        playerHandler.getMyMap().get(row).set(col, "O");
        playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.MINE.getPoints());
        try {
            playerHandler.takeTurn();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void checkPosition(char position) throws IndexOutOfBoundsException {
        if (position != '~') {
            throw new IndexOutOfBoundsException("Row out of bounds.");
        }
    }


    private void validateInput(char input) throws InvalidKeyException {
        if (input < 65 || input > 90) {
            throw new InvalidKeyException("Wrong letter");
        }
    }

    private void checkPlayerPoints(Battleship.PlayerHandler playerHandler) throws NotEnoughPointsException {
        if (playerHandler.getPlayerPoints() < PointValues.MINE.getPoints()) {
            throw new NotEnoughPointsException(Messages.NOT_ENOUGH_POINTS);
        }
    }
}
