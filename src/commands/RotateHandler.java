package commands;

import Battleship.Battleship;
import Battleship.ships.StraightShip;
import Messages.Messages;

import java.util.List;

public class RotateHandler implements CommandHandler {

    @Override
    public void execute(Battleship.PlayerHandler playerHandler) {
        List<StraightShip> shipList = playerHandler.getCharacter().getPlayerShips();
        int boatIndex;
        try {
            boatIndex = getBoatIndex(playerHandler.getMessage());
        } catch (NumberFormatException e) {
            playerHandler.sendMessage(Messages.INVALID_SYNTAX);
            return;
        }
        shipList.get(boatIndex).rotate();

    }

    private int getBoatIndex(String message) throws NumberFormatException, ArrayIndexOutOfBoundsException {
        String[] separated = message.split(" ");

        return Integer.parseInt(separated[1]);
    }
}
