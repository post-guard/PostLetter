package top.rrricardo.postletter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        var scene = SceneManager.createScene("test-view.fxml", 800, 600);
        SceneManager.pushScene(scene, "test-view");
    }
}