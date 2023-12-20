package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Battleship.ships.ship_parts.ShipPart;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import MessagesAndPrinter.Messages;

import java.util.List;

import static commands.CommandHelper.isNotNumber;

public class PlaceHandler implements CommandHandler {

    /**
     * Executes the PLACE PreparationCommand to place each Ship in the map.
     * Calls methods setPosition and placeShipInMap, and checks position validations.
     *
     * @param playerHandler receives a PlayerHandler as parameter.
     * @param game          receives a Battleship representing a game as parameter.
     */
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

    /**
     * It removes the Ship from the map by replacing the String on each location it occupies
     * for the default String of the map.
     *
     * @param ship  receives a Ship as parameter.
     * @param myMap receives List of a List of Strings representing a map.
     */
    private void removeFromMap(Ship ship, List<List<String>> myMap) {
        for (ShipPart shipPart : ship.getShipParts()) {
            int row = shipPart.getRow();
            int col = shipPart.getCol();

            myMap.get(row).set(col, "~");
        }
    }

    /**
     * It draws the Ship on the map by replacing the String on each location for the String
     * that represents that specific Ship type.
     *
     * @param ship  receives a Ship as parameter.
     * @param myMap receives List of a List of Strings representing a map.
     */
    private void placeShipInMap(Ship ship, List<List<String>> myMap) {
        for (ShipPart shipPart : ship.getShipParts()) {
            int row = shipPart.getRow();
            int col = shipPart.getCol();

            myMap.get(row).set(col, ship.getType().getICON());
        }
    }

    /**
     * Check's if the Ship fits on those coordinates of the map.
     *
     * @param playerHandler receives a PlayerHandler representing a player.
     * @param ship          receives a Ship.
     * @throws InvalidPositionException if the String of that position on the map is not equal to '~',
     *                                  or if the row and column for any part of the Ship go off the boundaries of the map.
     */

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

    /**
     * Takes the input message and returns an array of ints representing the Ship that is being placed,
     * and the row and column for the intended location.
     *
     * @param message  receives a String representing the message input.
     * @param shipList receives a List of Ships as in the Ships owned by that player.
     * @return returns an array of ints.
     * @throws InvalidSyntaxException if the int representing the Ship
     *                                is bigger than the List of Ships owned by the player.
     */
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

    /**
     * Check's if the user input is valid.
     *
     * @param separated receives an array of String as parameter.
     * @throws InvalidSyntaxException throws InvalidSyntaxException if that array is not of length 4,
     *                                if the second or third index of that array is not a number,
     *                                and if the fourth index of that array is not a char between A and Z.
     */
    private void checkValidInput(String[] separated) throws InvalidSyntaxException {
        if (separated.length != 4) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(separated[1]) || isNotNumber(separated[2])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (separated[3].charAt(0) < 65 || separated[3].charAt(0) > 90) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
    }

}
