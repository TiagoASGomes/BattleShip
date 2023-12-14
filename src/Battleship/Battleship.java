package Battleship;

import Battleship.Maps.MapType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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


        public PlayerHandler(Socket socket) {
            this.socket = socket;
            try {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        public void generateMap(String map){

            String [] squareMapSpaces = map.split("\n");

            List<List<String>> squareMap = new ArrayList<>();
            for (int i = 0; i < squareMapSpaces.length; i++) {
                List<String> temp = new ArrayList<>();
                Arrays.stream(squareMapSpaces[i].split(" ")).forEach(position -> temp.add(position));
                squareMap.add(temp);
            }

        }


        @Override
        public void run() {

            while (!socket.isClosed()) {
                try {

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
    }


}

