import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {
    private static final String CON_STR = "";
    private static Connection connection;
    private static Statement statement;

    public List<UserEntry> userEntries() {
        return userEntries;
    }

    private List<UserEntry> userEntries;
    private ResultSet resultSet;
    @Override
    public void start() throws SQLException, ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection("jdbc:sqlite:C:/sqllite/users.db");

        statement = connection.createStatement();
        System.out.println("Сервис аутентификации запущен");
        loadUsers();
    }

    public void loadUsers() throws SQLException {
        resultSet = statement.executeQuery("SELECT id, login, password, nick FROM user;");
        while (resultSet.next()) {
            userEntries.add(new UserEntry(resultSet.getInt("id"),resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("nick")));
            //System.out.println("Пользователь"+resultSet.getString("login")+" ID: "+resultSet.getInt("id") +" Pass: " + resultSet.getString("password")+" Nic: "+ resultSet.getString("nick")+"загружен");
        }
    }

    @Override
    public void stop() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Сервис аутентификации остановлен");
    }

    public BaseAuthService() {
        userEntries = new ArrayList<UserEntry>();
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (UserEntry o : userEntries) {
            if (o.getLogin().equals(login) && o.getPass().equals(pass)) return o.getNick();
        }
        return null;
    }

  }
