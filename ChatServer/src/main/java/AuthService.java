import java.sql.SQLException;

public interface AuthService {
    void start() throws SQLException, ClassNotFoundException;

    String getNickByLoginPass(String login, String pass);
    Integer getIdByNick(String name);
    String getNickById(Integer id);
    void stop();


    void updateUserData(String oldNic, String newNic) throws SQLException;
}
