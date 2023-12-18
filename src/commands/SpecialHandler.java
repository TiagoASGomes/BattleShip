package commands;

import Battleship.Battleship;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.PlayerNotFoundException;

import java.io.IOException;

public class SpecialHandler implements CommandHandler {
    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        try {
            checkPlayerPoints(playerHandler);
            playerHandler.getCharacter().special(playerHandler, game);
        } catch (PlayerNotFoundException e) {
            //TODO Fechar jogo
            throw new RuntimeException(e);
        } catch (InvalidSyntaxException | InvalidPositionException e) {
            playerHandler.sendMessage(e.getMessage());
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void checkPlayerPoints(Battleship.PlayerHandler playerHandler) {
        //TODO verificar pontos
    }
}
