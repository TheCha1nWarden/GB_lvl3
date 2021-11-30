package Chat.Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyServer {
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    private ExecutorService executorService;
    private static Logger log = LogManager.getLogger(MyServer.class);

    public AuthService getAuthService() {
        return authService;
    }

    public MyServer() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            executorService = Executors.newCachedThreadPool();
            while (true) {
                log.info("Сервер ожидает подключение");
                Socket socket = server.accept();
                log.info("Клиент подключился");
                executorService.execute(() -> new ClientHandler(this, socket));
            }
        } catch (IOException e) {
            log.error("Ошибка в работе сервера");
        }
        finally {
            executorService.shutdown();
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler o: clients) {
        if (o.getName().equals(nick)) {
            return true;
        }
        }
        return false;
    }

    public synchronized void sendMsgTo(ClientHandler from, String nick, String msg) {
        for (ClientHandler c : clients) {
            if (c.getName().equals(nick)) {
                c.sendMsg(from.getName() + ": " + msg);
                from.sendMsg("сообщение для " + nick + ": " + msg);
                return;
            }
        }
        from.sendMsg(nick + " не в сети");
    }

    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler o: clients) {
            o.sendMsg(msg);
        }
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
    }
}
