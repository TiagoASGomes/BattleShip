package Battleship.Character;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Battleship.ships.ShipFactory;
import Battleship.ships.ShipType;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Colors;
import MessagesAndPrinter.Messages;

import java.util.List;

import static commands.CommandHelper.*;

public class CharacterOne extends Character {


    public CharacterOne(ShipType[] ships) {
        super(CharacterType.ONE);

        for (ShipType shipType : ships) {
            playerShips.add(ShipFactory.create(shipType));
        }
    }

    @Override
    public void special(Battleship.PlayerHandler playerHandler, Battleship game) throws PlayerNotFoundException, InvalidSyntaxException, InvalidPositionException {
        Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
        int row = getRow(playerHandler.getMessage());
        checkValidRow(row, opponent.getMyMap());
        doSpecial(row, opponent, playerHandler);
    }

    private void doSpecial(int row, Battleship.PlayerHandler opponent, Battleship.PlayerHandler playerHandler) {
        List<List<String>> playerMap = playerHandler.getOppMap();
        List<List<String>> opponentMap = opponent.getMyMap();
        for (int i = 1; i < playerMap.get(row).size() - 2; i++) {
            if (checkInvalidPosition(row, i, opponentMap)) {
                continue;
            }
            if (checkForMine(opponent, row, i)) {
                mineExplosion(playerHandler, opponent, row, i);
                continue;
            }
            Ship ship = opponent.checkIfHit(row, i);
            if (ship != null) {
                playerHandler.winPoint(ship);
                playerMap.get(row).set(i, Colors.RED + "X" + Colors.RESET);
                continue;
            }
            playerMap.get(row).set(i, Colors.BLUE + "X" + Colors.RESET);
        }
    }


    private void checkValidRow(int row, List<List<String>> myMap) throws InvalidPositionException {
        if (row < 1 || row >= myMap.size() - 2) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
    }

    private int getRow(String message) throws InvalidSyntaxException {
        String[] splitMessage = message.split(" ");
        checkValidInput(splitMessage);
        return Integer.parseInt(splitMessage[1]);
    }


    private void checkValidInput(String[] message) throws InvalidSyntaxException {
        if (message.length != 2) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(message[1])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }


    }


}
