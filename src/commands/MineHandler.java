package commands;

import Battleship.Battleship;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;

public class MineHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

       /* if (playerHandler.getPlayerPoints().getPlayerPoints()) < 2){
    playerHandler.sendMessage(TODO MESSAGE DONT HAVE ENOUGH POINTS);
    return;

}*/

//check if player has points. if not, sout message and return.


        List<List<String>> map = playerHandler.getMyMap();

        String[] message = playerHandler.getMessage().split(" ");

        int row = Integer.parseInt(message[1]);
        char charCol = message[2].charAt(0);
        try {
            validateInput(charCol);
        } catch (InvalidKeyException e) {
            playerHandler.sendMessage(Messages.INVALID_SYNTAX);
            try {
                playerHandler.placeMine();
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
                playerHandler.placeMine();
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
                playerHandler.placeMine();
                return;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        playerHandler.getMyMap().get(row).set(col, "O");
        // TODO perde 2 pontos
        //playerHandler.getPlayerPoints().setPlayerPoints(playerHandler.getPlayerPoints().getPlayerPoints()-2)
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
}
