package top.rrricardo.postletter.models;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * 聊天信息细胞工厂
 */

public class MessageCellFactory implements Callback<ListView<Message>, ListCell<Message>> {
    @Override
    public ListCell<Message> call(ListView<Message> messageListView) {
        return new MessageCell();
    }
}
