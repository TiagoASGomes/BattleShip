package MessagesAndPrinter;

import Battleship.Battleship;

import java.util.List;

public class Printer {


    public static String createMap(Battleship.PlayerHandler playerHandler) {

        List<List<String>> map = playerHandler.getMyMap();
        List<List<String>> mapOpp = playerHandler.getOppMap();

        StringBuilder finalMap = new StringBuilder();

        for (int i = 0; i < map.size(); i++) {
            List<String> line = map.get(i);
            for (int j = 0; j < line.size(); j++) {
                finalMap.append(line.get(j)).append(" ");
            }
            finalMap.append("\t\t");
            List<String> lineOpp = mapOpp.get(i);
            for (int j = 0; j < lineOpp.size(); j++) {
                finalMap.append(lineOpp.get(j)).append(" ");
            }
            finalMap.append("\n");
        }
        return finalMap.toString();
    }


}

/*Barcos:
    A A

    B B B

    C C C C

    D D D D D

    A A
      A A
*/
