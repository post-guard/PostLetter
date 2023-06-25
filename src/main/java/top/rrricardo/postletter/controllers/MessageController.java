package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.GroupDTO;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.models.Session;
import top.rrricardo.postletter.models.User;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.ControllerBase;

import java.io.IOException;

public class MessageController extends HomeController implements ControllerBase {


    @FXML
    private Button sendButton;
    @FXML
    private ListView<String> sessionListView = new ListView<>();


    public void open(){
        try {
            var response = HttpService.getInstance().get("/user/",  new TypeReference<ResponseDTO<User[]>>(){});

            if(response != null) {

                User[] users = response.getData();

                ObservableList<String> items = FXCollections.observableArrayList("今天", "明天");
                for (int i = 0; i < users.length; i++) {
                    items.add(users[i].getNickname());
                    System.out.println(users[i].getNickname());
                }

                sessionListView.setItems(items);
            }
        } catch(NetworkException | IOException e) {
            System.out.println("获取会话列表失败");
        }
    }

}
