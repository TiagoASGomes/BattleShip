package Battleship;

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
        player2.getSocket();
    }


    public class PlayerHandler implements Runnable {


        private PrintWriter out;
        private Socket socket;
        private BufferedReader in;


        public PlayerHandler(Socket socket) {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }

        public Socket getSocket() {
            return socket;
        }

        @Override
        public void run() {

            while (!socket.isClosed()) {
                try {

                    String line = in.readLine();

                    out.write(line);
                    out.println();
                    out.flush();

                        /*if (line.equals("/quit")) {
                            socket.close();
                            System.exit(0);
                        }*/
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

