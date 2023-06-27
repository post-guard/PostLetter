package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.*;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.ControllerBase;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MessageController extends HomeController implements ControllerBase {


    @FXML
    private Button sendButton;
    @FXML
    private Label sessionLabelName;
    @FXML
    private Label sessionLabelLevel;
    @FXML
    private ListView<Session> sessionListView = new ListView<>();
    @FXML
    private TextArea sendTextArea;

    private static Session currentSession;

    private User currentUser;

    private final Callback<ListView<Session>, ListCell<Session>> call = TextFieldListCell.forListView(new StringConverter<>() {

        @Override
        public String toString(Session object) {
            return object.getName();
        }

        @Override
        public Session fromString(String string) {

            return new Session(string, "", 100);
        }
    });


    public void open() {
        try {


            var userResponse = HttpService.getInstance().get("/user/" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<User>>() {
            });
            if(userResponse != null) {
                currentUser = userResponse.getData();
            }

            if(currentUser != null) {

                var webSocketManager = WebSocketManager.getInstance();

                var participantResponse = HttpService.getInstance().get("/participant/user/" + currentUser.getId(),
                        new TypeReference<ResponseDTO<List<Participant>>>() {
                });
                if (participantResponse != null) {

                    ObservableList<Session> items = FXCollections.observableArrayList();

                    for(int i = 0 ;i < participantResponse.getData().size(); i++) {
                        var sessionResponse = HttpService.getInstance().get("/session/"
                                        + participantResponse.getData().get(i).getSessionId(),
                                new TypeReference<ResponseDTO<Session>>() {
                                });
                        if(sessionResponse != null) {
                            items.add(sessionResponse.getData());
                        }
                    }

                    sessionListView.setItems(items);
                    sessionListView.setCellFactory(call);
                }
            }

        } catch (NetworkException | IOException e) {
            System.out.println("获取会话列表失败");
        } finally {
            sendButton.setDisable(true);

            sessionListView.getSelectionModel().selectedItemProperty().addListener(
                    (ObservableValue<? extends Session> ov, Session old_val,
                     Session new_val) -> {
                        currentSession = new_val;
                        sessionLabelName.setText(new_val.getName());
                        sessionLabelLevel.setText("Lv." + new_val.getLevel());
                    });

            sendTextArea.textProperty().addListener((observable, oldValue, newValue) ->
                    sendButton.setDisable(Objects.equals(sendTextArea.getText(), "")));
        }
    }

    @FXML
    protected void onSendButtonClick() throws IOException{

        try {
            if(currentSession != null){

                var sessionResponse = HttpService.getInstance().get("/session/"
                                    + currentSession.getId(),
                        new TypeReference<ResponseDTO<Session>>() {});
                if(sessionResponse != null) {

                    LocalDateTime localDateTime = LocalDateTime.now();
                    var message = new Message(
                        currentSession.getId(), currentUser.getId(),
                            sendTextArea.getText(),localDateTime
                    );

                    var response = HttpService.getInstance().post("/message/send",message ,
                            new TypeReference<ResponseDTO<Message>>() {});

                    if(response != null) {
                        sendTextArea.setText("");
                    }
                }
            }
        } catch (NetworkException e) {
            e.printStackTrace();
            System.out.println("发送消息失败");
        }
    }
}
