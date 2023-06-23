package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.CreateUserDTO;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {
    @FXML
    private Button registerButton;
    @FXML
    private Button returnButton;
    @FXML
    private Label label;    //显示注册信息
    @FXML
    private TextField nicknameInput;
    @FXML
    private TextField userNameInput;
    @FXML
    private PasswordField passwordField1;
    @FXML
    private PasswordField passwordField2;

    //点击确认注册按钮
    @FXML
    protected void onRegisterClick() throws IOException {

        label.setTextFill(Color.rgb(255, 20, 20));

        //对所有输入框进行判空，以及判断两次输入的密码是否一样
        if (!Objects.equals(nicknameInput.getText(), "")) {
            if (!Objects.equals(userNameInput.getText(), "")) {
                if (!Objects.equals(passwordField1.getText(), "")) {
                    if (!Objects.equals(passwordField2.getText(), "")) {
                        if (Objects.equals(passwordField1.getText(), passwordField2.getText())) {
                            //判断输入的用户名是否合法（规定必须是长度6-20的数字、字母、下划线组成）
                            if (userNameInput.getText().length() < 6 || userNameInput.getText().length() > 20) {
                                label.setText("用户名长度必须是6~20");
                            } else {
                                //检测用户名是否有除数字、字母、下划线之外的非法符号
                                char[] array = userNameInput.getText().toCharArray();
                                for (char value : array) {
                                    if (!(value >= 'a' && value <= 'z' || value >= 'A' && value <= 'Z' ||
                                            value >= '0' && value <= '9' || value == '_')) {
                                        label.setText("用户名只能由数字、字母、下划线组成");
                                        return;
                                    }
                                }

                                if (passwordField1.getText().length() < 6 || passwordField1.getText().length() > 20) {
                                    label.setText("密码长度必须是6~20");
                                    return;
                                } else {
                                    //检测密码里是否有除数字、字母、下划线之外的非法符号
                                    char[] array1 = passwordField1.getText().toCharArray();
                                    for (char c : array1) {
                                        if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' ||
                                                c >= '0' && c <= '9' || c == '_')) {
                                            label.setText("密码只能由数字、字母、下划线组成");
                                            return;
                                        }
                                    }
                                }

                                //所有输入合法后，才能发起注册请求
                                var createUserDTO = new CreateUserDTO(nicknameInput.getText(), userNameInput.getText(),
                                        passwordField1.getText());

                                try {
                                    var response = HttpService.postBody("http://10.28.243.52:10188/user/register", createUserDTO,
                                            new TypeReference<ResponseDTO<Object>>() {
                                            });

                                    if (response != null) {
                                        label.setText("注册成功，请返回登录页登录");
                                        label.setTextFill(Color.rgb(20, 255, 20));
                                    }
                                } catch (NetworkException e) {
                                    label.setText(e.getMessage());
                                    label.setTextFill(Color.rgb(255, 20, 20));
                                }
                            }
                        }
                        else{
                                label.setText("两次输入的密码不一致");
                            }
                        } else {
                            label.setText("请再次输入密码");
                        }
                    } else {
                        label.setText("密码不能为空");
                    }
                } else {
                    label.setText("用户名不能为空");
                }
            } else {
                label.setText("昵称不能为空");
            }
        }



        //返回登录页
        @FXML
        protected void onReturnClick () throws IOException {
            SceneManager.popScene();
        }

}
