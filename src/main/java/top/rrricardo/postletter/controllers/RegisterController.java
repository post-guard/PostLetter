package top.rrricardo.postletter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Button registerButton;
    @FXML
    private Button returnButton;
    @FXML
    private Label label;    //显示注册信息

    //做注册的判断
    @FXML
    protected void onRegisterClick(){

    }

    //返回登录页
    @FXML
    protected void onReturnClick() throws IOException {
        SceneManager.popScene();
    }

}
