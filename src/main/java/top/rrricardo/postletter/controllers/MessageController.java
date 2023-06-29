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
import top.rrricardo.postletter.services.MessageDistribution;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MessageController extends HomeController{


    @FXML
    private Button sendButton;
    @FXML
    private Label sessionLabelName;
    @FXML
    private Label sessionLabelLevel;
    @FXML
    private ListView<Session> sessionListView = new ListView<>();
    @FXML
    private ListView<Message> messageListView = new ListView<>();
    @FXML
    private TextArea sendTextArea;

    private static Session currentSession;

    private User currentUser;

    private final Callback<ListView<Session>, ListCell<Session>> sessionCallback = TextFieldListCell.forListView(new StringConverter<>() {

        @Override
        public String toString(Session object) {
            return object.getName();
        }

        @Override
        public Session fromString(String string) {

            return new Session(string, "", 100);
        }
    });

    private MessageDistribution messageDistribution;


    public void open() {
        try {


            var userResponse = HttpService.getInstance().get("/user/" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<User>>() {
            });
            if(userResponse != null) {
                currentUser = userResponse.getData();
            }

            if(currentUser != null) {

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
                    sessionListView.setCellFactory(sessionCallback);

                    updateMessageListView();
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

                        if (this.messageDistribution!=null) {
                            messageDistribution.isStop = true;
                        }
                        // 停下先前窗口的刷新信息线程
                        updateMessageListView(new_val.getId());
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
                        // 如果消息能正常发送，清空输入框
                    }
                }
            }
        } catch (NetworkException e) {
            e.printStackTrace();
            System.out.println("发送消息失败");
        }
    }

    /**
     * 更新聊天窗口的列表
     * 若sessionId为空,则列表显示为空;若为一个正确的session值,则显示这个会话的聊天信息
     * @param sessionId 可以为空，也可以为会话的session id
     */
    private void updateMessageListView(int... sessionId){

        ObservableList<Message> items = FXCollections.observableArrayList();
        messageListView.setSelectionModel(new NoSelectionModel<>());
        messageListView.setFocusTraversable( false );

        if(sessionId.length == 0) {
            items.clear();
            messageListView.setItems(items);
            messageListView.setCellFactory(new MessageCellFactory());
        }
        else {
            ResponseDTO<Session> sessionResponse = null;

            try {
                sessionResponse = HttpService.getInstance().get("/session/" + sessionId[0],
                        new TypeReference<>() {
                        });
            } catch (IOException | NetworkException e) {
                e.printStackTrace();
            }

            if(sessionResponse != null) {
                if(sessionResponse.getData() != null) {
                    // 如果用户还在会话中
                    ResponseDTO<List<Message>> messageResponse = null;

                    try {
                        messageResponse = HttpService.getInstance().get("/message/session/" + sessionId[0],
                                new TypeReference<>() {
                                });
                    } catch (IOException | NetworkException e) {
                        e.printStackTrace();
                    }


                    if(messageResponse != null) {
                        items.addAll(messageResponse.getData());
                        messageListView.setItems(items);
                        messageListView.setCellFactory(new MessageCellFactory());
                        messageListView.scrollTo(items.size());
                        messageDistribution = new MessageDistribution(messageListView,items,currentSession.getId());
                        new Thread(messageDistribution).start();
                    }
                }
            }
        }
    }


    public void close() {
        if(messageDistribution != null) {
            messageDistribution.isStop = true;
        }
    }

}
