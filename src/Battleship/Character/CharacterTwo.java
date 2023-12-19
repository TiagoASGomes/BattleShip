package Battleship.Character;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.util.List;

import static commands.CommandHelper.*;

public class CharacterTwo extends Character {

    public CharacterTwo(ShipType[] ships) {
        super(CharacterType.TWO);

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

    private void doSpecial(int[] position, Battleship.PlayerHandler opponent, Battleship.PlayerHandler playerHandler) {
        int row = position[0] - 2;
        for (int col = position[1] - 2; col < position[1] + 3; col++) {
            checkHit(opponent, playerHandler, row++, col);
        }
        row = position[0] + 2;
        for (int col = position[1] - 2; col < position[1] + 3; col++) {
            checkHit(opponent, playerHandler, row--, col);
        }
    }

    private void checkHit(Battleship.PlayerHandler opponent, Battleship.PlayerHandler playerHandler, int row, int col) {
        List<List<String>> playerMap = playerHandler.getOppMap();
        List<List<String>> opponentMap = opponent.getMyMap();
        String positionString;

        if (checkInvalidPosition(row, col, opponentMap)) {
            return;
        }
        Ship ship = opponent.checkIfHit(row, col);
        if (ship != null) {
            playerHandler.winPoint(ship);
            playerMap.get(row).set(col, "\u001B[31mX\u001B[0m");
            return;
        }
        playerMap.get(row).set(col, "\u001B[34mX\u001B[0m");
    }
    

    private int[] getPosition(Battleship.PlayerHandler playerHandler) throws InvalidSyntaxException, InvalidPositionException {
        String[] message = playerHandler.getMessage().split(" ");
        checkValidInput(message);
        int[] positions = new int[2];
        positions[0] = Integer.parseInt(message[1]);
        positions[1] = message[2].charAt(0) - 'A' + 1;
        checkValidOutOfBounds(positions, playerHandler.getMyMap());
        return positions;
    }

    private void checkValidOutOfBounds(int[] positions, List<List<String>> myMap) throws InvalidPositionException {
        if (positions[0] < 1 || positions[0] >= myMap.size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        if (positions[1] < 1 || positions[1] >= myMap.get(1).size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
    }


}
