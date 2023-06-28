package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.*;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.ControllerBase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupController implements ControllerBase {
    @FXML
    private TextField groupNameInput;
    @FXML
    private TextField detailsInput;
    @FXML
    ChoiceBox<Friend> choiceBox = new ChoiceBox<>();
    @FXML
    private Label label;
    @FXML
    private Button okButton;


    @Override
    public void open() {
        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Friend friend) {
                try {
                    var response = HttpService.getInstance().get("/user/" + friend.getFriendId(), new TypeReference<ResponseDTO<User>>() {
                    });
                    if (response != null) {
                        User user = response.getData();
                        return user.getNickname();
                    }
                } catch (NetworkException | IOException e) {
                    System.out.println("创群时加载好友列表失败");
                }
                //理论上永远不会到达这里
                return null;
            }

            @Override
            public Friend fromString(String s) {
                return null;
            }
        });

        try {
            var response = HttpService.getInstance().get("/friend/" + "?userId=" + Configuration.getInstance().getId(), new TypeReference<ResponseDTO<Friend[]>>() {});
            if(response != null){
                Friend [] friends = response.getData();
                choiceBox.getItems().addAll(friends);
            }
        }catch (NetworkException | IOException e){
            System.out.println("创群时获取好友列表失败");
        }
    }

    @FXML
    protected void onOkButtonClick(){
        //判断用户输入是否完整
        if(groupNameInput.getText().equals("")){
            label.setText("群聊名称不能为空");
            label.setTextFill(Color.rgb(255,20,20));
        }
        else if(detailsInput.getText().equals("")){
            label.setText("群聊详情不能为空");
        }
        else {
            List<Integer> list = new ArrayList<>();
            list.add(choiceBox.getValue().getFriendId());
            GroupDTO groupDTO = new GroupDTO(groupNameInput.getText(), detailsInput.getText(), 0, list);
            try {
                var response = HttpService.getInstance().post("/group/", groupDTO, new TypeReference<ResponseDTO<GroupDTO>>() {});
                if(response != null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("创建群聊成功");
                    alert.show();
                }
            }catch (NetworkException | IOException e){
                System.out.println("创建群聊失败");
            }
        }

    }

}
