package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Battleship.ships.ship_parts.ShipPart;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import MessagesAndPrinter.Messages;

import java.util.List;

public class PlaceHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        List<Ship> shipList = playerHandler.getCharacter().getPlayerShips();

        int[] message;
        Ship ship = null;
        try {
            message = getPlacementPosition(playerHandler.getMessage(), shipList);
            ship = shipList.get(message[0]);
            if (ship.isPlaced()) {
                removeFromMap(ship, playerHandler.getMyMap());
            }
            ship.setPosition(message[1], message[2]);
            checkIfValidPosition(playerHandler, ship);
            playerHandler.sendMessage(Messages.BOAT_PLACED);
            placeShipInMap(ship, playerHandler.getMyMap());

        } catch (InvalidSyntaxException e) {
            playerHandler.sendMessage(e.getMessage());
        } catch (InvalidPositionException e) {
            playerHandler.sendMessage(e.getMessage());
            ship.removeShip();
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

            myMap.get(row).set(col, ship.getType().getICON());
        }
    }

    private void checkIfValidPosition(Battleship.PlayerHandler playerHandler, Ship ship) throws InvalidPositionException {

        List<List<String>> map = playerHandler.getMyMap();
        List<ShipPart> shipPositions = ship.getShipParts();

        for (ShipPart shipPart : shipPositions) {
            int row = shipPart.getRow();
            int col = shipPart.getCol();

            if (row >= map.size() || col >= map.get(row).size()) {
                throw new InvalidPositionException(Messages.CANNOT_PLACE);
            }

            String position = map.get(row).get(col);

            if (!position.equals("~")) {
                throw new InvalidPositionException(Messages.CANNOT_PLACE);
            }
        }
    }

    private int[] getPlacementPosition(String message, List<Ship> shipList) throws InvalidSyntaxException {
        String[] separated = message.split(" ");
        checkValidInput(separated);

        int[] newMessage = new int[3];
        newMessage[0] = Integer.parseInt(separated[1]);
        newMessage[1] = Integer.parseInt(separated[2]);
        newMessage[2] = separated[3].charAt(0) - 'A' + 1;

        if (newMessage[0] >= shipList.size()) {
            throw new InvalidSyntaxException(Messages.SHIP_DOESNT_EXIST);
        }

        return newMessage;
    }

    private void checkValidInput(String[] separated) throws InvalidSyntaxException {
        if (separated.length > 4) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(separated[1]) || isNotNumber(separated[2])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (separated[3].charAt(0) < 65 || separated[3].charAt(0) > 90) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
    }

    private boolean isNotNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!Character.isDigit(digit)) {
                return true;
            }
        }
        return false;
    }
}
