package top.rrricardo.postletter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.rrricardo.postletter.models.Message;

import java.util.concurrent.ConcurrentLinkedDeque;

public class WebSocketMessage extends ClientWebSocket {

    public ConcurrentLinkedDeque<Message> messageList;

    public WebSocketMessage(String url) {

        super(url);
        messageList = new ConcurrentLinkedDeque<>();
        // 记得初始化列表不然会炸
    }

    @Override
    public void receive(String text) {

        super.receive(text);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Message message = objectMapper.readValue(text,Message.class);

            messageList.offer(message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("WebSocket收到服务端信息 " + text);
    }
}
