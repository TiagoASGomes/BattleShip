package commands;

import Battleship.Battleship;
import Battleship.ships.StraightShip;
import Messages.Messages;

import java.util.List;

public class ReadyHandler implements CommandHandler {
    @Override
    public void execute(Battleship.PlayerHandler playerHandler) {
        if (checkIfShipsPlaced(playerHandler)) {
            playerHandler.setReady();
            return;
        }
        playerHandler.sendMessage(Messages.CANT_READY_UP);
    }

    private boolean checkIfShipsPlaced(Battleship.PlayerHandler playerHandler) {
        List<StraightShip> shipList = playerHandler.getCharacter().getShipList();
        for (StraightShip ship : shipList) {
            if (!ship.isPlaced()) {
                return false;
            }
        }
        return true;
    }
}
