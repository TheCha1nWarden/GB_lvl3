package Chat.Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean flag = true;
    private static Logger log = LogManager.getLogger(ClientHandler.class);

    private String name;

    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
                try {
                    authentication();
                    readMessages();
                } catch (SocketTimeoutException e) {
                    delConnection();
                    flag = false;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(flag) {
                        closeConnection();
                    }
                }
        } catch (IOException e) {
            log.error("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException {
        socket.setSoTimeout(120000);
        while (true) {
            String str = in.readUTF();
            if (str.startsWith("/auth")) {
                String[] parts = str.split("\\s");
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        socket.setSoTimeout(0);
                        sendMsg("/authok " + nick);
                        name = nick;
                        myServer.broadcastMsg(name + " зашел в чат");
                        myServer.subscribe(this);
                        return;
                    } else {
                        sendMsg("Учетная запись уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль");
                }
            } else {
                sendMsg("Сначала необходимо войти в чат");
            }
        }
    }

    public void readMessages() throws IOException {
        while (true) {
            String strFromClient = in.readUTF();
            log.info("от" + name + ": " + strFromClient);
            System.out.println();
            if (strFromClient.startsWith("/w")) {
                String[] parts = strFromClient.split("\\s");
                myServer.sendMsgTo(this, parts[1], parts[2]);
            } else {
                if (strFromClient.startsWith("/cn")) {
                    String[] parts = strFromClient.split("\\s");
                    String tmpName = name;
                    name = myServer.getAuthService().changeNick(parts[1], name);
                    myServer.broadcastMsg(tmpName + " сменил ник на " + name);
                } else {
                    if (strFromClient.equals("/end")) {
                        return;
                    }
                    myServer.broadcastMsg(name + ": " + strFromClient);
                }
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeConnection() {
        sendMsg("Вы вышли из чата");
        myServer.unsubscribe(this);
        myServer.broadcastMsg(name + " вышел из чата");
        delConnection();
    }
    public void delConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}