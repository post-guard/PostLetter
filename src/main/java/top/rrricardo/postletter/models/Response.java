package top.rrricardo.postletter.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private String message;

    private T data;

    public Response() {

    }

    public Response(String message) {
        this.message = message;
        data = null;
    }

    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{\"message\": \"" + message + "\"," + "\"data\": \"" + data +  "\"}";
    }
}
