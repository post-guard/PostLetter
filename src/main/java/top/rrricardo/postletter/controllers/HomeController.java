package top.rrricardo.postletter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import top.rrricardo.postletter.utils.ControllerBase;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class HomeController implements ControllerBase {
    @FXML
    private Button exitButton;

    /**
     * 点击退出，退出登录，回到登录页
     * @throws IOException
     */
    @FXML
    protected void onExitClick() throws IOException{
        SceneManager.popScene();
    }
}
