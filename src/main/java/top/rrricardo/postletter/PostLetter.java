package top.rrricardo.postletter;

import javafx.application.Application;
import javafx.stage.Stage;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class PostLetter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setStage(stage);
        stage.setResizable(false);
        //初始界面为登录页
        SceneManager.replaceScene("log-view.fxml", 390, 430, "登录");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}