package top.rrricardo.postletter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import top.rrricardo.postletter.utils.ControllerBase;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;

/**
 * 固定的侧边栏
 */
public class HomeController implements ControllerBase {
    @FXML
    private Button messageButton;
    @FXML
    private Button contactsButton;
    @FXML
    private Button personalPageButton;
    @FXML
    private Button exitButton;

    @FXML
    protected void onMessageClick() throws IOException{
        SceneManager.replaceScene("message-view.fxml", 800, 600, "消息");
    }

    @FXML
    protected void onContactsClick() throws IOException{
        SceneManager.replaceScene("contacts-view.fxml", 800, 600, "联系人");
    }

    @FXML
    protected void onPersonalPageClick() throws IOException{
        SceneManager.replaceScene("personalPage-view.fxml", 800, 600, "个人主页");
    }

    @FXML
    protected void onExitClick() throws IOException{
        SceneManager.replaceScene("log-view.fxml", 390, 430, "登录");
    }

}
