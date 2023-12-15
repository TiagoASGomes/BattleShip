package Battleship;

import Battleship.Character.Character;
import Battleship.Character.CharacterFactory;
import Battleship.Character.CharacterType;
import Battleship.Maps.MapType;
import Battleship.ships.Ship;
import MessagesAndPrinter.Messages;
import MessagesAndPrinter.Printer;
import commands.Command;

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

        while (true) {
            try {
                players.get(0).takeTurn();

                players.get(1).takeTurn();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

        public void generateMap(String map) {

            String[] squareMapSpaces = map.split("\n");

            myMap = new ArrayList<>();
            for (int i = 0; i < squareMapSpaces.length; i++) {
                List<String> temp = new ArrayList<>();
                Arrays.stream(squareMapSpaces[i].split(" ")).forEach(position -> temp.add(position));
                myMap.add(temp);
            }
            String[] squareMapSpaces2 = map.split("\n");

            oppMap = new ArrayList<>();
            for (int i = 0; i < squareMapSpaces2.length; i++) {
                List<String> temp = new ArrayList<>();
                Arrays.stream(squareMapSpaces2[i].split(" ")).forEach(position -> temp.add(position));
                oppMap.add(temp);
            }

        }


        public void chooseCharacter() throws IOException {

            sendMessage(Messages.CHOOSE_CHARACTER);
            String playerChoice = in.readLine();
            switch (playerChoice) {
                case "1":
                    this.character = CharacterFactory.create(CharacterType.ONE);
                    System.out.println("a");
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
                generateMap(MapType.SQUARE_MAP.getMAP());
                generateMap(MapType.SQUARE_MAP.getMAP());
                sendMessage(Printer.createMap(this));
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
            message = in.readLine();
            Command command = Command.getCommandFromDescription(message.split(" ")[0]);
            command.getHandler().execute(this, Battleship.this);
            if (command.equals(Command.NOT_FOUND)) {
                takeTurn();
            }
        }

        private void placeShips() throws IOException {
            sendMessage(Messages.SHIP_PLACEMENT);
            while (!ready) {
                message = in.readLine();
                Command command = Command.getCommandFromDescription(message.split(" ")[0]);
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

        public boolean checkIfHit(int row, int col) {
            for (Ship ship : character.getPlayerShips()) {
                if (ship.gotHit(row, col)) {
                    return true;
                }
            }
            return false;
        }

    }


}

