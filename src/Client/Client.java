package Client;

import MessagesAndPrinter.Messages;

import javax.sound.sampled.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private final Map<String, Clip> clips = new HashMap<>();
    private boolean myTurn;

    /**
     * Constructor method of the Client class,
     */
    public Client() {
        getClips();
        myTurn = true;
    }

    /**
     * This method gets the music clips from a specified file.
     */
    private void getClips() {
        try {
            File[] files = new File("../Resources/SoundFiles").listFiles();
            for (File file : files) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clips.put(file.getName(), AudioSystem.getClip());
                clips.get(file.getName()).open(ais);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(Messages.ERROR);
        }
    }

    /**
     * Main method of the Client class,
     * Has a default host and port, so client can access the server,     *
     *
     * @param args address and port to start enter the server.
     */
    public static void main(String[] args) {
        try {
            Client client = new Client();
            InetAddress ip = getIp(args);
            int port = getPort(args);
            client.start(ip, port);
        } catch (IOException e) {
            System.out.println(Messages.LOST_CONNECTION);
            System.exit(0);
        }
    }

    private static int getPort(String[] args) {
        if (args.length < 2) {
            return 8888;
        }
        try {
            return Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return 8888;
        }
    }

    private static InetAddress getIp(String[] args) {
        try {
            if (args.length < 2) {
                return InetAddress.getByName("localhost");
            }
            return InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            try {
                return InetAddress.getByName("localhost");
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    /**
     * This method starts the connection between client and server,
     * Creates a new socket and a new thread that reads the input commands by the client and executes them.
     *
     * @param host Host String as a parameter,
     * @param port int port as a parameter,
     * @throws IOException when it's not possible to connect to the server
     */
    private void start(InetAddress host, int port) throws IOException {

        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(new KeyboardHandler(out, socket)).start();

        String line;
        while (!socket.isClosed() && (line = in.readLine()) != null) {
            if (line.startsWith("/")) {
                checkCommand(line, socket);
                continue;
            }
            System.out.println(line);
        }

        close(socket, in, out);
    }

    /**
     * Prints the correspondent Message, plays the correspondent song, and/or sets turn permission to true,
     * depending on the command it receives as parameter.
     *
     * @param line receives a String that represents a command.
     */
    private void checkCommand(String line, Socket socket) {

        switch (line) {
            case Messages.BOOM_COMMAND:
                System.out.println(Messages.BOOM);
                play("Explosion.wav");
                break;
            case Messages.MISSED_COMMAND:
                System.out.println(Messages.MISSED);
                play("Splash.wav");
                break;
            case Messages.GIVE_TURN_PERMISSION:
                myTurn = true;
                break;
            case Messages.GIVE_TURN_PERMISSION2:
                myTurn = true;
                System.out.println(Messages.YOUR_TURN);
                break;
            case Messages.WELCOME_COMMAND:
                play("EntryMusic.wav");
                break;
            case Messages.QUIT_COMMAND:
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    /**
     * Plays a Clip from certain index.
     *
     * @param name receives an int that is the index position of that Clip.
     */
    private void play(String name) {
        clips.get(name).setMicrosecondPosition(0);
        clips.get(name).start();
    }

    /**
     * Closes the Socket, the PrintWriter and the BufferedReader.
     */
    private void close(Socket socket, BufferedReader in, PrintWriter out) throws IOException {
        socket.shutdownInput();
        socket.close();
        in.close();
        out.close();
    }


    private class KeyboardHandler implements Runnable {
        private final PrintWriter out;
        private final Socket socket;
        private final BufferedReader in;

        /**
         * Constructor for KeyboardHandler.
         * Initializes PrintWriter, BufferedReader and Socket.
         *
         * @param out    receives a PrintWriter as parameter.
         * @param socket receives a Socket as parameter
         */
        public KeyboardHandler(PrintWriter out, Socket socket) {
            this.out = out;
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(System.in));

        }

        /**
         * Runs KeyboardHandler. While Socket is not closed,
         * receives inputs and checks if it is the client's turn to play.
         */
        @Override
        public void run() {
            while (!socket.isClosed()) {
                try {
                    String line = in.readLine();
                    if (line.equals("/quit")) {
                        close();
                    }
                    if (!myTurn) {
                        System.out.println(Messages.NOT_YOUR_TURN);
                        continue;
                    }
                    out.println(line);
                    myTurn = false;


                } catch (IOException e) {
                    System.out.println(Messages.ERROR);
                    close();
                }
            }
        }

        /**
         * Closes the Socket, the PrintWriter and the BufferedReader.
         */
        private void close() {
            try {
                socket.shutdownOutput();
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
