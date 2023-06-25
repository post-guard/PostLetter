package top.rrricardo.postletter.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.*;
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
        //设置令牌
        HttpService.setAuthorizeToken(token, "http://10.28.243.52:10188");
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

            if(fis.read(buffer) != -1){
                String result = new String(buffer);
                configuration =  mapper.readValue(result, Configuration.class);
            }

        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
