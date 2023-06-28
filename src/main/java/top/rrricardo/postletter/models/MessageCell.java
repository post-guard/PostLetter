package top.rrricardo.postletter.models;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.services.HttpService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 聊天气泡类
 */
public class MessageCell extends ListCell<Message> {
    private final AnchorPane messagePane;
    private final Circle avatarBackground;
    private final Label avatarText;
    private final Label avatarName;
    private final Rectangle messageBubble;
    private final Label messageText;
    private final Label messageSendTime;

    private ArrayList<User> userList;


    public MessageCell() {
        super();

        messagePane = new AnchorPane();
        avatarBackground = new Circle();
        avatarText = new Label();
        avatarName = new Label();
        messageBubble = new Rectangle();
        messageText = new Label();
        messageSendTime = new Label();


        try {
            var response = HttpService.getInstance().get("/user/"
                    , new TypeReference<ResponseDTO<ArrayList<User>>>() {});
            if(response != null && response.getData() != null) {
                userList = response.getData();
            }
        } catch (IOException | NetworkException e) {
            e.printStackTrace();
        }

        messagePane.getChildren().add(messageBubble);
        messagePane.getChildren().add(messageText);
        messagePane.getChildren().add(avatarBackground);
        messagePane.getChildren().add(avatarText);
        messagePane.getChildren().add(avatarName);
        messagePane.getChildren().add(messageSendTime);
    }

    @Override
    public void updateItem(Message item, boolean empty) {

        super.updateItem(item, empty);
        setEditable(false);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {

            if(item.getSendId() == Configuration.getInstance().getId()) {
                // 如果是当前用户自己的发言，会浮在右边
                this.setMessagePane(item,"right");
            }
            else {
                // 如果是其他用户的发言，会浮在左边
                this.setMessagePane(item,"left");
            }
            setGraphic(messagePane);
        }
    }

    private void setMessagePane(Message item, String rotation) {

        messageBubble.setWidth( Math.min(item.getText().length() * 15 + 10 , 160));
        messageBubble.setHeight( (item.getText().length()/10.0 + 1 ) * 15 + 10);
        messageBubble.setArcHeight(messageBubble.getHeight()/2);
        messageBubble.setArcWidth(messageBubble.getHeight()/2);
        messageBubble.setFill(Color.rgb(130,180,255));

        messageText.setStyle("-fx-text-fill: #FFFFFF");
        messageText.setText(item.getText());
        messageText.setFont(Font.font(15));
        messageText.setMaxWidth(160);
        messageText.setWrapText(true);

        messageSendTime.setFont(Font.font(10));

        try {
            var response = HttpService.getInstance().get("/user/online/" + item.getSendId()
                    , new TypeReference<ResponseDTO<Boolean>>() {});
            if(response != null && response.getData()) {
                avatarBackground.setFill(Color.rgb(80,120,255));
            }
            else {
                avatarBackground.setFill(Color.rgb(80,80,80));
            }
        } catch (IOException | NetworkException e) {
            e.printStackTrace();
        }

        avatarBackground.setRadius(20);


        avatarText.setFont(Font.font(20));
        avatarText.setStyle("-fx-text-fill: #FFFFFF");

        avatarText.setText(Objects.requireNonNull(getSenderNickname(item)).getNickname().substring(0,1));
        avatarName.setText(Objects.requireNonNull(getSenderNickname(item)).getNickname());

        messagePane.setPrefWidth(Math.min(item.getText().length() * 15 + 10 + 60, 1600));
        messagePane.setPrefHeight((item.getText().length()/10.0 + 1 ) * 15 + 65);

        messageSendTime.setText(item.getSendTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")));

        if(Objects.equals(rotation, "left")) {
            
            AnchorPane.setTopAnchor(avatarBackground,5.0);
            AnchorPane.setLeftAnchor(avatarBackground,10.0);

            AnchorPane.setTopAnchor(avatarText,12.0);
            AnchorPane.setLeftAnchor(avatarText,22.0);

            AnchorPane.setTopAnchor(avatarName,5.0);
            AnchorPane.setLeftAnchor(avatarName,55.0);

            AnchorPane.setTopAnchor(messageSendTime,50.0);
            AnchorPane.setLeftAnchor(messageSendTime,5.0);

            AnchorPane.setTopAnchor(messageBubble,25.0);
            AnchorPane.setLeftAnchor(messageBubble,60.0);

            AnchorPane.setTopAnchor(messageText,30.0);
            AnchorPane.setLeftAnchor(messageText,65.0);

        }
        else {
            AnchorPane.setTopAnchor(avatarBackground,5.0);
            AnchorPane.setRightAnchor(avatarBackground,10.0);

            AnchorPane.setTopAnchor(avatarText,12.0);
            AnchorPane.setRightAnchor(avatarText,22.0);

            AnchorPane.setTopAnchor(avatarName,5.0);
            AnchorPane.setRightAnchor(avatarName,55.0);

            AnchorPane.setTopAnchor(messageSendTime,50.0);
            AnchorPane.setRightAnchor(messageSendTime,5.0);

            AnchorPane.setTopAnchor(messageBubble,25.0);
            AnchorPane.setRightAnchor(messageBubble,60.0);

            AnchorPane.setTopAnchor(messageText,30.0);
            AnchorPane.setRightAnchor(messageText,65.0);
        }
    }

    private User getSenderNickname(Message message) {
        for(User user : userList) {
            if(user.getId() == message.getSendId()) {
                return user;
            }
        }
        return new User("Mr.Unknown","Unknown");
    }

}
