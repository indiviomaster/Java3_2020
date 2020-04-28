import java.sql.SQLException;

public interface AuthService {
    void start() throws SQLException, ClassNotFoundException;

    String getNickByLoginPass(String login, String pass);

    void stop();
}
