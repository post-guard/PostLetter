package top.rrricardo.postletter.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.ResponseDTO;

import java.io.IOException;

public class HttpService {
    private static final OkHttpClient s_client = new OkHttpClient();
    private static final ObjectMapper s_mapper = new ObjectMapper();
    private static final MediaType s_json_mediaType = MediaType.get("application/json; charset=utf-8");

    /**
     * 发出HTTP GET请求
     *
     * @param uri  请求地址
     * @param type 返回的响应对象类
     * @param <T>  请求的响应对象类
     * @return 响应对象
     * @throws IOException 请求中的异常
     */
    @Nullable
    public static <T> ResponseDTO<T> getBody(String uri, TypeReference<ResponseDTO<T>> type) throws IOException, NetworkException {
        var request = new Request.Builder()
                .url(uri)
                .build();

        return sendHelper(request, type);
    }

    /**
     * 发出HTTP POST请求
     *
     * @param uri  请求地址
     * @param type 返回的响应对象类
     * @param data 请求对象
     * @param <R>  请求的响应对象类
     * @param <V>  请求的对象类
     * @return 响应对象
     * @throws IOException 请求中的异常
     */
    @Nullable
    public static <R, V> ResponseDTO<R> postBody(String uri, @NotNull V data, TypeReference<ResponseDTO<R>> type)
            throws IOException, NetworkException {
        var request = new Request.Builder()
                .post(RequestBody.create(s_mapper.writeValueAsString(data), s_json_mediaType))
                .url(uri)
                .build();

        return sendHelper(request, type);
    }

    /**
     * 发出HTTP PUT请求
     *
     * @param uri  请求地址
     * @param type 返回的响应对象类
     * @param data 请求对象
     * @param <R>  请求的响应对象类
     * @param <V>  请求的对象类
     * @return 响应对象
     * @throws IOException 请求中的异常
     */
    @Nullable
    public static <R, V> ResponseDTO<R> putBody(String uri, @NotNull V data, TypeReference<ResponseDTO<R>> type)
            throws IOException, NetworkException {
        var request = new Request.Builder()
                .put(RequestBody.create(s_mapper.writeValueAsString(data), s_json_mediaType))
                .url(uri)
                .build();

        return sendHelper(request, type);
    }

    /**
     * 发起HTTP DELETE请求
     *
     * @param uri  请求地址
     * @param type 请求的响应对象
     * @param <T>  请求的响应对象类
     * @return 响应对象
     * @throws IOException 请求中的异常
     */
    @Nullable
    public static <T> ResponseDTO<T> deleteBody(String uri, TypeReference<ResponseDTO<T>> type)
            throws IOException, NetworkException {
        var request = new Request.Builder()
                .delete()
                .url(uri)
                .build();

        return sendHelper(request, type);
    }

    private static <T> ResponseDTO<T> sendHelper(Request request, TypeReference<ResponseDTO<T>> type)
            throws IOException, NetworkException {
        try (var response = s_client.newCall(request).execute()) {
            if (response.body() != null) {
                var body = s_mapper.readValue(response.body().charStream(), type);

                if (response.isSuccessful()) {
                    return body;
                }

                throw new NetworkException(response.code(), body.getMessage());
            }
        }

        return null;
    }
}
