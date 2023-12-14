package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Messages.Messages;

import java.util.List;

public class PlaceHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler) {
        List<Ship> shipList = playerHandler.getCharacter().getShipList();
        int[] message;
        try {
            message = getMessage(playerHandler.getMessage());
            checkIfValidPosition(message);
        } catch (NumberFormatException e) {
            playerHandler.sendMessage(Messages.INVALID_SYNTAX);
            return;
        }
        shipList.get(message[0]).setPosition(message[1], message[2]);


    }

    private void checkIfValidPosition(int[] message) {
        //TODO validar se ja existe navio e se esta dentro do mapa
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
