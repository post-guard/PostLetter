package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import top.rrricardo.postletter.exceptions.NetworkException;

import top.rrricardo.postletter.models.GroupDTO;
import top.rrricardo.postletter.models.ResponseDTO;
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
    private ListView<GroupDTO> listView;

    /**
     * 点击好友，查看好友列表
     */
    @FXML
    protected void onFriendsClick(){

    }


    /**
     * 点击群聊，查看群聊列表
     * @throws IOException
     */
    @FXML
    protected void onGroupClick() throws IOException{

        try {
            var response = HttpService.getInstance().get("/group/",  new TypeReference<ResponseDTO<GroupDTO[]>>(){});

            if(response != null){
                listView = new ListView<>();
                GroupDTO [] groups = response.getData();
                ObservableList<GroupDTO> items = FXCollections.observableArrayList();
                items.addAll(groups);

                listView.setItems(items);
                listView.setOnMouseClicked(click -> {
                    if(click.getClickCount() == 2){
                        GroupDTO selectedItem = listView.getSelectionModel().getSelectedItem();
                        System.out.println("选到的是: " + selectedItem.getName());
                    }
                });

                listView.setCellFactory(param -> new ListCell<GroupDTO>() {
                    public void updateItem(){

                    }
                });



            }

        } catch (NetworkException e) {
            //待完善
        }
    }

    /**
     * 点击用户，查看所有用户列表
     */
    @FXML
    protected void onUsersClick(){

    }


}
