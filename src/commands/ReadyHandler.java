package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Messages.Messages;

import java.util.List;

public class ReadyHandler implements CommandHandler {
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        if (checkIfShipsPlaced(playerHandler)) {
            playerHandler.setReady();
            playerHandler.sendMessage(Messages.READY);
            return;
        }
        playerHandler.sendMessage(Messages.CANT_READY_UP);
    }

    private boolean checkIfShipsPlaced(Battleship.PlayerHandler playerHandler) {
        List<Ship> shipList = playerHandler.getCharacter().getPlayerShips();
        for (Ship ship : shipList) {
            if (!ship.isPlaced()) {
                return false;
            }
        }
        return true;
    }
}
