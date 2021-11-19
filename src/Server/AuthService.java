package Server;

public interface AuthService {
    void start();
    String getNickByLoginPass(String login, String pass);
    String changeNick(String newNick, String nick);
    void stop();
}
