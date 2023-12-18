package Battleship.Character;

import Battleship.Battleship;
import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.util.List;

public class CharacterThree extends Character {
    public CharacterThree(ShipType[] ships) {
        super(CharacterType.ONE);

        for (ShipType shipType : ships) {
            playerShips.add(ShipFactory.create(shipType));
        }
    }

    @Override
    public void special(Battleship.PlayerHandler playerHandler, Battleship game) throws PlayerNotFoundException, InvalidSyntaxException, InvalidPositionException {
        Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
        int[] position = getPosition(playerHandler);
        doSpecial(position, opponent, playerHandler);
    }

    private void doSpecial(int[] position, Battleship.Battleship.PlayerHandler opponent, Battleship.Battleship.PlayerHandler playerHandler) {
        
    }

    private int[] getPosition(Battleship.Battleship.PlayerHandler playerHandler) throws InvalidSyntaxException, InvalidPositionException {
        String[] message = playerHandler.getMessage().split(" ");
        checkValidInput(message);
        int[] positions = new int[2];
        positions[0] = Integer.parseInt(message[1]);
        positions[1] = message[2].charAt(0) - 'A' + 1;
        checkValidPosition(positions, playerHandler.getMyMap());
        return positions;
    }

    private void checkValidPosition(int[] positions, List<List<String>> myMap) throws InvalidPositionException {
        if (positions[0] < 1 || positions[0] >= myMap.size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        if (positions[1] < 1 || positions[1] >= myMap.get(1).size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
    }

    private void checkValidInput(String[] message) throws InvalidSyntaxException {
        if (message.length != 3) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(message[1])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (message[2].charAt(0) < 65 || message[2].charAt(0) > 90) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
    }

    private Battleship.PlayerHandler getOpponent(Battleship game, Battleship.PlayerHandler playerHandler) throws PlayerNotFoundException {
        Battleship.PlayerHandler otherPlayer = game.getPlayers().stream()
                .filter(player -> !player.equals(playerHandler))
                .findFirst()
                .orElse(null);
        if (otherPlayer == null) {
            throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
        }
        return otherPlayer;
    }

    private boolean isNotNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!java.lang.Character.isDigit(digit)) {
                return true;
            }
        }
        return false;
    }
}
