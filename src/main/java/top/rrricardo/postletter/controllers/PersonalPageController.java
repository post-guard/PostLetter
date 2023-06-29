package top.rrricardo.postletter.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.Configuration;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.models.User;
import top.rrricardo.postletter.services.HttpService;

import java.io.IOException;

public class PersonalPageController extends HomeController{
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label circleLabel;

    @Override
    public void open() {
        try {
            var response = HttpService.getInstance().get("/user/" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<User>>() {
            });
            if(response != null){
                User user = response.getData();
                nicknameLabel.setText(user.getNickname());
                usernameLabel.setText(user.getUsername());
                circleLabel.setText(String.valueOf(user.getNickname().charAt(0)));
                circleLabel.setStyle("-fx-text-fill: White");
            }
        }catch (NetworkException | IOException e){
            System.out.println("获取个人主页失败");
        }
    }
}
