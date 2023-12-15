package Client;

import MessagesAndPrinter.Messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.start("localhost", 8888);
        } catch (IOException e) {
            System.out.println(Messages.LOST_CONNECTION);
        }

    }

    private void start(String host, int port) throws IOException {

        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(new KeyboardHandler(out, socket)).start();

        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }

        close(socket, in, out);
    }

    private void close(Socket socket, BufferedReader in, PrintWriter out) throws IOException {
        socket.close();
        in.close();
        out.close();
    }


    private static class KeyboardHandler implements Runnable {
        private final PrintWriter out;
        private final Socket socket;
        private final BufferedReader in;

        public KeyboardHandler(PrintWriter out, Socket socket) {
            this.out = out;
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }

        @Override
        public void run() {
            while (!socket.isClosed()) {
                try {

                    String line = in.readLine();
                    out.println(line);

                } catch (IOException e) {
                    System.out.println(Messages.ERROR);
                    close();
                }
            }
        }

        private void close() {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
