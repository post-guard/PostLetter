package top.rrricardo.postletter.services;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientWebSocket {

    private final String url;
    private WebSocket clientWebSocket;
    private final OkHttpClient client;

    private ScheduledExecutorService timer;

    public ClientWebSocket(String url) {
        this.url = url;
        client = new OkHttpClient().newBuilder().pingInterval(10, TimeUnit.SECONDS).build();
        connect();
    }

    public void connect() {

        Request request = new Request.Builder()
                .url("http://10.28.243.52:10188" + this.url)
                .build();
        clientWebSocket = client.newWebSocket(request, new ClientListener());

    }


    public void disconnect(int code, String reason) {
        if (clientWebSocket != null)
            clientWebSocket.close(code, reason);
    }


    public void send(final String message) {
        if (clientWebSocket != null) {

            clientWebSocket.send(message);
        }
    }


    class ClientListener extends WebSocketListener {

        //连接关闭
        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {

            super.onClosed(webSocket, code, reason);

            System.out.println("WebSocket连接关闭 "+code+reason);

        }

        //客户端主动关闭时回调
        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {

            super.onClosing(webSocket, code, reason);

            System.out.println("WebSocket正在关闭连接 "+code+reason);
        }

        // 连接出错
        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {

            super.onFailure(webSocket, t, response);

            System.out.println("WebSocket连接错误 "+response);

            // 重连
            try {
                System.out.println("3秒后开始重连..");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("开始重连");
            connect();
        }


        // 收到服务端发送来的 String 类型消息
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {

            super.onMessage(webSocket, text);

            System.out.println("WebSocket收到服务端信息 "+text);
        }


        // 连接成功
        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

            clientWebSocket = webSocket;
            System.out.println("WebSocket连接成功 连接到"+"http://10.28.243.52:10188" + url);
            // 开启心跳
            Runnable heartBeat = () -> clientWebSocket.send("ping");

            if(timer!=null) {
                timer.shutdown();
            }
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(heartBeat,5,10,TimeUnit.SECONDS);
        }

    }

}
