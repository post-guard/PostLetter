package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Callback;
import top.rrricardo.postletter.exceptions.NetworkException;

import top.rrricardo.postletter.models.*;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.ControllerBase;

import java.io.IOException;


public class ContactsController extends HomeController implements ControllerBase{
    private int state; //标记联系人界面的全局状态
    @FXML
    private Button friendsButton;
    @FXML
    private Button groupButton;
    @FXML
    private Button usersButton;
    @FXML
    private HBox hBox;
    @FXML
    private ListView<OrientationBase> listView = new ListView<>();
    @FXML
    Circle circle;
    @FXML
    Label circleLabel;
    @FXML
    Label label1a;
    @FXML
    Label label1b;
    @FXML
    Label label2a;
    @FXML
    Label label2b;
    @FXML
    Button button1;
    @FXML
    Button button2;

    /**
     * 辅助方法，用于清除右半区
     */
    private void emptyRightArea(){
        circle.setVisible(false);
        circleLabel.setText("");
        label1a.setText("");
        label1b.setText("");
        label2a.setText("");
        label2b.setText("");
        button1.setDisable(true);
        button1.setVisible(false);
        button2.setDisable(true);
        button2.setVisible(false);
    }

    /**
     * 好友列表的单元格显示
     */
    private static class FriendCell extends ListCell<OrientationBase>{
        @Override
        protected void updateItem(OrientationBase friend, boolean b) {
            super.updateItem(friend, b);

            if(friend == null){
                this.setText("");
            }else {
                try {
                    var response = HttpService.getInstance().get("/user/" + "?userId=" + friend.getFriendId(), new TypeReference<ResponseDTO<User>>() {
                    });

                    if(response != null){
                        User user = response.getData();
                        this.setText(user.getNickname());
                    }
                }catch (NetworkException| IOException e){
                    return;
                }
            }
        }
    }
    /**
     * 点击好友，查看好友列表
     */
    @FXML
    protected void onFriendsClick(){
        //设置状态变量为1
        state = 1;
        //清除原有列表
        listView.getItems().clear();
        //限制按钮点击
        friendsButton.setDisable(true);
        groupButton.setDisable(false);
        usersButton.setDisable(false);
        //清除右半区
        emptyRightArea();

        try {
            var response = HttpService.getInstance().get("/friend/" + "?userId=" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<Friend[]>>() {
            });
            if(response != null){
                Friend[] friends = response.getData();
                ObservableList<OrientationBase> items = FXCollections.observableArrayList();
                items.addAll(friends);

                listView.setItems(items);
                listView.setCellFactory(new Callback<>() {
                    @Override
                    public ListCell<OrientationBase> call(ListView<OrientationBase> friendListView) {
                        return new FriendCell();
                    }
                });

                listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrientationBase>() {
                    @Override
                    public void changed(ObservableValue<? extends OrientationBase> observableValue, OrientationBase oldFriend, OrientationBase newFriend) {
                        if(newFriend != null){
                            try {
                                System.out.println("666666id:" + newFriend.getFriendId());
                                var response = HttpService.getInstance().get("/user/" + "?userId=" + newFriend.getFriendId(), new TypeReference<ResponseDTO<User>>(){});

                                if(response != null){
                                    User newUser = response.getData();
                                    circle.setVisible(true);
                                    circleLabel.setText(String.valueOf(newUser.getNickname().charAt(0))); //头像只显示第一个字符
                                    circleLabel.setStyle("-fx-text-fill: white");
                                    label1a.setText("昵称");
                                    label1b.setText(newUser.getNickname());
                                    label2a.setText("账号");
                                    label2b.setText(newUser.getUsername());

                                    button1.setVisible(false);
                                    button1.setDisable(true);
                                    button2.setVisible(true);
                                    button2.setDisable(false);
                                    button2.setText("删除好友");    //只有删除好友按钮
                                }
                            }catch (NetworkException | IOException e){
                                return;
                            }
                        }
                    }
                });
            }
        }catch (NetworkException | IOException e){
            return;
        }

    }

    /**
     * 群聊的单元格显示
     */
    private static class GroupCell extends ListCell<GroupDTO>{
        @Override
        protected void updateItem(GroupDTO groupDTO, boolean b) {
            super.updateItem(groupDTO, b);

            if(groupDTO == null){
                this.setText("");
            }else {
                this.setText(groupDTO.getName());
            }
        }
    }
    /**
     * 点击群聊，查看群聊列表
     */
    @FXML
    protected void onGroupClick() throws IOException{
        //设置状态变量为2
        state = 2;
        //清除原有列表
        listView.getItems().clear();
        //限制按钮点击
        friendsButton.setDisable(false);
        groupButton.setDisable(true);
        usersButton.setDisable(false);
        //清除右半区
        emptyRightArea();

    }


    /**
     * 用户列表的单元格显示
     */
    private static class UserCell extends ListCell<OrientationBase>{
        @Override
        protected void updateItem(OrientationBase user, boolean b) {
            super.updateItem(user, b);

            if(user == null){
                this.setText("");
            }
            else {
                this.setText(user.getNickname());
            }
        }
    }


    /**
     * 点击用户，查看所有用户列表
     */
    @FXML
    protected void onUsersClick() throws IOException{
        //设置状态变量为3
        state = 3;
        //清除原有列表
        listView.getItems().clear();
        //限制按钮点击
        friendsButton.setDisable(false);
        groupButton.setDisable(false);
        usersButton.setDisable(true);
        //清除右半区
        emptyRightArea();

        try {
            var response = HttpService.getInstance().get("/user/",  new TypeReference<ResponseDTO<User[]>>(){});

            if(response != null){
                User [] users = response.getData();
                ObservableList<OrientationBase> items = FXCollections.observableArrayList();
                items.addAll(users);

                listView.setItems(items);
                listView.setCellFactory(new Callback<>() {
                    @Override
                    public ListCell<OrientationBase> call(ListView<OrientationBase> userListView) {
                        return new UserCell();
                    }
                });

                //点击一个用户，这个用户的资料就加载到右半区显示
                listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrientationBase>() {
                    @Override
                    public void changed(ObservableValue<? extends OrientationBase> observableValue, OrientationBase oldUser, OrientationBase newUser) {
                        if(newUser != null){
                            circle.setVisible(true);
                            circleLabel.setText(String.valueOf(newUser.getNickname().charAt(0))); //头像只显示第一个字符
                            circleLabel.setStyle("-fx-text-fill: white");
                            label1a.setText("昵称");
                            label1b.setText(newUser.getNickname());
                            label2a.setText("账号");
                            label2b.setText(newUser.getUsername());
                            //判断当前查看的用户是不是好友
                            try {
                                var response = HttpService.getInstance().get("/friend/" + "?userId=" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<Friend[]>>() {
                                });
                                if(response != null){
                                    Friend [] friends = response.getData();
                                    //遍历好友列表
                                    int i;
                                    for(i = 0; i < friends.length; i++){
                                        //是好友，只提供删除好友按钮
                                        if(friends[i].getFriendId() == newUser.getId()){
                                            button1.setVisible(false);
                                            button1.setDisable(true);
                                            button2.setVisible(true);
                                            button2.setDisable(false);
                                            button2.setText("删除好友");
                                            break;
                                        }
                                    }
                                    //不是好友，只提供添加好友按钮
                                    if(i == friends.length){
                                        button1.setVisible(true);
                                        button1.setDisable(false);
                                        button1.setText("添加好友");
                                        button2.setVisible(false);
                                        button2.setDisable(true);
                                    }
                                }
                            }catch (NetworkException | IOException e){
                                return;
                            }
                        }
                    }
                });
            }
        } catch (NetworkException e) {
            return;
        }
    }


}
