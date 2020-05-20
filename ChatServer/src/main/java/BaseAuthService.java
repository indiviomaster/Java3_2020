import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {
    private static final Logger LOGGER = LogManager.getLogger(BaseAuthService.class);
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

        connection = DriverManager.getConnection("jdbc:sqlite:users.db");

        statement = connection.createStatement();
        LOGGER.info("Сервис аутентификации запущен");
        loadUsers();
    }

    public void loadUsers() throws SQLException {
        resultSet = statement.executeQuery("SELECT id, login, password, nick FROM user;");
        while (resultSet.next()) {
            userEntries.add(new UserEntry(resultSet.getInt("id"),resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("nick")));
            LOGGER.debug("пользователь {} добавлен в список зарегистрированных",resultSet.getString("nick"));
        }
    }

    @Override
    public void stop() {
        try {
            statement.close();
        } catch (SQLException e) {
            LOGGER.error("Ошибка БД", e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Ошибка закрытия соединения с БД", e);

        }

        LOGGER.info("Сервис аутентификации остановлен");
    }

    public BaseAuthService() {
        userEntries = new ArrayList<UserEntry>();
    }

    @Override
    public String getNickById(Integer id){
        for (UserEntry o : userEntries) {
            if (o.getId() == id) return o.getNick();
        }
        LOGGER.debug("Пользовательс с ID: {} отсутствует",id);
        return null;
    }
    @Override
    public Integer getIdByNick(String nick){
        for (UserEntry o : userEntries) {
            if (o.getNick().equals(nick)) return o.getId();
        }
        LOGGER.debug("Пользовательс с ником: {} отсутствует в списке",nick);
        return -1;
    }
    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (UserEntry o : userEntries) {
            if (o.getLogin().equals(login) && o.getPass().equals(pass)) return o.getNick();
        }
        LOGGER.debug("Пользовательс с именем: {} и паролем:[{}] отсутствует в списке",login,pass);
        return null;
    }

    @Override
    public void updateUserData(String oldNic, String newNic) throws SQLException {
        for (UserEntry o : userEntries) {
            if (o.getNick().equals(oldNic)){
                o.setNick(newNic);
                //System.out.println("Пользователь: "+o.getLogin()+" обновил на ник: "+o.getNick());
                LOGGER.info("Пользователь: {} обновил ник на: {}",o.getLogin(), o.getNick());
                statement.executeUpdate("UPDATE user SET nick = '"+o.getNick()+"' WHERE id = "+o.getId()+";");
            }
        }
    }
}
