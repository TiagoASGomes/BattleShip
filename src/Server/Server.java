package Server;

import Battleship.Battleship;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService service;
    private final List<Battleship> games;
    private int gameIndex;

    public Server(){
        games = new CopyOnWriteArrayList<>();
        gameIndex = 0;
    }
    public void start(){

        try {
            serverSocket = new ServerSocket(8888);
            service = Executors.newFixedThreadPool(5);

            while (true){
                Socket client = serverSocket.accept();
                int index = checkAvailableGame();
                if(index != -1){
                    games.get(index).acceptPlayer(client);
                }else {
                    games.add(new Battleship(client));
                    service.submit(games.get(gameIndex++));
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private int checkAvailableGame() {
        for(int i = 0; i< games.size();i++){
            if(games.get(i).isOpen()){
                return i;
            }
        }
        return -1;
    }
}
