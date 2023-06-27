package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import javafx.scene.shape.Circle;
import javafx.util.Callback;
import top.rrricardo.postletter.exceptions.NetworkException;

import top.rrricardo.postletter.models.*;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.ControllerBase;

import java.io.IOException;
import java.util.Optional;


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
    private Circle circle;
    @FXML
    private Label circleLabel;
    @FXML
    private Label label1a;
    @FXML
    private Label label1b;
    @FXML
    private Label label2a;
    @FXML
    private Label label2b;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private MenuButton button3; //只会用于群聊拉好友
    /**
     * 保存当前被选中的条目的id
     */
    private OrientationBase selectedItem;

    /**
     * 进入联系人列表，自动加载出好友列表
     * 同时开启监听器
     */
    @Override
    public void open() {
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
                //开启监听器，监听用户点击ListView上的Item,点击一下就展示这个Item的详细资料
                listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrientationBase>() {
                    @Override
                    public void changed(ObservableValue<? extends OrientationBase> observableValue, OrientationBase oldItem, OrientationBase newItem) {
                        if(newItem == null){
                            return;
                        }
                        selectedItem = newItem;
                        //被点击的必定是Friend列表里的Item
                        if(newItem.getNickname() == null && newItem.getFriendId() != 0){
                            try {
                                var response = HttpService.getInstance().get("/user/" + newItem.getFriendId(), new TypeReference<ResponseDTO<User>>(){});

                                if(response != null){
                                    //获取到好友的User信息
                                    User tempUser = response.getData();
                                    circle.setVisible(true);
                                    circleLabel.setText(String.valueOf(tempUser.getNickname().charAt(0))); //头像只显示第一个字符
                                    circleLabel.setStyle("-fx-text-fill: white");
                                    label1a.setText("昵称");
                                    label1b.setText(tempUser.getNickname());
                                    label2a.setText("账号");
                                    label2b.setText(tempUser.getUsername());

                                    button1.setVisible(true);
                                    button1.setDisable(false);
                                    button1.setText("发消息");
                                    button2.setVisible(true);
                                    button2.setDisable(false);
                                    button2.setText("删除好友");    //只有删除好友按钮
                                }
                            }catch (NetworkException | IOException e){
                                return;
                            }
                        }
                        //被点击的必定是Group列表里的Item
                        else if(newItem.getNickname() == null && newItem.getFriendId() == 0){
                            //待完善
                            System.out.println("这是group!");
                        }
                        //被点击的必定是User列表里的Item
                        else {
                            circle.setVisible(true);
                            circleLabel.setText(String.valueOf(newItem.getNickname().charAt(0))); //头像只显示第一个字符
                            circleLabel.setStyle("-fx-text-fill: white");
                            label1a.setText("昵称");
                            label1b.setText(newItem.getNickname());
                            label2a.setText("账号");
                            label2b.setText(newItem.getUsername());
                            //判断当前查看的用户是不是好友
                            try {
                                var response = HttpService.getInstance().get("/friend/" + "?userId=" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<Friend[]>>() {
                                });
                                if(response != null){
                                    Friend [] friends = response.getData();
                                    //遍历好友列表
                                    int i;
                                    for(i = 0; i < friends.length; i++){
                                        //是好友
                                        if(friends[i].getFriendId() == newItem.getId()){
                                            button1.setVisible(true);
                                            button1.setDisable(false);
                                            button1.setText("发消息");
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
        }catch (NetworkException | IOException e){
            System.out.println("加载失败");
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
        button3.setDisable(true);
        button3.setVisible(false);
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
                    var response = HttpService.getInstance().get("/user/" + friend.getFriendId(), new TypeReference<ResponseDTO<User>>() {
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


            }
        } catch (NetworkException e) {
            return;
        }
    }


    @FXML
    protected void clickButton1(){
        //没有第二个按钮，那么选中的Item一定是User，且不是好友，button1的功能为添加好友
        if(!button2.isVisible()) {
            //弹窗：是否添加好友？
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("是否添加该用户为好友？");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()){
                ButtonType buttonType = result.get();
                //确定添加好友
                if(buttonType == ButtonType.OK){
                    try {
                        Friend friend = new Friend(Configuration.getInstance().getId(), selectedItem.getId());
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
        //有第二个按钮，button1的功能必定是跳转到发消息界面
        else {

        }

    }


    @FXML
    protected void clickButton2(){
        //如果是群聊列表，button2的功能是退出群聊
        if(state == 2){

        }
        //否则，button2的功能只能是删除好友
        else {
            //弹窗：是否删除好友？
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("是否删除该好友？");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()){
                ButtonType buttonType = result.get();
                //确定删除好友
                if(buttonType == ButtonType.OK){
                    try {
                        var response = HttpService.getInstance().delete("/friend/" + selectedItem.getFriendId(), new TypeReference<ResponseDTO<Friend>>() {
                        });
                    }catch (NetworkException | IOException e){
                        System.out.println("删除失败");
                    }
                }
            }
        }


    }

    /**
     * 只有点击群聊列表的Item后，才会该按钮
     * 只用于拉好友入群
     */
    protected void clickButton3(){

    }


}
