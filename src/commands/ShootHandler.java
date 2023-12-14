package commands;

import Battleship.Battleship;

import java.util.List;

public class ShootHandler implements CommandHandler {

    // Lista do player com os barcos
    int row;
    char charCol; //y


    @Override
    public void execute(Battleship.PlayerHandler playerHandler) {
        List<List<String>> map = playerHandler.getMyMap();
        String input = playerHandler.getMessage();
        int col;
        charCol = input.charAt(0);
        col = charCol - 'A' + 1;

        row = Integer.parseInt(input);

        char position = map.get(row).get(col).charAt(0);
        if ((position == 'X' || position == ' ' || position == '*')) {
            throw new IndexOutOfBoundsException("Row out of bounds");

        }
    }
}
