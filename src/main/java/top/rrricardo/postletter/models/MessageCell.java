package top.rrricardo.postletter.models;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.TextUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 聊天气泡类
 */
public class MessageCell extends ListCell<Message> {
    private final AnchorPane messagePane;

    private final AnchorPane avatarPane;

    private final AnchorPane bubblePane;
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
        bubblePane = new AnchorPane();
        avatarPane = new AnchorPane();
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

        avatarPane.getChildren().add(avatarBackground);
        avatarPane.getChildren().add(avatarText);

        bubblePane.getChildren().add(messageBubble);
        bubblePane.getChildren().add(messageText);

        messagePane.getChildren().add(avatarPane);
        messagePane.getChildren().add(bubblePane);

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
            setText(null);
            setGraphic(messagePane);
        }
    }

    private void setMessagePane(Message item, String rotation) {

        avatarPane.setPrefWidth(40);
        avatarPane.setPrefHeight(40);

        messageText.setStyle("-fx-text-fill: #FFFFFF");
        messageText.setText(item.getText());
        messageText.setFont(Font.font(15));
        //messageText.setMinWidth(Region.USE_PREF_SIZE);
        //messageText.setPrefWidth(TextUtils.computeTextWidth(messageText.getFont(),
        //        messageText.getText(), 0.0D));

        messageText.setMaxWidth(150);
        messageText.setWrapText(true);
        var textLength = 1.2 * TextUtils.computeTextWidth(new Font(15),
                messageText.getText(), 0.0D);
        var textHeight = 0;
        if(textLength > 150) {
            textHeight = ((int) (textLength/150) + 1) * 15;
            textLength = 150;
        } else {
            textHeight = 15;
        }
        messageBubble.setWidth(textLength + 10);
        messageBubble.setHeight(textHeight + 15);
        messageBubble.setArcHeight(messageBubble.getHeight()/2);
        messageBubble.setArcWidth(messageBubble.getHeight()/2);
        messageBubble.setFill(Color.rgb(130,180,255));

        bubblePane.setPrefWidth(messageBubble.getWidth());
        bubblePane.setPrefHeight(messageBubble.getHeight());

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
        avatarText.setAlignment(Pos.CENTER);
        avatarText.setStyle("-fx-text-fill: #FFFFFF");

        avatarText.setText(Objects.requireNonNull(getSenderNickname(item)).getNickname().substring(0,1));
        avatarName.setText(Objects.requireNonNull(getSenderNickname(item)).getNickname());

        /*messagePane.setPrefWidth(Math.min(item.getText().length() * 15 + 10 + 60, 150));
        messagePane.setPrefHeight((item.getText().length()/10.0 + 1 ) * 15 + 65);*/


        messageSendTime.setText(item.getSendTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")));

        messagePane.setMinWidth(Region.USE_PREF_SIZE);
        messagePane.setMinHeight(Region.USE_PREF_SIZE);
        if(Objects.equals(rotation, "left")) {

            AnchorPane.clearConstraints(avatarPane);
            AnchorPane.clearConstraints(bubblePane);
            AnchorPane.clearConstraints(avatarBackground);
            AnchorPane.clearConstraints(avatarText);
            AnchorPane.clearConstraints(avatarName);
            AnchorPane.clearConstraints(messageBubble);
            AnchorPane.clearConstraints(messageText);
            AnchorPane.clearConstraints(messageSendTime);

            AnchorPane.setTopAnchor(avatarPane,0.0);
            AnchorPane.setLeftAnchor(avatarPane,5.0);

            AnchorPane.setTopAnchor(avatarBackground,0.0);
            AnchorPane.setLeftAnchor(avatarBackground,0.0);
            AnchorPane.setTopAnchor(avatarText,0.0);
            AnchorPane.setBottomAnchor(avatarText,0.0);
            AnchorPane.setLeftAnchor(avatarText,0.0);
            AnchorPane.setRightAnchor(avatarText,0.0);

            AnchorPane.setTopAnchor(avatarName,5.0);
            AnchorPane.setLeftAnchor(avatarName,55.0);

            AnchorPane.setTopAnchor(messageSendTime,50.0);
            AnchorPane.setLeftAnchor(messageSendTime,0.0);

            AnchorPane.setTopAnchor(bubblePane,25.0);
            AnchorPane.setLeftAnchor(bubblePane,60.0);

            AnchorPane.setTopAnchor(messageBubble,0.0);
            //AnchorPane.setBottomAnchor(messageBubble,0.0);
            AnchorPane.setLeftAnchor(messageBubble,0.0);
            //AnchorPane.setRightAnchor(messageBubble,0.0);

            AnchorPane.setTopAnchor(messageText,5.0);
            //AnchorPane.setBottomAnchor(messageText,5.0);
            AnchorPane.setLeftAnchor(messageText,5.0);
            //AnchorPane.setRightAnchor(messageText,5.0);

        }
        else if(Objects.equals(rotation, "right")){

            AnchorPane.clearConstraints(avatarPane);
            AnchorPane.clearConstraints(bubblePane);
            AnchorPane.clearConstraints(avatarBackground);
            AnchorPane.clearConstraints(avatarText);
            AnchorPane.clearConstraints(avatarName);
            AnchorPane.clearConstraints(messageBubble);
            AnchorPane.clearConstraints(messageText);
            AnchorPane.clearConstraints(messageSendTime);


            AnchorPane.setTopAnchor(avatarPane,0.0);
            AnchorPane.setRightAnchor(avatarPane,5.0);

            AnchorPane.setTopAnchor(avatarBackground,0.0);
            AnchorPane.setLeftAnchor(avatarBackground,0.0);

            AnchorPane.setTopAnchor(avatarText,0.0);
            AnchorPane.setBottomAnchor(avatarText,0.0);
            AnchorPane.setLeftAnchor(avatarText,0.0);
            AnchorPane.setRightAnchor(avatarText,0.0);

            AnchorPane.setTopAnchor(avatarName,5.0);
            AnchorPane.setRightAnchor(avatarName,55.0);

            AnchorPane.setTopAnchor(messageSendTime,50.0);
            AnchorPane.setRightAnchor(messageSendTime,0.0);


            AnchorPane.setTopAnchor(bubblePane,25.0);
            AnchorPane.setRightAnchor(bubblePane,60.0);

            AnchorPane.setTopAnchor(messageBubble,0.0);
            //AnchorPane.setBottomAnchor(messageBubble,0.0);
            AnchorPane.setRightAnchor(messageBubble,0.0);
            //AnchorPane.setRightAnchor(messageBubble,0.0);

            AnchorPane.setTopAnchor(messageText,5.0);
            //AnchorPane.setBottomAnchor(messageText,5.0);
            //AnchorPane.setRightAnchor(messageText,5.0);
            AnchorPane.setRightAnchor(messageText,5.0);
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
