package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Battleship.ships.ship_parts.ShipPart;
import MessagesAndPrinter.Messages;

import java.util.List;

public class PlaceHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<Ship> shipList = playerHandler.getCharacter().getPlayerShips();
        int[] message;
        try {
            message = getMessage(playerHandler.getMessage());
        } catch (NumberFormatException e) {
            playerHandler.sendMessage(Messages.INVALID_SYNTAX);
            return;
        }
        shipList.get(message[0]).setPosition(message[1], message[2]);
        try {
            checkIfValidPosition(message, playerHandler, shipList.get(message[0]));
        } catch (IndexOutOfBoundsException e) {
            playerHandler.sendMessage("no");
            shipList.get(message[0]).removeShip();
        }
        playerHandler.sendMessage("yes");


    }

    private void checkIfValidPosition(int[] message, Battleship.PlayerHandler playerHandler, Ship ship) throws IndexOutOfBoundsException {

        List<List<String>> map = playerHandler.getMyMap();
        List<ShipPart> shipPositions = ship.getShipParts();

        for (ShipPart shipPart : shipPositions) {
            int row = shipPart.getRow();
            int col = shipPart.getCol();

            String position = map.get(row).get(col);
            if (!position.equals("~")) {
                throw new IndexOutOfBoundsException();
            }

        }

    }

    private int[] getMessage(String message) throws NumberFormatException {
        String[] separated = message.split(" ");

        int[] newMessage = new int[3];
        newMessage[0] = Integer.parseInt(separated[1]);
        newMessage[1] = Integer.parseInt(separated[2]);
        newMessage[2] = Integer.parseInt(separated[3]);

        return newMessage;
    }
}
