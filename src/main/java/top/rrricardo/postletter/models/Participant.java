package top.rrricardo.postletter.models;

import java.io.Serializable;

public class Participant implements Serializable {
    private int id;

    private int userId;

    private int sessionId;

    private int permission;

    public Participant(int userId, int sessionId, int permission) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.permission = permission;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
