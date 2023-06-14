package top.rrricardo.postletter.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class HttpService {
    private static final OkHttpClient s_client = new OkHttpClient();
    private static final ObjectMapper s_mapper = new ObjectMapper();
    private static final MediaType s_json_mediaType = MediaType.get("application/json; charset=utf-8");

    /**
     * 发出HTTP GET请求
     * @param uri 请求地址
     * @param responseType 返回的响应对象类
     * @return 响应对象
     * @param <T> 请求的响应对象类
     * @throws IOException 请求中的异常
     */
    @Nullable
    public static <T> T getBody(String uri, Class<T> responseType) throws IOException {
        var request = new Request.Builder()
                .url(uri)
                .build();

        try (var response = s_client.newCall(request).execute()) {
            if (response.body() != null) {
                return s_mapper.readValue(response.body().charStream(), responseType);
            }
        }

        return null;
    }

    /**
     * 发出HTTP POST请求
     * @param uri 请求地址
     * @param responseType 返回的响应对象类
     * @param data 请求对象
     * @return 响应对象
     * @param <R> 请求的响应对象类
     * @param <V> 请求的对象类
     * @throws IOException 请求中的异常
     */
    @Nullable
    public static <R, V> R postBody(String uri, @NotNull V data, Class<R> responseType) throws IOException {
        var request = new Request.Builder()
                .post(RequestBody.create(s_mapper.writeValueAsString(data), s_json_mediaType))
                .url(uri)
                .build();

        try (var response = s_client.newCall(request).execute()) {
            if (response.body() != null) {
                return s_mapper.readValue(response.body().charStream(), responseType);
            }
        }

        return null;
    }

    /**
     * 发出HTTP PUT请求
     * @param uri 请求地址
     * @param responseType 返回的响应对象类
     * @param data 请求对象
     * @return 响应对象
     * @param <R> 请求的响应对象类
     * @param <V> 请求的对象类
     * @throws IOException 请求中的异常
     */
    @Nullable
    public static <R, V> R putBody(String uri, @NotNull V data, Class<R> responseType) throws IOException {
        var request = new Request.Builder()
                .put(RequestBody.create(s_mapper.writeValueAsString(data), s_json_mediaType))
                .url(uri)
                .build();

        try(var response = s_client.newCall(request).execute()) {
            if (response.body() != null) {
                return s_mapper.readValue(response.body().charStream(), responseType);
            }
        }

        return null;
    }

    /**
     * 发起HTTP DELETE请求
     * @param uri 请求地址
     * @param responseType 请求的响应对象
     * @return 响应对象
     * @param <T> 请求的响应对象类
     * @throws IOException 请求中的异常
     */
    @Nullable
    public static <T> T deleteBody(String uri, Class<T> responseType) throws IOException {
        var request = new Request.Builder()
                .delete()
                .url(uri)
                .build();

        try (var response = s_client.newCall(request).execute()) {
            if (response.body() != null) {
                return s_mapper.readValue(response.body().charStream(), responseType);
            }
        }

        return null;
    }
}
