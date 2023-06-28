package top.rrricardo.postletter.models;

import java.io.Serializable;

public class Friend implements Serializable{
    private int id;
    private int userId;
    private int friendId;
    private int sessionId;

    public Friend() {

    }

    public Friend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
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

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
