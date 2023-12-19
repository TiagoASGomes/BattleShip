package commands;

import Battleship.Battleship;
import Battleship.ships.Ship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ShootHandler implements CommandHandler {


    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {

        try {
            Battleship.PlayerHandler opponent = getOpponent(game, playerHandler);
            int[] coordinates = getCoordinates(playerHandler.getMessage(), opponent.getMyMap());

            if (checkForMine(opponent, coordinates[0], coordinates[1])) {
                mineExplosion(playerHandler, opponent, coordinates[0], coordinates[1]);
                return;
            }
            checkHit(playerHandler, opponent, coordinates[0], coordinates[1]);
        } catch (PlayerNotFoundException e) {
            //TODO mensagem desconecçao
            game.closeGame();
        } catch (InvalidSyntaxException | InvalidPositionException e) {
            try {
                playerHandler.sendMessage(Messages.INVALID_SYNTAX);
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void checkHit(Battleship.PlayerHandler playerHandler, Battleship.PlayerHandler opponent, int row, int col) {
        Ship ship = opponent.checkIfHit(row, col);

        if (ship != null) {
            playerHandler.winPoint(ship);
            playerHandler.getOppMap().get(row).set(col, "\u001B[31mX\u001B[0m");
            return;
        }
        playerHandler.getOppMap().get(row).set(col, "\u001B[34mX\u001B[0m");
    }

    private void mineExplosion(Battleship.PlayerHandler player, Battleship.PlayerHandler opponent, int row, int col) {
        opponent.getMyMap().get(row).set(col, "\u001B[34mR\u001B[0m");
        player.getOppMap().get(row).set(col, "\u001B[34mR\u001B[0m");

        int randRow = (int) (Math.random() * (player.getMyMap().size() - 4 + 1) + 1);
        int randCol = (int) (Math.random() * (player.getMyMap().get(0).size() - 4 + 1) + 1);
        Ship ship = opponent.checkIfHit(row, col);
        if (ship != null) {
            player.getMyMap().get(randRow).set(randCol, "\u001B[31mX\u001B[0m");
            return;
        }
        player.getMyMap().get(randRow).set(randCol, "\u001B[34mX\u001B[0m");
    }

    /**
     * @param message receives a String as parameter.
     * @param map     receives a List of Lists of Strings as parameter.
     *                creates an array of String by splitting the String message by the spaces.
     *                Takes two ints from that array index 1 and 2, and stores them in an array of length 2.
     * @return returns an array of int type that represents two coordinates of the map.
     * @throws InvalidSyntaxException   if the input is not allowed.
     * @throws InvalidPositionException if the coordinates are off of the map size, if the length is bigger than 1,
     *                                  and if there is already a space, an '*', or 'R' in that coordinate on the map.
     */
    private int[] getCoordinates(String message, List<List<String>> map) throws InvalidSyntaxException, InvalidPositionException {
        String[] separated = message.split(" ");
        checkValidInput(separated);

        int[] coordinates = new int[2];
        coordinates[0] = Integer.parseInt(separated[1]);
        coordinates[1] = separated[2].charAt(0) - 'A' + 1;

        if (coordinates[0] >= map.size() - 1 || coordinates[1] >= map.get(coordinates[0]).size() - 1) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        String positionString = map.get(coordinates[0]).get(coordinates[1]);
        if (positionString.length() > 1) {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }
        char position = positionString.charAt(0);
        if (position == ' ' || position == '*' || position == 'R') {
            throw new InvalidPositionException(Messages.INVALID_POSITION);
        }

        return coordinates;
    }

    /**
     * @param separated receives an array of String as parameter.
     * @throws InvalidSyntaxException throws InvalidSyntaxException if that array is not of length 3,
     *                                if the second index of that array is not a number,
     *                                if the third index of that array is not a char between A and Z.
     */
    private void checkValidInput(String[] separated) throws InvalidSyntaxException {
        if (separated.length != 3) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(separated[1])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (separated[2].charAt(0) < 65 || separated[2].charAt(0) > 90) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
    }

    /**
     * @param number Receives a String
     * @return Returns true if that String is not a number
     */
    private boolean isNotNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!Character.isDigit(digit)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param otherPlayer Receives a PlayerHandler
     * @param row         receives an int that represents a row as parameter
     * @param col         receives another int that represents a column as parameter
     * @return returns true if that coordinate in that PlayerHandler's Map has the char 'O'
     */
    private boolean checkForMine(Battleship.PlayerHandler otherPlayer, int row, int col) {
        return otherPlayer.getMyMap().get(row).get(col).charAt(0) == 'O';
    }

    /**
     * @param game          receives a Battleship game as parameter
     * @param playerHandler receives a PlayerHandler as parameter
     * @return returns a PlayerHandler that is not himself, if there is one,
     * @throws PlayerNotFoundException and throws a PlayerNotFoundException if there is none.
     */
    private Battleship.PlayerHandler getOpponent(Battleship game, Battleship.PlayerHandler playerHandler) throws PlayerNotFoundException {
        Optional<Battleship.PlayerHandler> opponent = game.getOtherPlayer(playerHandler);
        if (opponent.isEmpty()) {
            throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
        }
        return opponent.get();
    }


}
