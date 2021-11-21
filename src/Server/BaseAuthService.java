package Server;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseAuthService implements AuthService{

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        Connection connection = DatabaseConnector.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT nick FROM users WHERE login = ? AND pass = ?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, pass);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
            return rs.getString("nick");

        } catch (SQLException e) {
            throw new RuntimeException("getNick error", e);
        } finally {
            DatabaseConnector.close(connection);
        }
        return null;
    }

    @Override
    public String changeNick(String newNick, String nick) {
        String output = nick;
        Connection connection = DatabaseConnector.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET nick = ?  WHERE nick = ?");
            preparedStatement.setString(1, newNick);
            preparedStatement.setString(2, nick);
            preparedStatement.executeUpdate();
            connection.commit();
            output = newNick;
        } catch (SQLException e) {
            DatabaseConnector.rollback(connection);
            throw new RuntimeException("update error", e);
        } finally {
            DatabaseConnector.close(connection);
            return output;
        }
    }



    public void save(String login, String pass, String nick) {
        Connection connection = DatabaseConnector.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (login, pass, nick) VALUES (?, ?, ?)");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, pass);
            preparedStatement.setString(3, nick);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DatabaseConnector.rollback(connection);
            throw new RuntimeException("save error", e);
        } finally {
            DatabaseConnector.close(connection);
        }
    }



    public void delete(String login, String pass) {
        Connection connection = DatabaseConnector.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM users WHERE login = ? AND pass = ?"
            );
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, pass);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            DatabaseConnector.rollback(connection);
            throw new RuntimeException("delete error", e);
        } finally {
            DatabaseConnector.close(connection);
        }
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }

    public BaseAuthService() {
        //тут можно добавлять новых пользователей
//        save("login1", "pass1", "nick1");
//        save("login2", "pass2", "nick2");
//        save("login3", "pass3", "nick3");
    }


}
