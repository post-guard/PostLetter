package top.rrricardo.postletter.exceptions;

/**
 * 网络异常类
 */
public class NetworkException extends Exception {
    private final int statusCode;
    public NetworkException(int statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public NetworkException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
