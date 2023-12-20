package MessagesAndPrinter;

import Battleship.Battleship;
import Battleship.ships.Ship;

import java.util.ArrayList;
import java.util.List;

public class Printer {
    /**
     * Receives a Battleship PlayerHandler object (playerHandler),
     * @param playerHandler object representing the player's handler in the Battleship game,
     * @return a String of the finalMap.
     */
    public static String createMap(Battleship.PlayerHandler playerHandler) {

        List<List<String>> map = playerHandler.getMyMap();
        List<List<String>> mapOpp = playerHandler.getOppMap();

        StringBuilder finalMap = new StringBuilder();

        for (int i = 0; i < map.size(); i++) {
            List<String> line = map.get(i);
            for (String position : line) {
                if (position.equals("*")) {
                    position = Colors.YELLOW + position + Colors.RESET;
                }
                finalMap.append(position).append(" ");
            }
            finalMap.append("\t\t");
            List<String> lineOpp = mapOpp.get(i);
            for (String position : lineOpp) {
                if (position.equals("*")) {
                    position = Colors.YELLOW + position + Colors.RESET;
                }
                finalMap.append(position).append(" ");
            }
            finalMap.append("\n");
        }
        return finalMap.toString();
    }

    /**
     * This method is responsible for creating a formatted string that represents the player's ships,
     * @param playerHandler object representing the player's handler in the Battleship game,
     * @return a formatted string obtained by converting the StringBuilder to a regular string.
     */
    public static String createShipString(Battleship.PlayerHandler playerHandler) {
        List<String> shipsText = createShipTextArray(playerHandler.getCharacter().getPlayerShips());
        StringBuilder messageToPrint = new StringBuilder();

        for (int i = 0; i < shipsText.size(); i++) {
            messageToPrint.append(i).append(" - ").append(shipsText.get(i)).append("\n");
        }
        return messageToPrint.toString();
    }

    /**
     * Receives a list of Ship objects representing player's ships,
     * @param playerShips list of Ship objects representing player's ships,
     * @return a List of String representations of the ships.
     */
    private static List<String> createShipTextArray(List<Ship> playerShips) {
        List<String> ships = new ArrayList<>();
        for (Ship ship : playerShips) {
            ships.add(getShipString(ship));
        }
        return ships;
    }

    /**
     * Receives Ship, checks his type and if it is rotated or not,
     * @param ship receives a Ship,
     * @return String of the ship that represents is form.
     */
    private static String getShipString(Ship ship) {
        return switch (ship.getType()) {
            case BIG_SHIP -> ship.isRotated() ? TextShips.BIG_SHIP_V : TextShips.BIG_SHIP_H;
            case CARRIER -> ship.isRotated() ? TextShips.CARRIER_V : TextShips.CARRIER_H;
            case SUBMARINE -> ship.isRotated() ? TextShips.SUBMARINE_V : TextShips.SUBMARINE_H;
            case DESTROYER -> ship.isRotated() ? TextShips.DESTROYER_V : TextShips.DESTROYER_H;
            case CRUISER -> ship.isRotated() ? TextShips.CRUISER_V : TextShips.CRUISER_H;
            case CROSS_SHIP -> TextShips.CROSSSHIP;
            case BATTLECRUISER -> ship.isRotated() ? TextShips.BATTLECRUISER_V : TextShips.BATTLECRUISER_H;
        };
    }
}