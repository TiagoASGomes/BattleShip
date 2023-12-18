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
            String position = opponentMap.get(row).get(i);
            if (checkInvalidPosition(position)) {
                continue;
            }
            Ship ship = opponent.checkIfHit(row, i);
            if (ship != null) {
                playerHandler.winPoint(ship);
                playerHandler.getOppMap().get(row).set(i, "\u001B[31mX\u001B[0m");
                continue;
            }
            playerHandler.getOppMap().get(row).set(i, "\u001B[34mX\u001B[0m");
        }
    }

    private boolean checkInvalidPosition(String position) {
        if (position.length() > 1) {
            return true;
        }
        char positionChar = position.charAt(0);
        return positionChar == ' ' || positionChar == '*';
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
