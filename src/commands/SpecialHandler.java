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
