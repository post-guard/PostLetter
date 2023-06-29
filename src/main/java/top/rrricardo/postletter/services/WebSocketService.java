package top.rrricardo.postletter.services;

import top.rrricardo.postletter.models.Configuration;

import java.util.ArrayList;
import java.util.List;


public class WebSocketService {
    private WebSocketService() {
        webSocketList = new ArrayList<>();
    }

    private static final WebSocketService webSocketManager = new WebSocketService();

    public static WebSocketService getInstance() {
        return webSocketManager;
    }

    public static List<ClientWebSocket> webSocketList;

    public void init() {

        WebSocketMessage webSocketMessage = new WebSocketMessage("/websocket/message/" + Configuration.getInstance().getId());
        WebSocketHeartBeat webSocketHeartBeat = new WebSocketHeartBeat("/websocket/heartbeat/" + Configuration.getInstance().getId());
        webSocketList.add(webSocketMessage);
        webSocketList.add(webSocketHeartBeat);
    }

    /**
     * 关闭列表里的全部WebSocket连接,并清空列表
     */
    public void closeAll() {
        for (ClientWebSocket webSocket : webSocketList) {
            webSocket.state = "disconnected";
            webSocket.disconnect(1000, "用户下线");
        }
        webSocketList.clear();
    }

}


