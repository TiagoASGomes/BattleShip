package Battleship;

import Battleship.Character.Character;
import Battleship.Character.CharacterFactory;
import Battleship.Character.CharacterType;
import Battleship.Maps.MapType;
import Battleship.ships.Ship;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Colors;
import MessagesAndPrinter.MapString;
import MessagesAndPrinter.Messages;
import MessagesAndPrinter.Printer;
import commands.GameCommands;
import commands.PreparationCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Battleship.PointValues.HIT;
import static Battleship.PointValues.SINK;

public class Battleship implements Runnable {

    private boolean open = true;
    private final List<PlayerHandler> players = new CopyOnWriteArrayList<>();
    private final ExecutorService service;
    private final List<MapType> choices = new CopyOnWriteArrayList<>();
    private boolean finished;

    /**
     * Constructs a Battleship game instance with an initial player connected.
     *
     * @param client The socket representing the connection of the initial player.
     */
    public Battleship(Socket client) {
        players.add(new PlayerHandler(client));
        players.getFirst().sendMessage(Messages.WAIT_FOR_OPPONENT);
        service = Executors.newFixedThreadPool(2);

        finished = false;
    }

    /**
     * Runs the main logic of the Battleship game, including player connection checks,
     * map selection, player readiness checks, game execution, winner determination,
     * and game closure.
     * Overrides the run method of the Runnable interface.
     */
    @Override
    public void run() {


        checkPlayersConnected();

        mapSelection();

        checkPlayersReady();

        int loserIndex = playGame();

        printWinner(loserIndex);

        closeGame();
    }

    /**
     * Prints winner and loser messages to the players.
     *
     * @param loserIndex The index of the losing player in the 'players' list.
     */
    private void printWinner(int loserIndex) {
        players.get(loserIndex).sendMessage(Messages.LOSER);
        getOtherPlayer(players.get(loserIndex)).ifPresent(loser -> loser.sendMessage(Messages.WINNER));
    }

    /**
     * Retrieves the other player (not equal to the given player) from the 'players' list.
     *
     * @param playerHandler The player for which to find the other player.
     * @return An Optional containing the other player, or empty if not found.
     */
    public Optional<PlayerHandler> getOtherPlayer(PlayerHandler playerHandler) {
        return players.stream()
                .filter(player -> !player.equals(playerHandler))
                .findFirst();
    }

    /**
     * Plays the main game loop, taking turns between players until the game is over.
     *
     * @return The index of the losing player in the 'players' list.
     */
    private int playGame() {
        int currentPlayerIndex = new Random().nextInt(players.size());
        while (gameNotOver()) {
            currentPlayerIndex = swapPlayer(currentPlayerIndex);
            PlayerHandler currentPlayer = players.get(swapPlayer(currentPlayerIndex));
            try {
                currentPlayer.playerPoints += 1;
                currentPlayer.takeTurn();
                updateMaps();
            } catch (IOException | PlayerNotFoundException e) {
                closeGame();
            }
        }
        return currentPlayerIndex;
    }

    private void checkForDisconnect() throws PlayerNotFoundException {
        for (PlayerHandler player : players) {
            if (!player.isConnected()) {
                throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
            }
        }
    }

    /**
     * Swaps the player index to toggle between players in a two-player game.
     *
     * @param playerIndex The current player index.
     * @return The index of the other player.
     */
    private int swapPlayer(int playerIndex) {
        return playerIndex == 0 ? 1 : 0;

    }

    /**
     * Waits until all players are ready by continuously checking their readiness.
     * The method sleeps for a short duration between checks to avoid excessive CPU usage.
     * If any player is not ready, it keeps waiting until all players are ready.
     *
     * @throws RuntimeException If an InterruptedException occurs during the waiting process.
     */
    private void checkPlayersReady() {
        while (checkPlayersNotReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Waits until two map choices are available and assigns the chosen maps to the players.
     * Checks continuously for player readiness at a short interval.
     */
    private void mapSelection() {
        while (choices.size() < 2) {
            for (PlayerHandler player : players) {
                if (!player.isConnected()) {
                    closeGame();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        int rand = new Random().nextInt(choices.size());
        sendMapMessage(rand);
        players.get(0).setType(choices.get(rand));
        players.get(1).setType(choices.get(rand));

    }

    /**
     * Waits until the required number of players are connected before starting the game.
     * Checks continuously for player connection at a short interval.
     */
    private void checkPlayersConnected() {
        while (checkPlayersNotConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends messages related to map selection to both players.
     *
     * @param rand The index used to select a map from the choices.
     */
    private void sendMapMessage(int rand) {
        if (choices.get(0).equals(choices.get(1))) {
            broadCast(Messages.SAME_CHOICE);
            broadCast(Messages.MAP_CHOOSEN + choices.getFirst().getMAP_NAME());
        } else {
            broadCast(Messages.DIFFERENT_MAP);
            broadCast(Messages.MAP_CHOOSEN + choices.get(rand).getMAP_NAME());
        }
    }

    /**
     * Sends the same message to both players in the game.
     *
     * @param sameChoice The message to be broadcasted to both players.
     */
    private void broadCast(String sameChoice) {
        players.get(0).sendMessage(sameChoice);
        players.get(1).sendMessage(sameChoice);
    }

    /**
     * Submits a chosen map type to the list of available choices.
     *
     * @param type The chosen map type to be added to the list of choices.
     */
    public void submitMapChoice(MapType type) {
        choices.add(type);
    }

    /**
     * Checks if the game is not over by verifying if any player still has active ships.
     *
     * @return True if the game is not over (at least one player has active ships), false otherwise.
     */
    private boolean gameNotOver() {
        for (PlayerHandler player : players) {
            if (player.allShipsSinked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the maps for all players by sending the current state of their maps to each player.
     */
    private void updateMaps() {
        for (PlayerHandler player : players) {
            player.sendMessage(Printer.createMap(player));
        }
    }

    /**
     * Checks if any player is not ready to proceed with the game.
     *
     * @return True if at least one player is not ready, false otherwise.
     */
    private boolean checkPlayersNotReady() {
        for (PlayerHandler player : players) {
            if (!player.isReady()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the required number of players is not connected to the game.
     *
     * @return True if the number of connected players is not equal to the required number, false otherwise.
     */
    private boolean checkPlayersNotConnected() {
        return players.size() != 2;
    }

    /**
     * Accepts a new player by adding a player handler for the given socket to the game.
     * Marks the game as closed to new players after accepting the first player.
     * Submits each player handler to the executor service for concurrent processing.
     *
     * @param client The socket representing the connection to the new player.
     */
    public void acceptPlayer(Socket client) {
        players.add(new PlayerHandler(client));
        open = false;
        for (PlayerHandler player : players) {
            service.submit(player);
        }
    }

    /**
     * Closes the Battleship game by setting the 'finished' flag to true
     * and closing the connections for all connected players.
     */
    public void closeGame() {
        finished = true;
        broadCast(Messages.QUIT_COMMAND);
        for (PlayerHandler player : players) {
            player.close();
        }
    }

    /**
     * Checks if the game is open for accepting new players.
     *
     * @return True if the game is open, allowing new players to join; false otherwise.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Checks whether the game has finished.
     *
     * @return True if the game has finished, false otherwise.
     */
    public boolean isFinished() {
        return finished;
    }


    public class PlayerHandler implements Runnable {

        private final PrintWriter out;
        private final Socket socket;
        private final BufferedReader in;
        private boolean ready;
        private String message;
        private Character character;
        private List<List<String>> myMap;
        private List<List<String>> oppMap;
        private int playerPoints;
        private MapType type;

        /**
         * Constructs a player handler for handling communication with a player connected via the given socket.
         *
         * @param socket The socket representing the connection to the player.
         */
        public PlayerHandler(Socket socket) {
            this.socket = socket;
            ready = false;

            try {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /**
         * Executes the main logic for handling a player's connection and participation in the Battleship game.
         * The method begins by sending welcome messages to the player, prompting them to choose a map type, and
         * waiting for the selected map type. Once the map type is selected, the player's maps are initialized based
         * on the chosen type. The player is then prompted to choose a character type, and their ships are placed on the
         * map. If an I/O error occurs during communication with the player or if an interruption occurs while waiting
         * for events, appropriate actions are taken, such as closing the connection and printing error messages.
         * Overrides the run method of the Runnable interface.
         */
        @Override
        public void run() {

            try {
                sendMessage(Messages.WELCOME);
                sendMessage(Messages.WELCOME_COMMAND);
                chooseMap();
                waitForMapSelected();

                myMap = generateMap(type.getMAP());
                oppMap = generateMap(type.getMAP());

                chooseCharacter();

                placeShips();

            } catch (IOException | InterruptedException e) {
                closeGame();

            }

        }

        /**
         * Generates a 2D list representing a map from the provided string representation.
         *
         * @param map The string representation of the map with rows separated by newline characters and columns by spaces.
         * @return A 2D list representing the generated map.
         */
        public List<List<String>> generateMap(String map) {

            String[] squareMapSpaces = map.split("\n");

            List<List<String>> mapList = new ArrayList<>();
            for (String squareMapSpace : squareMapSpaces) {
                List<String> temp = new ArrayList<>(Arrays.asList(squareMapSpace.split(" ")));
                mapList.add(temp);
            }
            return mapList;
        }

        /**
         * Allows the player to choose a map type by displaying options and processing the player's choice.
         * The available map types are presented with corresponding numeric options.
         * The player's choice is read from the input stream, and the appropriate map type is selected.
         * In case of an invalid choice, an error message is sent, and the player is prompted to choose again.
         *
         * @throws IOException If an I/O error occurs during communication with the player.
         */
        public void chooseMap() throws IOException {

            sendMessage(Messages.CHOOSE_MAP);
            sendMessage(MapString.ALL_MAPS);
            String playerChoice = in.readLine();
            switch (playerChoice) {
                case "1":
                    submitMapChoice(MapType.SQUARE_MAP);
                    return;
                case "2":
                    submitMapChoice(MapType.HEXA_MAP);
                    return;
                case "3":
                    submitMapChoice(MapType.ROCK_MAP);
                    return;
                default:
                    sendMessage(Messages.NO_SUCH_COMMAND);
                    sendMessage(Messages.GIVE_TURN_PERMISSION);
                    chooseMap();
            }
        }

        /**
         * Allows the player to choose a character type by displaying options and processing the player's choice.
         * The available character types are presented with corresponding numeric options.
         * The player's choice is read from the input stream, and the appropriate character type is selected.
         * In case of an invalid choice, an error message is sent, and the player is prompted to choose again.
         *
         * @throws IOException If an I/O error occurs during communication with the player.
         */
        public void chooseCharacter() throws IOException {

            sendMessage(Messages.GIVE_TURN_PERMISSION);
            sendMessage(Messages.CHOOSE_CHARACTER);
            String playerChoice = in.readLine();
            switch (playerChoice) {
                case "1":
                    this.character = CharacterFactory.create(CharacterType.ONE);
                    return;
                case "2":
                    this.character = CharacterFactory.create(CharacterType.TWO);
                    return;
                case "3":
                    this.character = CharacterFactory.create(CharacterType.THREE);
                    return;
                default:
                    sendMessage(Messages.NO_SUCH_COMMAND);
                    sendMessage(Messages.GIVE_TURN_PERMISSION);
                    chooseCharacter();
            }
        }


        /**
         * Waits until the player has selected a map type before proceeding. It continuously checks
         * if the map type has been set (not null) at regular intervals. Once the map type is selected,
         * it sends a message to the player indicating that they can proceed to the next step.
         *
         * @throws InterruptedException If the waiting thread is interrupted while sleeping.
         */
        private void waitForMapSelected() throws InterruptedException {
            while (type == null) {
                Thread.sleep(100);
            }
            sendMessage(Messages.GIVE_TURN_PERMISSION);
        }

        /**
         * Initiates the player's turn during the game. It prompts the player for their move by sending
         * a message with the current point score and then reads the player's command. It interprets the
         * command and executes the corresponding game action. If the command is not recognized, it
         * recursively calls itself to prompt the player again until a valid command is received.
         *
         * @throws IOException If an I/O error occurs while reading the player's command.
         */
        public void takeTurn() throws IOException, PlayerNotFoundException {
            sendMessage(Messages.GIVE_TURN_PERMISSION2);
            sendMessage(String.format(Messages.POINTS, playerPoints));
            message = in.readLine();
            checkForDisconnect();
            GameCommands command = GameCommands.getCommandFromDescription(message.split(" ")[0]);
            command.getHandler().execute(this, Battleship.this);

            if (command.equals(GameCommands.NOT_FOUND)) {
                takeTurn();
            }
        }

        /**
         * Handles the ship placement phase of the game for the player. It sends messages to instruct
         * the player to place their ships and continuously prompts them for ship placement until all
         * ships are placed. During this process, it provides visual feedback to the player by sending
         * the current state of their map and the ships they have placed. Once all ships are placed,
         * it notifies the player that all ships are placed and instructs them to wait for the opponent.
         * Additionally, it introduces a delay to allow time for the opponent to finish ship placement.
         *
         * @throws IOException          If an I/O error occurs while interacting with the player.
         * @throws InterruptedException If the thread is interrupted during the delay.
         */
        private void placeShips() throws IOException {
            sendMessage(Messages.SHIP_PLACEMENT);
            while (!ready) {
                sendMessage(Messages.GIVE_TURN_PERMISSION);
                sendMessage(Printer.createMap(this));
                sendMessage(Printer.createShipString(this));
                message = in.readLine();
                PreparationCommand command = PreparationCommand.getCommandFromDescription(message.split(" ")[0]);
                command.getHandler().execute(this, Battleship.this);
            }

            try {
                sendMessage(Messages.ALL_SHIPS_PLACED);
                sendMessage(Messages.WAIT_FOR_OPPONENT);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Sends a message to the player using the output stream.
         *
         * @param message The message to be sent to the player.
         */
        public void sendMessage(String message) {
            out.println(message);
        }

        /**
         * Checks if a ship is hit at the specified position on the player's game board.
         *
         * @param row The row coordinate of the target position.
         * @param col The column coordinate of the target position.
         * @return The ship that got hit, or null if no ship is hit at the specified position.
         */
        public Ship checkIfHit(int row, int col) {
            for (Ship ship : character.getPlayerShips()) {
                if (ship.gotHit(row, col)) {
                    String coloredVersion = Colors.RED + ship.getType().getICON() + Colors.RESET;
                    myMap.get(row).set(col, coloredVersion);
                    return ship;
                }
            }
            myMap.get(row).set(col, Colors.BLUE + "X" + Colors.RESET);
            return null;

        }

        /**
         * Checks if all ships belonging to the player are sunk.
         *
         * @return True if all ships are sunk.
         */
        public boolean allShipsSinked() {
            for (Ship ship : character.getPlayerShips()) {
                if (!ship.isSinked()) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Awards points to the player based on hitting and sinking a ship.
         * The player receives points for hitting a ship and additional points
         * if the ship is sunk.
         *
         * @param ship The ship that was hit.
         */
        public void winPoint(Ship ship) {
            playerPoints += HIT.getPoints();
            if (ship.isSinked()) {
                playerPoints += SINK.getPoints();
            }
        }

        /**
         * Closes the player's socket, input stream, and output stream.
         * This method is used to gracefully close the player's connection
         * to the server.
         */
        public void close() {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.out.println(Messages.ERROR);
            }
        }

        /**
         * Checks if the player is ready.
         *
         * @return True if the player is ready.
         */
        public boolean isReady() {
            return ready;
        }

        /**
         * Sets the player as ready.
         */
        public void setReady() {
            ready = true;
        }

        /**
         * Gets the received message from the player.
         *
         * @return The received message from the player.
         */
        public String getMessage() {
            return message;
        }

        /**
         * Gets the character associated with the player.
         *
         * @return The character associated with the player.
         */
        public Character getCharacter() {
            return character;
        }

        /**
         * Gets the map representing the player's own game board.
         *
         * @return The map representing the player's own game board.
         */
        public List<List<String>> getMyMap() {
            return myMap;
        }

        /**
         * Sets the map type for the player.
         *
         * @param type The map type to be set for the player.
         */
        public void setType(MapType type) {
            this.type = type;
        }

        /**
         * Gets the map representing the opponent's game board.
         *
         * @return The map representing the opponent's game board.
         */
        public List<List<String>> getOppMap() {
            return oppMap;
        }

        /**
         * Gets the map type associated with the player.
         *
         * @return The map type associated with the player.
         */
        public MapType getType() {
            return type;
        }

        /**
         * Retrieves the current points accumulated by the player.
         *
         * @return The total points of the player.
         */
        public int getPlayerPoints() {
            return playerPoints;
        }

        /**
         * Sets the player's points to the specified value.
         *
         * @param point The new point value for the player.
         */
        public void setPlayerPoints(int point) {
            this.playerPoints = point;
        }

        /**
         * Checks if the player is still connected to the server.
         *
         * @return {@code true} if the player's socket is not closed, indicating an active connection;
         * {@code false} otherwise.
         */
        public boolean isConnected() {
            return !socket.isClosed();
        }
    }


}



