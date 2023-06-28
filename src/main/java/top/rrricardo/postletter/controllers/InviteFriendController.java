package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.*;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.Common;
import top.rrricardo.postletter.utils.ControllerBase;

import java.io.IOException;
import java.util.Optional;


public class InviteFriendController implements ControllerBase {
    @FXML
    ListView<Friend> listView = new ListView<>();

    @Override
    public void open() {
        try {
            //获取所有好友
            var response = HttpService.getInstance().get("/friend/" + "?userId=" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<Friend[]>>() {});
            if(response != null){
                Friend [] friends = response.getData();
                ObservableList<Friend> items = FXCollections.observableArrayList();
                //获取在群聊里的好友
                var response1 = HttpService.getInstance().get("/participant/session/" + Common.sessionId, new TypeReference<ResponseDTO<Participant[]>>() {});
                if(response1 != null){
                    Participant[] participants = response1.getData();
                    //筛掉已经在群里的好友
                    for(var friend: friends){
                        int i;
                        for(i = 0; i < participants.length; i++){
                            if(friend.getFriendId() == participants[i].getUserId()){
                                break;
                            }
                        }
                        if(i == participants.length){
                            items.add(friend);
                        }
                    }
                }

                listView.setItems(items);
                listView.setCellFactory(friendListView -> new InviteFriendController.FriendCell());    //展示好友列表

                //开启监听器
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldfriend, newFriend) -> {
                    if(newFriend == null){
                        return;
                    }
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert1.setHeaderText("是否邀请该好友进群？");
                    Optional<ButtonType> result = alert1.showAndWait();
                    if(result.isPresent()){
                        ButtonType buttonType = result.get();
                        if(buttonType == ButtonType.OK){
                            try {
                                Participant participant = new Participant(newFriend.getFriendId(), Common.sessionId, 0);
                                var response2 = HttpService.getInstance().post("/participant/", participant, new TypeReference<ResponseDTO<Participant>>() {});
                                if(response2 != null){
                                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                    alert2.setHeaderText("成功邀请好友进群");
                                    alert2.show();
                                }
                            }catch (NetworkException | IOException e){
                                System.out.println("邀请好友失败");
                            }
                        }
                    }

                });
            }
        }catch (NetworkException | IOException e){
            System.out.println("获取好友列表失败");
        }
    }

    private static class FriendCell extends ListCell<Friend> {
        @Override
        protected void updateItem(Friend friend, boolean b) {
            super.updateItem(friend, b);

            if(friend == null){
                this.setText("");
            }else {
                try {
                    var response = HttpService.getInstance().get("/user/" + friend.getFriendId(), new TypeReference<ResponseDTO<User>>() {
                    });

                    if(response != null){
                        User user = response.getData();
                        this.setText(user.getNickname());
                    }
                }catch (NetworkException | IOException e){
                    System.out.println("FriendCell抛出异常");
                }
            }
        }
    }
}
