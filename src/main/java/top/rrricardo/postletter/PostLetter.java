package top.rrricardo.postletter;

import javafx.application.Application;
import javafx.stage.Stage;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class PostLetter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setStage(stage);

        var scene = SceneManager.createScene("hello-view.fxml", 300, 200);
        SceneManager.setPrimaryScene(scene, "你好");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}