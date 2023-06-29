package top.rrricardo.postletter.services;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import top.rrricardo.postletter.models.Message;

import java.util.ArrayList;

/**
 * 从WebSocket的消息队列中提取信息，并放入聊天窗口的会话列表中
 */
public class MessageDistribution implements Runnable {
    private final ObservableList<Message> items;
    private final ListView<Message> listView;
    private final int currentSessionId;

    public boolean isStop;
    public MessageDistribution(ListView<Message> listView,ObservableList<Message> items,int currentSessionId) {
        this.listView = listView;
        this.items = items;
        this.currentSessionId = currentSessionId;
        this.isStop = false;
    }


    @Override
    public void run() {
        WebSocketMessage webSocketMessage = (WebSocketMessage) WebSocketService.webSocketList.get(0);
        while (!isStop) {
            ArrayList<Message> deleteList = new ArrayList<>();
            for(Message message : webSocketMessage.messageList) {
                if(message.getSendId() != -1 && message.getSessionId() == currentSessionId) {
                    // 不是用户编号为-1的系统添加好友请求,且为当前对话的消息
                    Platform.runLater(() -> items.add(message));

                    deleteList.add(message);
                }
            }
            for(Message message : deleteList) {
                webSocketMessage.messageList.remove(message);
            }
            deleteList.clear();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
