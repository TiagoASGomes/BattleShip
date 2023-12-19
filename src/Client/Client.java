package Client;

import MessagesAndPrinter.Messages;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final List<Clip> clips = new ArrayList<>();


    public Client() {
        getClips();
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
                playSound(line);
                continue;
            }
            System.out.println(line);
        }

        close(socket, in, out);
    }

    private void playSound(String line) {
        switch (line) {
            case "/hit":
                play(0);
                break;
            case "/miss":
                play(1);
                break;
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
