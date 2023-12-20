package commands;

import Battleship.Battleship;
import Battleship.PointValues;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;

import java.io.IOException;

import static commands.CommandHelper.checkPlayerPoints;

public class SpecialHandler implements CommandHandler {

    /**
     * Implements the SPECIAL GameCommand and pays its cost in player points.
     * Calls the Character method special for that PlayerHandler.
     *
     * @param playerHandler receives a PlayerHandler as parameter.
     * @param game          receives a Battleship as parameter.
     */
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        try {
            checkPlayerPoints(playerHandler, PointValues.SPECIAL);
            playerHandler.getCharacter().special(playerHandler, game);
            playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.SPECIAL.getPoints());
        } catch (PlayerNotFoundException e) {
            game.closeGame();
            throw new RuntimeException(e);
        } catch (InvalidSyntaxException | InvalidPositionException | NotEnoughPointsException e) {
            playerHandler.sendMessage(e.getMessage());
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                game.closeGame();
            }
        }

    }

}
