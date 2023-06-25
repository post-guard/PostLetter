package top.rrricardo.postletter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import top.rrricardo.postletter.utils.ControllerBase;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class FaceRecognitionController implements ControllerBase{
    @FXML
    private Button returnButton;
    @FXML
    private Label label;

    /**
     * 点击返回，回到账号密码登录页面
     * @throws IOException
     */
    @FXML
    protected void onReturnClick() throws IOException{
        SceneManager.popScene();
    }

}
