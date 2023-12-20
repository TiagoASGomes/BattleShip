package Client;

import MessagesAndPrinter.Messages;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final List<Clip> clips = new ArrayList<>();
    private boolean myTurn;


    public Client() {
        getClips();
        myTurn = true;
    }

    private void getClips() {
        try {
            File[] files = new File("Resources/SoundFiles").listFiles();
            for (int i = 0; i < files.length; i++) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(files[i]);
                clips.add(AudioSystem.getClip());
                clips.get(i).open(ais);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

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
            if (line.startsWith("/")) {
                checkCommand(line);
                continue;
            }
            System.out.println(line);
        }

        close(socket, in, out);
    }

    private void checkCommand(String line) {
        switch (line) {
            case Messages.BOOM_COMMAND:
                System.out.println(Messages.BOOM);
                play(0);
                break;
            case Messages.MISSED_COMMAND:
                System.out.println(Messages.MISSED);
                play(1);
                break;
            case Messages.GIVE_TURN_PERMISSION:
                myTurn = true;
                break;
            case Messages.GIVE_TURN_PERMISSION2:
                myTurn = true;
                System.out.println(Messages.YOUR_TURN);
                break;
            case Messages.WELCOME:
                play(2);
                break;
//            case Messages.GIVE_TURN_PERMISSION:
//                synchronized (lock) {
//                    lock.notifyAll();
//                }
//                break;
//            case Messages.GIVE_TURN_PERMISSION2:
//                synchronized (lock) {
//                    lock.notifyAll();
//                }
//                System.out.println(Messages.YOUR_TURN);
//                break;
        }
    }

    private void play(int index) {
        clips.get(index).setMicrosecondPosition(0);
        clips.get(index).start();
    }


    private void close(Socket socket, BufferedReader in, PrintWriter out) throws IOException {
        socket.close();
        in.close();
        out.close();
    }


    private class KeyboardHandler implements Runnable {
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
