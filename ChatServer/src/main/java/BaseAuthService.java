import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {
    private static final String CON_STR = "";
    private static Connection connection;
    private static Statement statement;

    public List<UserEntry> getUserEntries() {
        return userEntries;
    }
    public void updateUserEntries(UserEntry entry) {

        ;
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
    public String getNickById(Integer id){
        for (UserEntry o : userEntries) {
            if (o.getId() == id) return o.getNick();
        }
        return null;
    }
    @Override
    public Integer getIdByNick(String nick){
        for (UserEntry o : userEntries) {
            if (o.getNick().equals(nick)) return o.getId();
        }
        return -1;
    }
    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (UserEntry o : userEntries) {
            if (o.getLogin().equals(login) && o.getPass().equals(pass)) return o.getNick();
        }
        return null;
    }

    @Override
    public void updateUserData(String oldNic, String newNic) throws SQLException {
        for (UserEntry o : userEntries) {
            if (o.getNick().equals(oldNic)){
                o.setNick(newNic);
                System.out.println("Пользователь"+o.getLogin()+ " обновлен на ник:" + o.getNick());
                statement.executeUpdate("UPDATE user SET nick = '"+o.getNick()+"' WHERE id = "+o.getId()+";");
            }
        }
    }
}
