package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.Configuration;
import top.rrricardo.postletter.models.Friend;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.models.User;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.Common;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;
import java.util.Optional;


public class FriendListController extends HomeController{
    @FXML
    private Button friendsButton;
    @FXML
    private Button groupButton;
    @FXML
    private Button usersButton;
    @FXML
    private Circle circle;
    @FXML
    private Label circleLabel;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ListView<Friend> listView = new ListView<>();
    private Friend friendSelected;


    @Override
    public void open() {
        friendsButton.setStyle("-fx-border-color: #512cb9");
        groupButton.setStyle("-fx-border-color: #ffffff");
        usersButton.setStyle("-fx-border-color: #ffffff");

        try {
            var response = HttpService.getInstance().get("/friend/" + "?userId=" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<Friend[]>>() {});
            if(response != null){
                Friend [] friends = response.getData();
                ObservableList<Friend> items = FXCollections.observableArrayList();
                items.addAll(friends);
                listView.setItems(items);
                listView.setCellFactory(friendListView -> new FriendCell());    //展示好友列表

                //开启监听器
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldfriend, newFriend) -> {
                    if(newFriend == null){
                        return;
                    }
                    friendSelected = newFriend; //保存当前选中的好友对象
                    Common.sessionId = friendSelected.getFriendId(); //保存当前选中好友的会话id
                    try {
                        var response1 = HttpService.getInstance().get("/user/" + newFriend.getFriendId(), new TypeReference<ResponseDTO<User>>(){});

                        if(response1 != null){
                            //获取到好友的User信息
                            User tempUser = response1.getData();
                            circle.setVisible(true);
                            circleLabel.setText(String.valueOf(tempUser.getNickname().charAt(0))); //头像只显示第一个字符
                            circleLabel.setStyle("-fx-text-fill: white");
                            label1.setText("昵称");
                            label2.setText(tempUser.getNickname());
                            label3.setText("账号");
                            label4.setText(tempUser.getUsername());
                            sendMessageButton.setVisible(true);
                            deleteButton.setVisible(true);
                        }
                    }catch (NetworkException | IOException e){
                        System.out.println("加载好友资料失败");
                    }
                });
            }
        }catch (NetworkException | IOException e){
            System.out.println("获取好友列表失败");
        }
    }

    /**
     *刷新好友列表
     */
    @FXML
    protected void updateFriendList() throws IOException{
        SceneManager.replaceScene("friendList-view.fxml", 800, 600, "联系人-好友列表");
    }

    /**
     * 切换查看群列表
     */
    @FXML
    protected void onGroupClick() throws IOException {
        SceneManager.replaceScene("groupList-view.fxml", 800, 600, "联系人-群聊列表");
    }

    /**
     * 切换查看全部用户列表
     */
    @FXML
    protected void onUsersClick() throws IOException{
        SceneManager.replaceScene("userList-view.fxml", 800, 600, "联系人-所有用户列表");
    }

    /**
     * 给好友发消息
     */
    @FXML
    protected void sendClick() throws IOException{
        onMessageClick();
    }

    /**
     * 删除该好友
     */
    @FXML
    protected void deleteClick(){
        //弹窗：是否删除好友？
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText("是否删除该好友？");
        Optional<ButtonType> result = alert1.showAndWait();
        if(result.isPresent()){
            ButtonType buttonType = result.get();
            //确定删除好友
            if(buttonType == ButtonType.OK){
                try {
                    var response = HttpService.getInstance().delete("/friend/" + friendSelected.getId(), new TypeReference<ResponseDTO<Friend>>() {
                    });
                    if(response != null){
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setHeaderText("删除成功");
                        alert2.show();
                        updateFriendList();   //刷新好友列表
                    }
                }catch (NetworkException | IOException e){
                    System.out.println("删除失败");
                }
            }
        }
    }

    private static class FriendCell extends ListCell<Friend>{
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
