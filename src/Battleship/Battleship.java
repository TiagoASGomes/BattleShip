package Battleship;

import java.net.Socket;

public class Battleship implements Runnable{

    private boolean open = true;
    private final Socket player1;
    private Socket player2;

    public Battleship(Socket client) {
        this.player1 = client;
    }

    @Override
    public void run() {

    }

    public boolean isOpen() {
        return open;
    }

    public void acceptPlayer(Socket client) {
        player2 = client;
    }

    public class PlayerHandler{
        
    }
}
