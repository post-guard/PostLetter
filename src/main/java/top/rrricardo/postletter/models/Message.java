package top.rrricardo.postletter.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private int id;

    private int sessionId;

    private int sendId;

    @NotNull
    private String text;

    @NotNull
    private LocalDateTime sendTime;

    public Message(int sessionId, int sendId, @NotNull String text, @NotNull LocalDateTime sendTime) {
        this.sessionId = sessionId;
        this.sendId = sendId;
        this.text = text;
        this.sendTime = sendTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    @NotNull
    public String getText() {
        return text;
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    public @NotNull LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(@NotNull LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }
}
