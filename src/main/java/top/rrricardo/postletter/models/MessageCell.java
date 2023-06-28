package top.rrricardo.postletter.models;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * 聊天气泡类
 */
public class MessageCell extends ListCell<Message> {
    AnchorPane anchorPane = new AnchorPane();
    Circle avatarBackground = new Circle(30, Color.rgb(0,20,255));
    Label avatarText = new Label();
    Rectangle messageBubble = new Rectangle();
    Label messageText = new Label();


    public MessageCell() {
        super();


    }

    @Override
    public void updateItem(Message item, boolean empty) {

        super.updateItem(item, empty);
        setEditable(false);

        if (empty || item == null) {
            setText(null);
            //setGraphic(null);
        } else {

            if(item.getId() == Configuration.getInstance().getId()) {
                // 如果是当前用户自己的发言，会浮在右边
            }
            else {
                // 如果是其他用户的发言，会浮在左边
            }
            setText(item.getText());
            //setGraphic(null);
        }
    }



}
