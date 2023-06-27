package top.rrricardo.postletter.models;

import top.rrricardo.postletter.services.ClientWebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebSocketManager {
    private WebSocketManager() {
        webSocketList = new ArrayList<>();

        WebSocketMessage webSocketMessage = new WebSocketMessage("/websocket/message/" + Configuration.getInstance().getId());
        WebSocketHeartBeat webSocketHeartBeat = new WebSocketHeartBeat("/websocket/heartbeat/" + Configuration.getInstance().getId());
        webSocketList.add(webSocketMessage);
        webSocketList.add(webSocketHeartBeat);
    }

    private static WebSocketManager webSocketManager = new WebSocketManager();

    public static WebSocketManager getInstance() {
        return webSocketManager;
    }

    public static List<ClientWebSocket> webSocketList;

    public void closeAll() {
        for(ClientWebSocket webSocket : webSocketList) {
            webSocket.disconnect(1000,"用户下线");
        }
    }

}

class WebSocketMessage extends ClientWebSocket {

    public WebSocketMessage(String url) {
        super(url);
    }

    @Override
    public void receive(String text) {
        super.receive(text);
        System.out.println("WebSocket收到服务端信息 "+text);
    }
}


class WebSocketHeartBeat extends ClientWebSocket {

    private ScheduledExecutorService timer;

    public WebSocketHeartBeat(String url) {
        super(url);
    }

    @Override
    public void action() {
        super.action();
        Runnable heartBeat = () -> clientWebSocket.send("ping");

        if(timer!=null) {
            timer.shutdown();
        }
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(heartBeat,5,10, TimeUnit.SECONDS);
    }
}