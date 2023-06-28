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

public class RemoveMemberController implements ControllerBase {
    @FXML
    ListView<Participant> listView = new ListView<>();
    private Participant participant1;

    @Override
    public void open() {
        try {
            //获取当前会话的所有成员
            var response = HttpService.getInstance().get("/participant/session/" + Common.sessionId, new TypeReference<ResponseDTO<Participant[]>>() {});
            if(response != null){
                Participant[] participants = response.getData();
                for(Participant participant: participants){
                    //找到自己
                    if(participant.getUserId() == Configuration.getInstance().getId()){
                        participant1 = participant;
                        break;
                    }
                }
                ObservableList<Participant> items = FXCollections.observableArrayList();
                items.addAll(participants);
                listView.setItems(items);
                listView.setCellFactory(participantListView -> new ParticipantCell());
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue, participant, newParticipant) -> {
                    if(newParticipant == null){
                        return;
                    }
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert1.setHeaderText("确定要移除该成员？");
                    Optional<ButtonType> result = alert1.showAndWait();
                    if(result.isPresent()){
                        ButtonType buttonType = result.get();
                        if(buttonType == ButtonType.OK){
                            //比较权限
                            if(participant1.getPermission() > newParticipant.getPermission()){
                                try {
                                    var response1 = HttpService.getInstance().delete("/participant/" + newParticipant.getId(), new TypeReference<ResponseDTO<Participant>>() {});
                                    if(response1 != null){
                                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                        alert2.setHeaderText("已将该成员移出群聊");
                                        alert2.show();
                                    }
                                }catch (NetworkException | IOException e){
                                    System.out.println("移除失败");
                                }
                            }else {
                                Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                                alert3.setHeaderText("不能移除权限比自己高的成员");
                                alert3.show();
                            }
                        }
                    }
                });

            }
        }catch (NetworkException | IOException e){
            System.out.println("踢人时获取成员列表失败");
        }
    }

    private static class ParticipantCell extends ListCell<Participant> {
        @Override
        protected void updateItem(Participant participant, boolean b) {
            super.updateItem(participant, b);

            if(participant == null){
                this.setText("");
            }else {
                try {
                    var response = HttpService.getInstance().get("/user/" + participant.getUserId(), new TypeReference<ResponseDTO<User>>() {
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
