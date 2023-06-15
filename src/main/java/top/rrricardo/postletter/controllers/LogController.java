package top.rrricardo.postletter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

public class LogController {
    @FXML
    private Label label;    //显示登录信息
    @FXML
    private Button okButton;
    @FXML
    private Button registerButton;

    //做登录的判断
    @FXML
    protected void onOKClick() throws IOException {
        //暂时还没做判断，点击登录就会直接进入主页
        var scene = SceneManager.createScene("home-view.fxml", 800, 600);
        SceneManager.pushScene(scene, "个人主页");
    }


    //点击注册按钮，进入注册页面
    @FXML
    protected void onRegisterClick() throws IOException{
        var scene = SceneManager.createScene("register-view.fxml", 390, 430);
        SceneManager.pushScene(scene, "新用户注册");
    }
}