import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MyServer {
    private final int PORT = 8189;  

    private Map<Integer, ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public MyServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();



            clients = new HashMap<>();

            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();

                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println("Ошибка в работе сервера");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick) {

        return clients.containsKey(authService.getIdByNick(nick));
    }

    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler o : clients.values()) {
            o.sendMsg(msg);
        }
    }
    public synchronized void broadcastMsg(String from, String msg) {
        broadcastMsg(formatMessage(from, msg));
    }

    public synchronized void sendMsgToClient(String from, String to, String msg) {
        if (clients.containsKey(authService.getIdByNick(to))) {
            clients.get(authService.getIdByNick(to)).sendMsg(formatMessage(from, msg));
        }
    }
    public synchronized void sendMsgFromSrvToClient(String to, String msg){
        if (clients.containsKey(authService.getIdByNick(to))) {
            clients.get(authService.getIdByNick(to)).sendMsg(msg);
        }
    }


    public synchronized void unsubscribe(ClientHandler o) {

        clients.remove(authService.getIdByNick(o.getName()));
        broadcastClients();
    }

    public synchronized void subscribe(ClientHandler o) {

        clients.put(authService.getIdByNick(o.getName()),o);
        broadcastClients();
    }

    private String formatMessage(String from, String msg) {
        return from + ": " + msg;
    }


    public synchronized void broadcastClients() {
        StringBuilder builder = new StringBuilder("/clients ");
        for (Integer id : clients.keySet()) {
            builder.append(authService.getNickById(id)).append(' ');
        }
        broadcastMsg(builder.toString());
    }

    public void changeClientNicName(String oldNic, String newNic) {
        try {
            authService.updateUserData(oldNic,newNic);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
