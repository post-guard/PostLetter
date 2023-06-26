package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.HBox;

import javafx.util.Callback;
import top.rrricardo.postletter.exceptions.NetworkException;

import top.rrricardo.postletter.models.GroupDTO;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.models.User;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.ControllerBase;

import java.io.IOException;


public class ContactsController extends HomeController implements ControllerBase{
    @FXML
    private Button friendsButton;
    @FXML
    private Button groupButton;
    @FXML
    private Button usersButton;
    @FXML
    private HBox hBox;
    @FXML
    private ListView<String> listView = new ListView<>();

    /**
     * 点击好友，查看好友列表
     */
    @FXML
    protected void onFriendsClick(){

    }


    /**
     * 点击群聊，查看群聊列表
     */
    @FXML
    protected void onGroupClick() throws IOException{


    }

    /**
     * 点击用户，查看所有用户列表
     */
    @FXML
    protected void onUsersClick() throws IOException{
        try {
            var response = HttpService.getInstance().get("/user/",  new TypeReference<ResponseDTO<User[]>>(){});

            if(response != null){

                User [] users = response.getData();
//                ObservableList<User> items = FXCollections.observableArrayList();
//                items.addAll(users);
//
//                listView.setItems(items);
                ObservableList<String> items = FXCollections.observableArrayList("今天", "明天");
                for(int i = 0; i < users.length; i++){
                    items.add(users[i].getNickname());
                }

                listView.setItems(items);


            }

        } catch (NetworkException e) {
            //待完善
        }
    }


}
