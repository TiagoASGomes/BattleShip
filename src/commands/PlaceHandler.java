package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Battleship.ships.ship_parts.ShipPart;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Messages.Messages;

import java.util.List;

public class PlaceHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<Ship> shipList = playerHandler.getCharacter().getPlayerShips();

        int[] message = new int[0];
        try {
            message = getMessage(playerHandler.getMessage());
            if (shipList.get(message[0]).isPlaced()) {
                removeFromMap(shipList.get(message[0]), playerHandler.getMyMap());
            }

            shipList.get(message[0]).setPosition(message[1], message[2]);
            checkIfValidPosition(playerHandler, shipList.get(message[0]));
            playerHandler.sendMessage(Messages.BOAT_PLACED);
            placeShipInMap(shipList.get(message[0]), playerHandler.getMyMap());

        } catch (InvalidSyntaxException e) {
            playerHandler.sendMessage(e.getMessage());

        } catch (InvalidPositionException e) {
            playerHandler.sendMessage(e.getMessage());
            shipList.get(message[0]).removeShip();
        }
    }

    private void removeFromMap(Ship ship, List<List<String>> myMap) {
        for (ShipPart shipPart : ship.getShipParts()) {
            int row = shipPart.getRow();
            int col = shipPart.getCol();

            myMap.get(row).set(col, "~");
        }
    }

    private void placeShipInMap(Ship ship, List<List<String>> myMap) {
        for (ShipPart shipPart : ship.getShipParts()) {
            int row = shipPart.getRow();
            int col = shipPart.getCol();

            myMap.get(row).set(col, "A");
        }
    }

    private void checkIfValidPosition(Battleship.PlayerHandler playerHandler, Ship ship) throws InvalidPositionException {

        List<List<String>> map = playerHandler.getMyMap();
        List<ShipPart> shipPositions = ship.getShipParts();

        for (ShipPart shipPart : shipPositions) {
            int row = shipPart.getRow();
            int col = shipPart.getCol();

            String position = map.get(row).get(col);
            if (!position.equals("~")) {
                throw new InvalidPositionException(Messages.CANNOT_PLACE);
            }
        }
    }

    private int[] getMessage(String message) throws InvalidSyntaxException {
        String[] separated = message.split(" ");
        checkValidInput(separated);

        int[] newMessage = new int[3];
        newMessage[0] = Integer.parseInt(separated[1]);
        newMessage[1] = Integer.parseInt(separated[2]);
        newMessage[2] = separated[3].charAt(0) - 'A' + 1;

        return newMessage;
    }

    private void checkValidInput(String[] separated) throws InvalidSyntaxException {
        if (separated.length > 4) {
            throw new InvalidSyntaxException(Messages.INVALID_SYNTAX);
        }
        if (!Character.isDigit(separated[1].charAt(0)) || !Character.isDigit(separated[2].charAt(0))) {
            throw new InvalidSyntaxException(Messages.INVALID_SYNTAX);
        }
        if (!Character.isAlphabetic(separated[3].charAt(0))) {
            throw new InvalidSyntaxException(Messages.INVALID_SYNTAX);
        }
    }
}
