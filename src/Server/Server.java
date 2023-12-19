package Server;

import Battleship.Battleship;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService service;
    private final List<Battleship> games;
    private int gameIndex;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public Server() {
        games = new CopyOnWriteArrayList<>();
        gameIndex = 0;
    }

    public void start() {

        try {
            serverSocket = new ServerSocket(8888);
            service = Executors.newCachedThreadPool();

            while (!serverSocket.isClosed()) {

                Socket client = serverSocket.accept();
                Optional<Battleship> game = checkAvailableGame();

                if (game.isEmpty()) {
                    games.add(new Battleship(client));
                    service.submit(games.get(gameIndex++));
                    continue;
                }

                game.get().acceptPlayer(client);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void removeFinishedGames() {
        games.stream()
                .filter(Battleship::isFinished)
                .forEach(games::remove);
    }

    private Optional<Battleship> checkAvailableGame() {
        removeFinishedGames();
        return games.stream()
                .filter(Battleship::isOpen)
                .findFirst();
    }
}
