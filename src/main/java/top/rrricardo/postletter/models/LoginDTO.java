package top.rrricardo.postletter.models;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginDTO {
    private String username;

    private String password;

    private final String hostname;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
        try {
            hostname = Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHostname() {
        return hostname;
    }
}
