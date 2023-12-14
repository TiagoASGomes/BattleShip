package Battleship;

import Battleship.Character.Character;
import Battleship.Character.CharacterFactory;
import Battleship.Character.CharacterType;
import Messages.Messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Battleship implements Runnable {

    private boolean open = true;
    private final PlayerHandler player1;
    private PlayerHandler player2;
    private ExecutorService service;


    public Battleship(Socket client) {
        this.player1 = new PlayerHandler(client);
    }


    @Override
    public void run() {

        service = Executors.newFixedThreadPool(2);
        while (player1 == null || player2 == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public boolean isOpen() {
        return open;
    }

    public void acceptPlayer(Socket client) {
        player2 = new PlayerHandler(client);
        service.submit(player1);
        service.submit(player2);
    }


    public class PlayerHandler implements Runnable {


        private PrintWriter out;
        private Socket socket;
        private BufferedReader in;
        private String message;

        private Character character;


        public PlayerHandler(Socket socket) {
            this.socket = socket;
            try {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void chooseCharacter() throws IOException {

            sendMessage(Messages.CHOOSE_CHARACTER);
            String playerChoice = in.readLine();
            switch (playerChoice) {
                case "1":
                    this.character = CharacterFactory.create(CharacterType.ONE);
                    break;
                case "2":
                    this.character = CharacterFactory.create(CharacterType.TWO);
                    break;
                default:
                    sendMessage(Messages.NO_SUCH_COMMAND);
                    chooseCharacter();


            }
        }


        @Override
        public void run() {

            while (!socket.isClosed()) {
                try {

                    chooseCharacter();


                    String line = in.readLine();

                    out.println(line);


                } catch (IOException e) {
                    System.out.println("Something went wrong with the server. Connection closing...");
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }


        public String getMessage() {
            return message;
        }
    }


}

