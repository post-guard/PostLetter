package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.*;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.Common;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;
import java.util.Optional;

public class GroupListController extends HomeController{
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
    private Button createGroupButton;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Button inviteButton;
    @FXML
    private Button exitGroupButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button setPermissionButton;
    @FXML
    private ListView<GroupDTO> listView = new ListView<>();
    private GroupDTO groupSelected; //保存选中的Group对象
    /**
     * 当前登录的用户在这个群里的权限
     */
    private int currentUserPermission;

    @Override
    public void open() {
        friendsButton.setStyle("-fx-border-color: #ffffff");
        groupButton.setStyle("-fx-border-color: #512cb9");
        usersButton.setStyle("-fx-border-color: #ffffff");
        emptyRightArea();

        try {
            var response = HttpService.getInstance().get("/participant/user/" + Configuration.getInstance().getId() , new TypeReference<ResponseDTO<Participant[]>>(){});
            if(response != null){
                Participant [] participants = response.getData();
                var response1 = HttpService.getInstance().get("/group/" , new TypeReference<ResponseDTO<GroupDTO[]>>(){});
                if(response1 != null){
                    GroupDTO[] groups = response1.getData();
                    ObservableList<GroupDTO> items = FXCollections.observableArrayList();
                    //找出当前用户参加的所有群聊
                    for(Participant participant: participants)
                        for (GroupDTO group : groups) {
                            if (participant.getSessionId() == group.getSessionId()) {
                                items.add(group);
                            }
                        }

                    if(items.size() > 0){
                        listView.setItems(items);
                        listView.setCellFactory(groupDTOListView -> new GroupCell());
                        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, groupDTO, newGroup) -> {
                            if(newGroup == null){
                                return;
                            }
                            groupSelected = newGroup;
                            Common.sessionId = groupSelected.getSessionId();
                            displayInformation();   //展示群资料
                        });
                    }

                }
            }
        }catch (NetworkException | IOException e){
            System.out.println("群聊列表加载失败");
        }

    }

    @FXML
    protected void onCreateGroupClick() throws IOException {
        SceneManager.showAnotherScene("createGroup-view.fxml", 390, 430, "创建群聊");
        updateGroupList();  //创建后刷新群聊列表
    }
    @FXML
    protected void onSendMessageClick() throws IOException{
        onMessageClick();
    }
    @FXML
    protected void onInviteClick() throws IOException{
        SceneManager.showAnotherScene("inviteFriend-view.fxml", 311, 390, "邀请好友入群");
        displayInformation();

    }
    @FXML
    protected void onExitGroupClick(){
        //群主退群，群直接解散
        if(currentUserPermission == 3){
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("是否解散群聊？");
            Optional<ButtonType> result = alert1.showAndWait();
            if(result.isPresent()){
                ButtonType buttonType = result.get();
                //确定解散
                if(buttonType == ButtonType.OK){
                    try {
                        var response = HttpService.getInstance().delete("/session/" + groupSelected.getSessionId(), new TypeReference<ResponseDTO<Session>>(){});
                        if(response != null){
                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setHeaderText("已解散群聊");
                            alert2.show();
                            updateGroupList();//刷新群聊列表
                        }
                    }catch (NetworkException | IOException e){
                        System.out.println("删群失败");
                    }
                }
            }
        }
        else {
            Alert alert3 = new Alert(Alert.AlertType.CONFIRMATION);
            alert3.setHeaderText("是否退出群聊？");
            Optional<ButtonType> result = alert3.showAndWait();
            if(result.isPresent()){
                ButtonType buttonType = result.get();
                //确定退出
                if(buttonType == ButtonType.OK){
                    try {
                        var response = HttpService.getInstance().get("/participant/session/" + groupSelected.getSessionId(), new TypeReference<ResponseDTO<Participant[]>>(){});
                        if(response != null){
                            Participant [] participants = response.getData();
                            for(Participant participant: participants){
                                if(participant.getUserId() == Configuration.getInstance().getId()){
                                    try {
                                        var response1 = HttpService.getInstance().delete("/participant/" + participant.getId(), new TypeReference<ResponseDTO<Participant>>(){});
                                        if(response1 != null){
                                            Alert alert4 = new Alert(Alert.AlertType.INFORMATION);
                                            alert4.setHeaderText("退群成功");
                                            alert4.show();
                                            updateGroupList(); //更新群列表
                                        }
                                    }catch (NetworkException | IOException e){
                                        System.out.println("退群失败！");
                                    }
                                }
                            }
                        }
                    }catch (NetworkException | IOException e){
                        System.out.println("退群时获取参与者列表失败");
                    }
                }
            }
        }
    }
    @FXML
    protected void onRemoveClick() throws IOException{
        SceneManager.showAnotherScene("removeMember-view.fxml", 311, 390, "移除群成员");
        displayInformation();
    }
    @FXML
    protected void onSetPermissionClick() throws IOException{
        SceneManager.showAnotherScene("setPermission-view.fxml", 311, 390, "设置权限");

    }

    @FXML
    protected void onFriendsClick() throws IOException {
        SceneManager.replaceScene("friendList-view.fxml", 800, 600, "联系人-好友列表");
    }

    @FXML
    protected void updateGroupList() throws IOException {
        SceneManager.replaceScene("groupList-view.fxml", 800, 600, "联系人-群聊列表");
    }
    @FXML
    protected void onUsersClick() throws IOException{
        SceneManager.replaceScene("userList-view.fxml", 800, 600, "联系人-所有用户列表");
    }

    /**
     * 根据当前选中的Group对象，显示出资料，并根据权限提供相应按钮
     */
    private void displayInformation(){
        emptyRightArea();
        circle.setVisible(true);
        circleLabel.setText(String.valueOf(groupSelected.getName().charAt(0)));
        circleLabel.setStyle("-fx-text-fill: white");
        label1.setText("群名：" + groupSelected.getName());
        label2.setText("详情：" + groupSelected.getDetails());
        try {
            var response = HttpService.getInstance().get("/session/" + groupSelected.getSessionId(), new TypeReference<ResponseDTO<Session>>() {});
            if(response != null){
                Session session = response.getData();
                label3.setText("当前人数：" + groupSelected.getParticipants().size() + "/" + session.getLevel());
                //获取用户permission
                var response1 = HttpService.getInstance().get("/participant/session/" + groupSelected.getSessionId(), new TypeReference<ResponseDTO<Participant[]>>() {});
                if(response1 != null){
                    Participant [] participants = response1.getData();
                    for(Participant participant: participants){
                        if(participant.getUserId() == Configuration.getInstance().getId()){
                            currentUserPermission = participant.getPermission();
                            break;
                        }
                    }
                }

                sendMessageButton.setDisable(false);
                sendMessageButton.setVisible(true);
                inviteButton.setDisable(false);
                inviteButton.setVisible(true);
                exitGroupButton.setDisable(false);
                exitGroupButton.setVisible(true);
                //根据用户权限提供相应额外功能
                if(currentUserPermission == 3){
                    setButtonForOwner();
                    label4.setText("您的权限：群主");
                }
                else if(currentUserPermission == 1){
                    setButtonForManager();
                    label4.setText("您的权限：管理员");
                }
                else {
                    setButtonForMembers();
                    label4.setText("您的权限：群成员");
                }

            }
        }catch (NetworkException | IOException e){
            System.out.println("获取群资料失败");
        }
    }

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

    private void emptyRightArea(){
        circle.setVisible(false);
        circleLabel.setText("");
        sendMessageButton.setDisable(true);
        sendMessageButton.setVisible(false);
        inviteButton.setDisable(true);
        inviteButton.setVisible(false);
        exitGroupButton.setDisable(true);
        exitGroupButton.setVisible(false);
        removeButton.setDisable(true);
        removeButton.setVisible(false);
        setPermissionButton.setDisable(true);
        setPermissionButton.setVisible(false);
    }

    private void setButtonForMembers(){

    }
    private void setButtonForManager(){
        removeButton.setDisable(false);
        removeButton.setVisible(true);
    }
    private void setButtonForOwner(){
        removeButton.setDisable(false);
        removeButton.setVisible(true);
        setPermissionButton.setDisable(false);
        setPermissionButton.setVisible(true);
    }

}
