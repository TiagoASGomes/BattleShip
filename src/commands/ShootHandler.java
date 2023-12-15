package commands;

import Battleship.Battleship;

import java.io.IOException;
import java.util.List;

public class ShootHandler implements CommandHandler {

    // Lista do player com os barcos
    int row;
    char charCol; //y


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<List<String>> map = playerHandler.getOppMap();
        String[] input = playerHandler.getMessage().split(" ");
        int col;
        charCol = input[1].charAt(0);
        col = charCol - 'A' + 1;

        row = Integer.parseInt(input[2]);

        char position = map.get(row).get(col).charAt(0);
        try {
            checkPosition(position);
        } catch (IndexOutOfBoundsException e) {
            try {
                playerHandler.takeTurn();
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

        if (otherPlayer.checkIfHit(row, col)) {
            playerHandler.getOppMap().get(row).set(col, "\u001B[31mX\u001B[0m");
            return;
        }
        playerHandler.getOppMap().get(row).set(col, "\u001B[34mX\u001B[0m");

    }

    private static void checkPosition(char position) throws IndexOutOfBoundsException {
        if ((position == 'X' || position == ' ' || position == '*')) {
            throw new IndexOutOfBoundsException("Row out of bounds");

        }
    }
}
