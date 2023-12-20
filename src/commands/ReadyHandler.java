package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import MessagesAndPrinter.Messages;

import java.util.List;

public class ReadyHandler implements CommandHandler {

    /**
     * Executes the READY PreparationCommand.
     * Check's if all the Ships of that player are placed and set's that player has being ready for the battle.
     *
     * @param playerHandler receives a PlayerHandler representing a player as parameter.
     * @param game          receives a Battleship representing the game.
     */
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        if (checkIfAllShipsPlaced(playerHandler)) {
            playerHandler.setReady();
            playerHandler.sendMessage(Messages.READY);
            return;
        }
        playerHandler.sendMessage(Messages.CANT_READY_UP);
    }

    /**
     * Check's if a player has placed all of his Ships on the map.
     *
     * @param playerHandler receives a PlayerHandler representing a player as parameter.
     * @return returns true if that player has already placed all of his Ships on the map.
     */
    private boolean checkIfAllShipsPlaced(Battleship.PlayerHandler playerHandler) {
        List<Ship> shipList = playerHandler.getCharacter().getPlayerShips();
        for (Ship ship : shipList) {
            if (!ship.isPlaced()) {
                return false;
            }
        }
        return true;
    }
}
