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

    /**
     * Main method of the class Server
     * Creates a new Server and starts it.
     *
     * @param args Array of Strings
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    /**
     * Constructor method from Server class,
     * Creates a list that will hold the active games.
     */
    public Server() {
        games = new CopyOnWriteArrayList<>();
        gameIndex = 0;
    }

    /**
     * Starts a new server in the specified port (port 8888),
     * While server is open, server accepts players,
     * Check if there is an available game and if there isn't, a game is open,
     * If there is an open game, it accepts another player to join the game.
     */
    public void start() {

        try {
            serverSocket = new ServerSocket(8888);
            service = Executors.newCachedThreadPool();

            while (!serverSocket.isClosed()) {

                Socket client = serverSocket.accept();
                Optional<Battleship> game = checkAvailableGame();

                if (game.isEmpty()) {
                    games.add(new Battleship(client));
                    service.submit(games.getLast());
                    continue;
                }

                game.get().acceptPlayer(client);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Removes finished Battleship games from list.
     */
    private void removeFinishedGames() {
        games.stream()
                .filter(Battleship::isFinished)
                .forEach(games::remove);
    }

    /**
     * calls removeFinishedGames method.
     *
     * @return Battleship game if there is one open.
     */
    private Optional<Battleship> checkAvailableGame() {
        removeFinishedGames();
        return games.stream()
                .filter(Battleship::isOpen)
                .findFirst();
    }
}
