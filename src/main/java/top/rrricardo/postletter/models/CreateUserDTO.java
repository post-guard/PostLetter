package top.rrricardo.postletter.models;

/**
 * 创建用户的请求对象
 */
public class CreateUserDTO {
    private String nickname;

    private String emailAddress;
    private String password;

    public CreateUserDTO(String nickname, String emailAddress, String password) {
        this.nickname = nickname;
        this.emailAddress = emailAddress;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
