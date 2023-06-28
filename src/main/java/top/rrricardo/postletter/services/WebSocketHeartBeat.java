package top.rrricardo.postletter.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebSocketHeartBeat extends ClientWebSocket {

    private ScheduledExecutorService timer;

    public WebSocketHeartBeat(String url) {
        super(url);
    }

    @Override
    public void action() {
        super.action();
        Runnable heartBeat = () -> clientWebSocket.send("ping");

        if (timer != null) {
            timer.shutdown();
        }
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(heartBeat, 5, 10, TimeUnit.SECONDS);
    }
}
