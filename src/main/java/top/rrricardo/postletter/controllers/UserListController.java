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
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;
import java.util.Optional;

public class UserListController extends HomeController{
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
    private Button postButton;  //用户列表这里只有加好友按钮
    @FXML
    private ListView<User> listView = new ListView<>();
    private User userSelected;

    @Override
    public void open() {
        friendsButton.setStyle("-fx-border-color: #ffffff");
        groupButton.setStyle("-fx-border-color: #ffffff");
        usersButton.setStyle("-fx-border-color: #512cb9");

        try {
            var response = HttpService.getInstance().get("/user/",  new TypeReference<ResponseDTO<User[]>>(){});
            if(response != null){
                User [] users = response.getData();
                ObservableList<User> items = FXCollections.observableArrayList();
                items.addAll(users);
                listView.setItems(items);

                listView.setCellFactory(userListView -> new UserCell());
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue, olduser, newUser) -> {
                    if(newUser == null){
                        return;
                    }
                    //保存选中的User对象
                    userSelected = newUser;
                    try {
                        var response1 = HttpService.getInstance().get("/friend/" + "?userId=" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<Friend[]>>() {
                        });
                        if(response1 != null){
                            Friend [] friends = response1.getData();
                            //遍历好友列表
                            int i;
                            for(i = 0; i < friends.length; i++){
                                //是好友
                                if(friends[i].getFriendId() == newUser.getId()){
                                    postButton.setDisable(true);
                                    postButton.setVisible(false);
                                    break;
                                }
                            }
                            //不是好友，提供加好友按钮
                            if(i == friends.length){
                                postButton.setDisable(false);
                                postButton.setVisible(true);
                            }
                            circle.setVisible(true);
                            circleLabel.setText(String.valueOf(newUser.getNickname().charAt(0))); //头像只显示第一个字符
                            label1.setText("昵称");
                            label2.setText(newUser.getNickname());
                            label3.setText("账号");
                            label4.setText(newUser.getUsername());
                        }
                    }catch (NetworkException | IOException e){
                        System.out.println("获取用户资料失败");
                    }
                });
            }
        }catch (NetworkException | IOException e){
            System.out.println("获取用户列表失败");
        }


    }

    /**
     * 加好友按钮
     */
    @FXML
    protected void onPostClick(){
        //弹窗：是否添加好友？
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("是否添加该用户为好友？");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            ButtonType buttonType = result.get();
            //确定添加好友
            if(buttonType == ButtonType.OK){
                try {
                    Friend friend = new Friend(Configuration.getInstance().getId(), userSelected.getId());
                    var response = HttpService.getInstance().post("/friend/", friend, new TypeReference<ResponseDTO<Friend>>() {
                    });

                    if(response != null){
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setHeaderText("已发起好友申请");
                        alert1.show();
                    }
                }catch (NetworkException | IOException e){
                    System.out.println("添加失败");
                }
            }
        }

    }

    /**
     *切换查看好友列表
     */
    @FXML
    protected void onFriendsClick() throws IOException {
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
     * 更新用户列表
     */
    @FXML
    protected void updateUserList() throws IOException{
        SceneManager.replaceScene("userList-view.fxml", 800, 600, "联系人-所有用户列表");
    }


    private static class UserCell extends ListCell<User>{
        @Override
        protected void updateItem(User user, boolean b) {
            super.updateItem(user, b);

            if(user == null){
                this.setText("");
            }else {
                this.setText(user.getNickname());
            }
        }
    }
}
