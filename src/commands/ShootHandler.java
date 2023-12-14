package commands;

import Battleship.Battleship;

import java.util.ArrayList;

public class ShootHandler implements CommandHandler {

    // Lista do player com os barcos
    int row;
    char charCol; //y

    ArrayList<Integer> list = new ArrayList<Integer>();

    @Override
    public void execute(Battleship.PlayerHandler playerHandler) {
        String input = playerHandler.getMessage();
        int col;
        charCol = input.charAt(0);
        col = charCol - 'A' + 1;

        row = Integer.parseInt(input);

        list.get(col).get(row);
        if ((col == 'X' || col == ' ' || col == '*')) {
            throw new IndexOutOfBoundsException("Row out of bounds");

        }
    }
}
