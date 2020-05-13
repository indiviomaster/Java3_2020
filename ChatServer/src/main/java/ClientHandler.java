import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    static final int TIME_OUT_TO_LOGIN = 120000;
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    private String name;

    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";


            //Timer close connection
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(TIME_OUT_TO_LOGIN);
                        if(name=="") {
                            //System.out.println("Клиент отключен по таймауту");
                            LOGGER.info("Соединение отключено по таймауту");
                            closeConn();
                            }
                    } catch (InterruptedException e) {
                        LOGGER.error("Ошибка в отключени по таймауту", e);
                        //e.printStackTrace();
                    }
                }
            });

            //Main thread
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        authentication();
                        readMessages();
                    } catch (IOException e) {
                        LOGGER.error("Ошибка приложения", e);
                        //e.printStackTrace();
                    } finally {
                        closeConnection();

                    }
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
        finally {
            executorService.shutdown();
        }
    }

    public void authentication() throws IOException {
        while (true) {
            String str = in.readUTF();
            if (str.startsWith("/auth")) {
                String[] parts = str.split("\\s");
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        sendMsg("/authok " + nick);
                        name = nick;
                        myServer.broadcastMsg(name + " зашел в чат");
                        LOGGER.info("Пользователь : {} зашел в чат",name);
                        myServer.subscribe(this);
                        return;
                    } else {
                        LOGGER.info("Учетная запись: {} уже используется",nick);
                        sendMsg("Учетная запись:"+nick+" уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль");
                    LOGGER.info("Неверные логин/пароль: ник: ",nick);
                }
            }
        }
    }

    public void readMessages() throws IOException {
        while (true) {
            String strFromClient = in.readUTF();
            //System.out.println("от " + name + ": " + strFromClient);
            LOGGER.info("Сообщение от {} -> {}",name, strFromClient);
            if (strFromClient.equals("/end")) {

                return;
            }

            if (strFromClient.startsWith("/w")) {
                String[] tokens = strFromClient.split("\\s");
                String nick = tokens[1];
                String msg = strFromClient.substring(4 + nick.length());
                myServer.sendMsgToClient(name, nick, msg);
                LOGGER.info("Персональное сообщение от {} -> {} :",name, nick, msg);
            } else if(strFromClient.startsWith("/chng")){
                String[] tokens = strFromClient.split("\\s");
                String oldNic = name;
                String newNic = tokens[1];
                LOGGER.debug("Команда изменения ника: {} на ник: {}",oldNic,newNic);
                if(!myServer.isNickBusy(newNic)){
                this.name = tokens[1];
                    myServer.sendMsgFromSrvToClient(oldNic,"/upnick "+name);
                    myServer.changeClientNicName(oldNic, newNic);
                    myServer.broadcastMsg("Ник: "+oldNic+" изменен на "+name);
                    LOGGER.debug("Ник: "+oldNic+" изменен на "+name);
                    myServer.broadcastClients();
                }else{
                    myServer.sendMsgFromSrvToClient(name,"Ник занят");
                    LOGGER.info("Ник: {} занят",newNic);
                }

            }else{
                myServer.broadcastMsg(name, strFromClient);
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            LOGGER.error("Ошибка отправки сообщения:{}",msg,e);
            //e.printStackTrace();
        }
    }

    public void closeConnection() {

        myServer.unsubscribe(this);
        myServer.broadcastMsg(name + " вышел из чата");
        LOGGER.info("Клиент вышел из чата",name);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public void closeConn() {

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

    }

}