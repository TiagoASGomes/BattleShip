package Battleship;

import Battleship.Character.Character;
import Battleship.Character.CharacterFactory;
import Battleship.Character.CharacterType;
import Battleship.Maps.MapType;
import Battleship.ships.Ship;
import MessagesAndPrinter.Colors;
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

    public boolean isFinished() {
        return finished;
    }

    public Battleship(Socket client) {
        players.add(new PlayerHandler(client));
        service = Executors.newFixedThreadPool(2);

        finished = false;
    }


    @Override
    public void run() {


        checkPlayersConnected();

        mapSelection();

        checkPlayersReady();

        int loserIndex = playGame();

        printWinner(loserIndex);

        closeGame();
    }

    public void closeGame() {
        finished = true;
        for (PlayerHandler player : players) {
            player.close();
        }
    }

    private void printWinner(int loserIndex) {
        players.get(loserIndex).sendMessage(Messages.LOSER);
        getOtherPlayer(players.get(loserIndex)).ifPresent(loser -> loser.sendMessage(Messages.WINNER));
    }

    public Optional<PlayerHandler> getOtherPlayer(PlayerHandler playerHandler) {
        return players.stream()
                .filter(player -> !player.equals(playerHandler))
                .findFirst();
    }

    private int playGame() {
        int currentPlayerIndex = new Random().nextInt(players.size());
        while (gameNotOver()) {
            currentPlayerIndex = swapPlayer(currentPlayerIndex);
            PlayerHandler currentPlayer = players.get(swapPlayer(currentPlayerIndex));
            try {
                currentPlayer.playerPoints += 1;
                currentPlayer.takeTurn();
                updateMaps();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return currentPlayerIndex;
    }

    private int swapPlayer(int playerIndex) {
        return playerIndex == 0 ? 1 : 0;

    }

    private void checkPlayersReady() {
        while (checkPlayersNotReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void mapSelection() {
        while (choices.size() < 2) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        int rand = new Random().nextInt(choices.size());
        players.get(0).setType(choices.get(rand));
        players.get(1).setType(choices.get(rand));

    }

    private void checkPlayersConnected() {
        while (checkPlayersNotConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void submitMapChoice(MapType type) {
        choices.add(type);
    }

    private boolean gameNotOver() {
        for (PlayerHandler player : players) {
            if (player.allShipsSinked()) {
                return false;
            }
        }
        return true;
    }

    private void updateMaps() {
        for (PlayerHandler player : players) {
            player.sendMessage(Printer.createMap(player));
        }
    }

    public List<PlayerHandler> getPlayers() {
        return players;
    }

    private boolean checkPlayersNotReady() {
        for (PlayerHandler player : players) {
            if (!player.isReady()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPlayersNotConnected() {
        return players.size() != 2;
    }

    public boolean isOpen() {
        return open;
    }

    public void acceptPlayer(Socket client) {
        players.add(new PlayerHandler(client));
        open = false;
        for (PlayerHandler player : players) {
            service.submit(player);
        }
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

        public List<List<String>> generateMap(String map) {

            String[] squareMapSpaces = map.split("\n");

            List<List<String>> mapList = new ArrayList<>();
            for (String squareMapSpace : squareMapSpaces) {
                List<String> temp = new ArrayList<>(Arrays.asList(squareMapSpace.split(" ")));
                mapList.add(temp);
            }
            return mapList;
        }

        public void chooseMap() throws IOException {

            sendMessage(Messages.CHOOSE_MAP);
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
                    chooseMap();
            }
        }

        public void chooseCharacter() throws IOException {

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
                    chooseCharacter();
            }
        }


        @Override
        public void run() {

            try {
                sendMessage(Messages.WELCOME);

                chooseMap();
                waitForMapSelected();

                myMap = generateMap(type.getMAP());
                oppMap = generateMap(type.getMAP());

                chooseCharacter();

                placeShips();

            } catch (IOException e) {
                System.out.println("Something went wrong with the server. Connection closing...");
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        private void waitForMapSelected() throws InterruptedException {
            while (type == null) {
                Thread.sleep(100);
            }
        }


        public void takeTurn() throws IOException {
            sendMessage(Messages.YOUR_TURN);
            sendMessage(String.format(Messages.POINTS, playerPoints));
            message = in.readLine();
            GameCommands command = GameCommands.getCommandFromDescription(message.split(" ")[0]);
            command.getHandler().execute(this, Battleship.this);

            if (command.equals(GameCommands.NOT_FOUND)) {
                takeTurn();
            }
        }

        private void placeShips() throws IOException {
            sendMessage(Messages.SHIP_PLACEMENT);
            while (!ready) {
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

        public void sendMessage(String message) {
            out.println(message);
        }

        public boolean isReady() {
            return ready;
        }

        public void setReady() {
            ready = true;
        }

        public String getMessage() {
            return message;
        }

        public Character getCharacter() {
            return character;
        }

        public List<List<String>> getMyMap() {
            return myMap;
        }

        public void setType(MapType type) {
            this.type = type;
        }

        public List<List<String>> getOppMap() {
            return oppMap;
        }

        public MapType getType() {
            return type;
        }

        public Ship checkIfHit(int row, int col) {
            for (Ship ship : character.getPlayerShips()) {
                if (ship.gotHit(row, col)) {
                    String coloredVersion = Colors.RED + ship.getType().getICON() + Colors.RESET;
                    myMap.get(row).set(col, coloredVersion);
                    sendMessage(Messages.BOOM);
                    return ship;
                }
                if (ship.isSinked()) {
                    sendMessage(Messages.KABOOM);
                }
            }
            sendMessage(Messages.MISSED);
            myMap.get(row).set(col, Colors.BLUE + "X" + Colors.RESET);
            return null;

        }

        public boolean allShipsSinked() {
            for (Ship ship : character.getPlayerShips()) {
                if (!ship.isSinked()) {
                    return false;
                }
            }
            return true;
        }

        public void winPoint(Ship ship) {
            playerPoints += HIT.getPoints();
            if (ship.isSinked()) {
                playerPoints += SINK.getPoints();
            }
        }

        public int getPlayerPoints() {
            return playerPoints;
        }

        public void setPlayerPoints(int point) {
            this.playerPoints = point;
        }


        public void close() {
            try {
                socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}



