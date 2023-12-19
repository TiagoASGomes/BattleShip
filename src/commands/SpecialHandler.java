package commands;

import Battleship.Battleship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.io.IOException;

public class SpecialHandler implements CommandHandler {
    private static final int POINTS_TO_USE = 7;

    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        try {
            checkPlayerPoints(playerHandler);
            playerHandler.getCharacter().special(playerHandler, game);
            playerHandler.getPlayerPoints().setPlayerPoints(playerHandler.getPlayerPoints().getPlayerPoints() - POINTS_TO_USE);
        } catch (PlayerNotFoundException e) {
            //TODO Fechar jogo
            throw new RuntimeException(e);
        } catch (InvalidSyntaxException | InvalidPositionException | NotEnoughPointsException e) {
            playerHandler.sendMessage(e.getMessage());
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void checkPlayerPoints(Battleship.PlayerHandler playerHandler) throws NotEnoughPointsException {
        if (playerHandler.getPlayerPoints().getPlayerPoints() < POINTS_TO_USE) {
            throw new NotEnoughPointsException(Messages.NOT_ENOUGH_POINTS);
        }
    }
}
