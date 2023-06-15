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
        var scene = SceneManager.createScene("log-view.fxml", 390, 430);
        SceneManager.setPrimaryScene(scene, "登录");


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}