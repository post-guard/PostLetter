package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.LoginDTO;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label label;    //显示登录信息
    @FXML
    private Button okButton;
    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    //做登录的判断
    @FXML
    protected void onOKClick() throws IOException {
        var loginDTO = new LoginDTO(Integer.parseInt(usernameInput.getText()), passwordInput.getText());

        try {
            var response = HttpService.postBody("http://10.28.243.52:10188/user/login", loginDTO,
                    new TypeReference<ResponseDTO<String>>() {
                    });

            if (response != null) {
                label.setText(response.getMessage());
                var scene = SceneManager.createScene("home-view.fxml", 800, 600);
                SceneManager.pushScene(scene, "主页");
            }
        } catch (NetworkException e) {
            label.setText(e.getMessage());
        }
    }


    //点击注册按钮，进入注册页面
    @FXML
    protected void onRegisterClick() throws IOException {
        var scene = SceneManager.createScene("register-view.fxml", 390, 430);
        SceneManager.pushScene(scene, "新用户注册");
    }
}