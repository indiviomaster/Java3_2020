public class UserEntry {

    private int id;
    private String login;
    private String pass;
    private String nick;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getNick() {
        return nick;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public UserEntry(int id,String login, String pass, String nick) {
        this.id = id;
        this.login = login;
        this.pass = pass;
        this.nick = nick;
    }
}
