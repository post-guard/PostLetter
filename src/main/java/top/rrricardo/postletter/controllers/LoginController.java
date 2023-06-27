package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.Configuration;
import top.rrricardo.postletter.models.LoginDTO;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.models.ResponseData;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.ControllerBase;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;
import java.util.Objects;

public class LoginController implements ControllerBase{
    @FXML
    private Label label;    //显示登录信息
    @FXML
    private Button okButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button faceRecognitionButton;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    /**
     *登录确认按钮
     */
    @FXML
    protected void onOKClick() throws IOException {
        /*
          判断用户是否正确输入了账号、密码
         */
        if(Objects.equals(usernameInput.getText(), "") && Objects.equals(passwordInput.getText(), "")){
            label.setText("请输入用户名和密码");
            label.setTextFill(Color.rgb(255,20,20));
            return;
        }
        else if(Objects.equals(usernameInput.getText(), "")){
            label.setText("请输入用户名");
            label.setTextFill(Color.rgb(255,20,20));
            return;
        }
        else if(Objects.equals(passwordInput.getText(), "")){
            label.setText("请输入密码");
            label.setTextFill(Color.rgb(255,20,20));
            return;
        }


        var loginDTO = new LoginDTO(usernameInput.getText(), passwordInput.getText());

        try {
            var response = HttpService.postBody("http://10.28.243.52:10188/user/login", loginDTO,
                    new TypeReference<ResponseDTO<ResponseData>>() {
                    });

            if (response != null) {
                label.setText(response.getMessage());
                label.setTextFill(Color.rgb(20,255,20));

                //如果登录成功，就把token和id保存到本地
                if(response.getData().getToken().length() != 0){
                    String token = response.getData().getToken();
                    Configuration.getInstance().setToken(token);
                    int userId = response.getData().getId();
                    Configuration.getInstance().setId(userId);

                    Configuration.writeIntoFile();
                }

                //登录成功后，显示消息界面
                SceneManager.createScene("message-view.fxml", 800, 600, "主页");
                label.setText("");
            }
        } catch (NetworkException e) {
            label.setText(e.getMessage());
            label.setTextFill(Color.rgb(255,20,20));
        }
    }

    /**
     *切换为人脸识别登录
     */
    @FXML
    protected void onFaceRecognitionClick() throws IOException{
        SceneManager.createScene("face-recognition-view.fxml", 600, 800, "刷脸登录");
    }


    /**
     * 点击注册按钮
     */
    @FXML
    protected void onRegisterClick() throws IOException {
        SceneManager.createScene("register-view.fxml", 390, 430, "新用户注册");
    }

    //还没写好，还要改，，，，，，
    /**
     * 按下回车，触发登录按钮
     */
    @FXML
    protected void loginEnter(KeyEvent keyEvent) throws IOException{
        if(keyEvent.getCode().equals(KeyCode.ENTER))    {
            onOKClick();
        }
    }

}