package top.rrricardo.postletter.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.rrricardo.postletter.controllers.LoginController;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class Configuration {
    private String token;
    private static Configuration configuration = new Configuration();
    private static final ObjectMapper mapper = new ObjectMapper();
    private Configuration(){

    }
    public static Configuration getInstance(){
        return configuration;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 将令牌写入本地文件
     */
    public static void writeIntoFile() {

        String result = null;
        try {
            result = mapper.writeValueAsString(configuration);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            File file = new File("D:\\myToken.txt");
            FileOutputStream fos = new FileOutputStream(file);

            if (result != null) {
                fos.write(result.getBytes(Charset.defaultCharset()));
            }

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从本地文件加载令牌
     */
    public static void readFromFile(){

        FileInputStream fis = null;
        try {
            File file = new File("D:\\myToken.txt");
            fis = new FileInputStream(file);
            byte [] buffer = new byte[1024];

            //本地令牌文件为空，需要重新登录
            if(fis.read(buffer) == -1){
                var scene = SceneManager.createScene("log-view.fxml", 390, 430);
                SceneManager.setPrimaryScene(scene, "登录已过期，请重新登录");
            }

            String result = new String(buffer);
            configuration =  mapper.readValue(result, Configuration.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
