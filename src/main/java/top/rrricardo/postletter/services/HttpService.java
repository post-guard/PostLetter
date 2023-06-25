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

    private static HttpService instance;

    private final OkHttpClient client;
    private final String baseUrl;

    private HttpService(@NotNull String token, @NotNull String baseUrl) {
        var builder = new OkHttpClient.Builder();

        builder.authenticator((route, response) -> {
            if (response.request().header("Authorization") != null) {
                return null;
            }

            return response.request().newBuilder()
                    .header("Authorization", token)
                    .build();
        });

        client = builder.build();
        this.baseUrl = baseUrl;
    }

    public static void setAuthorizeToken(@NotNull String token, @NotNull String baseUrl) {
        instance = new HttpService(token, baseUrl);
    }

    @NotNull
    public static HttpService getInstance() {
       if (instance == null) {
           throw new RuntimeException();
       }

       return instance;
    }

    @Nullable
    public <T> ResponseDTO<T> get(String uri, TypeReference<ResponseDTO<T>> type) throws IOException, NetworkException {
        var request = new Request.Builder()
                .url(baseUrl + uri)
                .build();

        return sendHelper(client, request, type);
    }

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

        return sendHelper(s_client, request, type);
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
    public <R, V> ResponseDTO<R> post(String uri, @NotNull V data, TypeReference<ResponseDTO<R>> type)
            throws IOException, NetworkException {
        var request = new Request.Builder()
                .post(RequestBody.create(s_mapper.writeValueAsString(data), s_json_mediaType))
                .url(baseUrl + uri)
                .build();

        return sendHelper(client, request, type);
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

        return sendHelper(s_client, request, type);
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
    public <R, V> ResponseDTO<R> put(String uri, @NotNull V data, TypeReference<ResponseDTO<R>> type)
            throws IOException, NetworkException {
        var request = new Request.Builder()
                .put(RequestBody.create(s_mapper.writeValueAsString(data), s_json_mediaType))
                .url(baseUrl + uri)
                .build();

        return sendHelper(client, request, type);
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

        return sendHelper(s_client, request, type);
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

        return sendHelper(s_client, request, type);
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
    public <T> ResponseDTO<T> delete(String uri, TypeReference<ResponseDTO<T>> type)
            throws IOException, NetworkException {
        var request = new Request.Builder()
                .delete()
                .url(baseUrl + uri)
                .build();

        return sendHelper(client, request, type);
    }

    private static <T> ResponseDTO<T> sendHelper(OkHttpClient client, Request request, TypeReference<ResponseDTO<T>> type)
            throws IOException, NetworkException {
        try (var response = client.newCall(request).execute()) {
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
