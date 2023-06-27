package top.rrricardo.postletter.models;

import top.rrricardo.postletter.services.ClientWebSocket;

import java.util.ArrayList;
import java.util.List;


public class WebSocketManager {
    private WebSocketManager() {
        webSocketList = new ArrayList<>();
    }

    private static final WebSocketManager webSocketManager = new WebSocketManager();

    public static WebSocketManager getInstance() {
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


