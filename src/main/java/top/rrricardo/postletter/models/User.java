package top.rrricardo.postletter.models;

public class User {
    private int id;
    private String nickname;
    private String emailAddress;

    public User() {

    }

    public User(String nickname, String emailAddress) {
        this.nickname = nickname;
        this.emailAddress = emailAddress;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
