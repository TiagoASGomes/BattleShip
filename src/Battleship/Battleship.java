package Battleship;

import Battleship.Character.Character;
import Battleship.Character.CharacterFactory;
import Battleship.Character.CharacterType;
import Battleship.Maps.MapType;
import Battleship.ships.Ship;
import MessagesAndPrinter.Messages;
import MessagesAndPrinter.Printer;
import commands.GameCommands;
import commands.PreparationCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Battleship.PlayerPoints.pointForHit;
import static Battleship.PlayerPoints.pointForSinking;

public class Battleship implements Runnable {

    private boolean open = true;
    private final List<PlayerHandler> players = new CopyOnWriteArrayList<>();
    private ExecutorService service;


    public Battleship(Socket client) {
        players.add(new PlayerHandler(client));
    }


    @Override
    public void run() {

        service = Executors.newFixedThreadPool(2);
        while (checkPlayersNotConnected()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        while (checkPlayersNotReady()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        boolean firstWon = false;

        while (gameNotOver()) {
            try {
                players.get(0).playerPoints.setPlayerPoints(players.get(0).playerPoints.getPlayerPoints() + 1);
                players.get(0).takeTurn();

                updateMaps();
                if (!gameNotOver()) {
                    firstWon = true;
                    break;
                }
                players.get(1).playerPoints.setPlayerPoints(players.get(1).playerPoints.getPlayerPoints() + 1);
                players.get(1).takeTurn();
                updateMaps();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (firstWon) {
            players.get(0).sendMessage(Messages.WINNER);
            players.get(1).sendMessage(Messages.LOSER);
        } else {
            players.get(1).sendMessage(Messages.WINNER);
            players.get(0).sendMessage(Messages.LOSER);
        }


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
        private final PlayerPoints playerPoints;


        public PlayerHandler(Socket socket) {
            this.socket = socket;
            ready = false;
            playerPoints = new PlayerPoints();
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
            for (int i = 0; i < squareMapSpaces.length; i++) {
                List<String> temp = new ArrayList<>();
                Arrays.stream(squareMapSpaces[i].split(" ")).forEach(position -> temp.add(position));
                mapList.add(temp);
            }
            return mapList;
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
                default:
                    sendMessage(Messages.NO_SUCH_COMMAND);
                    chooseCharacter();
            }
        }


        @Override
        public void run() {


            try {
                myMap = generateMap(MapType.SQUARE_MAP.getMAP());
                oppMap = generateMap(MapType.SQUARE_MAP.getMAP());

                chooseCharacter();
                placeShips();

            } catch (IOException e) {
                System.out.println("Something went wrong with the server. Connection closing...");
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }

        public void takeTurn() throws IOException {
            sendMessage(Messages.YOUR_TURN);
            sendMessage(String.valueOf(playerPoints.getPlayerPoints()));
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

        public List<List<String>> getOppMap() {
            return oppMap;
        }

        public Ship checkIfHit(int row, int col) {
            for (Ship ship : character.getPlayerShips()) {
                if (ship.gotHit(row, col)) {
                    String coloredVersion = "\u001B[31m" + ship.getType().getICON() + "\u001B[0m";
                    myMap.get(row).set(col, coloredVersion);
                    return ship;
                }
            }
            myMap.get(row).set(col, "\u001B[34mX\u001B[0m");
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
            playerPoints.setPlayerPoints(playerPoints.getPlayerPoints() + pointForHit);
            System.out.println(Messages.HIT_POINTS);
            if (ship.isSinked()) {
                playerPoints.setPlayerPoints(playerPoints.getPlayerPoints() + pointForSinking);
                System.out.println(Messages.SINK_POINTS);

            }
        }


        public void shootMessage(Ship ship, PlayerHandler playerHandler, int row, int col) {
            if (checkIfHit(row, col) == ship) {
                sendMessage(Messages.BOOM);
            }
            if (checkIfHit(row, col) == null) {
                playerHandler.sendMessage(Messages.MISSED);
            }
            if (ship.isSinked()) {
                playerHandler.sendMessage(Messages.KABOOM);
            }
        }

    }


}

