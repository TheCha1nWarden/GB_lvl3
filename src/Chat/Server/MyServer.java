package Chat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MyServer {
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    private ExecutorService executorService;

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
                System.out.println("Сервер ожидает подключение");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                executorService.execute(() -> new ClientHandler(this, socket));
            }
        } catch (IOException e) {
            System.out.println("Ошибка в работе сервера");
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
