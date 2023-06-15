package top.rrricardo.postletter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button exitButton;

    @FXML
    protected void onExitClick() throws IOException{
        SceneManager.popScene();
    }
}
