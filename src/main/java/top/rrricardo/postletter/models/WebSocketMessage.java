package top.rrricardo.postletter.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.rrricardo.postletter.services.ClientWebSocket;

import java.util.ArrayList;

public class WebSocketMessage extends ClientWebSocket {

    public ArrayList<Message> messageList;

    public WebSocketMessage(String url) {
        super(url);
    }

    @Override
    public void receive(String text) {

        super.receive(text);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Message message = objectMapper.readValue(text,Message.class);
            //TODO:这里list添加操作后socket必断连，原因待查
            //messageList.add(message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("WebSocket收到服务端信息 " + text);
        System.out.println(messageList);
    }
}
