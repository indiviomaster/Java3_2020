import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextArea textArea;

    @FXML
    TextField msgField, loginField;

    @FXML
    HBox msgPanel, authPanel;

    @FXML
    PasswordField passField;

    @FXML
    ListView<String> clientsList;

    private Network network;

    private boolean authenticated;
    private String nickname;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientsList.setVisible(authenticated);
        clientsList.setManaged(authenticated);
        if (!authenticated) {
            nickname = "";
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthenticated(false);
        clientsList.setOnMouseClicked(this::clientClickHandler);
        createNetwork();
        network.connect();
    }

    public void sendAuth() {
        network.sendAuth(loginField.getText(), passField.getText());
        loginField.clear();
        passField.clear();
    }

    public void sendMsg() {
        if (network.sendMsg(msgField.getText())) {
            msgField.clear();
            msgField.requestFocus();
        }
    }

    public void sendExit(){


        network.sendMsg("/end");
    }



    public void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
            alert.showAndWait();
        });
    }

    public void createNetwork() {
        network = new Network();
        network.setCallOnException(args -> showAlert(args[0].toString()));

        network.setCallOnCloseConnection(args -> setAuthenticated(false));

        network.setCallOnAuthenticated(args -> {
            setAuthenticated(true);
            nickname = args[0].toString();
            textArea.clear();
            try {
                readFromFile(nickname);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        network.setCallOnMsgReceived(args -> {
            String msg = args[0].toString();
            if (msg.startsWith("/")) {
                if (msg.startsWith("/clients ")) {
                    String[] tokens = msg.split("\\s");

                    Platform.runLater(() -> {

                        clientsList.getItems().clear();
                        for (int i = 1; i < tokens.length; i++) {
                            if(!(tokens[i].trim().equals(nickname))&&(tokens[i].trim()!="")&&(tokens[i].trim()!=null))
                            {clientsList.getItems().add(tokens[i]);}
                        }
                    });
                }else if(msg.startsWith("/upnick")){
                    String[] tokens = msg.split("\\s");
                    nickname = tokens[1];
                    try {
                        readFromFile(nickname);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                textArea.appendText(msg + "\n");
                try {
                    writeToFile(msg + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void writeToFile(String str) throws IOException{

        File file =new File("data_"+nickname+".txt");
        try(FileWriter fileWriter = new FileWriter(file,true)){
            fileWriter.write(str);
        }
    }

    private void readFromFile(String nickname) throws IOException {
        textArea.clear();
        File file =new File("data_"+nickname+".txt");
        if(file.exists()) {
            int count = 0;
            List<String> listMsg = new ArrayList<>();
            RandomAccessFile rafile = new RandomAccessFile(file, "r");
            String line;
            while ((line = rafile.readLine()) != null) {
                listMsg.add(line);
                count++;
            }

            for (int i = ((listMsg.size() > 100) ? listMsg.size()-100:0) ; i< listMsg.size()  ; i++) {
                textArea.appendText(listMsg.get(i) + "\n");
            }
            rafile.close();
        }
    }

    private void clientClickHandler(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String nickname = clientsList.getSelectionModel().getSelectedItem();
            msgField.setText("/w " + nickname + " ");
            msgField.requestFocus();
            msgField.selectEnd();
        }
    }
}