package top.rrricardo.postletter;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Application;
import javafx.stage.Stage;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.Configuration;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.models.User;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class PostLetter extends Application {
    static {
        nu.pattern.OpenCV.loadLocally();
    }

    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setStage(stage);
        stage.setResizable(false);

        /*
          校验本地token是否还有效
         */
        //获取token
        Configuration.readFromFile();
        String token = Configuration.getInstance().getToken();

        if (token != null) {
            //发出一个请求，以校验token的有效性
            try {
                HttpService.setAuthorizeToken(token, "http://10.28.243.52:10188");
                var response = HttpService.getInstance().get("/user/", new TypeReference<ResponseDTO<User[]>>() {
                });

                //token还有效，无需登录，直接进入主页
                if (response != null) {
                    SceneManager.createScene("message-view.fxml", 800, 600, "主页");
                }

            } catch (NetworkException e) {
                //token过期，需要重新登录
                SceneManager.replaceScene("log-view.fxml", 390, 430, "登录");
            }
        } else {
            //本地没有token，需要登录
            SceneManager.replaceScene("log-view.fxml", 390, 430, "登录");
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}