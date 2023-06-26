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

import javafx.scene.shape.Circle;
import javafx.util.Callback;
import top.rrricardo.postletter.exceptions.NetworkException;

import top.rrricardo.postletter.models.Friend;
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
    private ListView<User> listView = new ListView<>();
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
     * 点击好友，查看好友列表
     */
    @FXML
    protected void onFriendsClick(){
        //限制按钮点击
        friendsButton.setDisable(true);
        groupButton.setDisable(false);
        usersButton.setDisable(false);
    }


    /**
     * 点击群聊，查看群聊列表
     */
    @FXML
    protected void onGroupClick() throws IOException{
        //限制按钮点击
        friendsButton.setDisable(false);
        groupButton.setDisable(true);
        usersButton.setDisable(false);

    }

    static class UserCell extends ListCell<User>{
        @Override
        protected void updateItem(User user, boolean b) {
            super.updateItem(user, b);

            if(user == null){
                this.setText("");
            }else{
                this.setText(user.getNickname());
            }
        }
    }

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
     * 点击用户，查看所有用户列表
     */
    @FXML
    protected void onUsersClick() throws IOException{
        //限制按钮点击
        friendsButton.setDisable(false);
        groupButton.setDisable(false);
        usersButton.setDisable(true);
        //将右半区内容置空
        emptyRightArea();

        try {
            var response = HttpService.getInstance().get("/user/",  new TypeReference<ResponseDTO<User[]>>(){});

            if(response != null){
                User [] users = response.getData();
                ObservableList<User> items = FXCollections.observableArrayList();
                items.addAll(users);

                listView.setItems(items);
                listView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
                    @Override
                    public ListCell<User> call(ListView<User> userListView) {
                        return new UserCell();
                    }
                });

                //点击一个用户，这个用户的资料就加载到右半区显示
                listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
                    @Override
                    public void changed(ObservableValue<? extends User> observableValue, User oldUser, User newUser) {
                        if(newUser != null){
                            circle.setVisible(true);
                            circleLabel.setText(String.valueOf(newUser.getNickname().charAt(0))); //头像只显示第一个字符
                            label1a.setText("昵称");
                            label1b.setText(newUser.getNickname());
                            label2a.setText("账号");
                            label2b.setText(newUser.getUsername());
                            //判断当前查看的用户是不是好友
                            try {
                                var response = HttpService.getInstance().get("/friend/" + "?userId=" + newUser.getId(), new TypeReference<ResponseDTO<Friend[]>>() {
                                });
                                if(response != null){
                                    Friend [] friends = response.getData();
                                    //遍历好友列表
                                    int i;
                                    for(i = 0; i < friends.length; i++){
                                        //是好友，只提供删除好友按钮
                                        if(friends[i].getFriendId() == newUser.getId()){
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
