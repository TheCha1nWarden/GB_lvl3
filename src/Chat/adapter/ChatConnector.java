package Chat.adapter;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class ChatConnector {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private boolean flagAuth = false;
    private File file;

    public ChatConnector(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during a connection to the server.", e);
        }
    }

    public void sendMessage(String outboundMessage) {
        try {
            out.writeUTF(outboundMessage);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during sending message to the server.", e);
        }
    }

    public String receiveMessage() {
        try {
            String tmp = in.readUTF();
            if (tmp.startsWith("/authok")) {
                String[] tmpArray = tmp.split("\\s");
                file = new File("history_" + tmpArray[1] + ".txt");
                flagAuth = true;
                return viewHistory(file);
            }
            if (flagAuth) {
                addToHistory(file, tmp);
            }
            return tmp;
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during receiving message from the server.", e);
        }
    }

    public void addToHistory(File file, String text) {
        try {
            FileOutputStream fout = new FileOutputStream(file, true);
            fout.write((text + "\n").getBytes(StandardCharsets.UTF_8));
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String viewHistory(File file) {
        try {
            BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
            int counter = 0;
            byte[] array = new byte[(int) file.length()];
            for (int i = 0; i < array.length; i++) {
                array[i] = (byte) fin.read();
                // проверка: не является ли байт в массиве, байтом кодирующим символ новой строки
                // насколько мне известно, числовой эквивалент символа новой строки в UTF8 равен 10
                if (array[i] == 10) {
                    counter++;
                }
                if (counter == 100) {
                    break;
                }
            }
            String tmp = new String(array, "UTF-8");
            fin.close();
            return tmp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
