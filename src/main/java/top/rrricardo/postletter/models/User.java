package top.rrricardo.postletter.models;

public class User{
    private int id;
    private String nickname;
    private String username;

    public User() {

    }

    public User(String nickname, String username) {
        this.nickname = nickname;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
