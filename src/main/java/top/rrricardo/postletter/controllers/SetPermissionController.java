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
import top.rrricardo.postletter.models.Participant;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.models.User;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.Common;
import top.rrricardo.postletter.utils.ControllerBase;

import java.io.IOException;
import java.util.Optional;

public class SetPermissionController implements ControllerBase {
    @FXML
    ListView <Participant> listView = new ListView<>();

    @Override
    public void open() {
        try {
            //获取当前会话的所有成员
            var response = HttpService.getInstance().get("/participant/session/" + Common.sessionId, new TypeReference<ResponseDTO<Participant[]>>() {});
            if(response != null){
                Participant[] participants = response.getData();
                ObservableList<Participant> items = FXCollections.observableArrayList();
                items.addAll(participants);
                listView.setItems(items);
                listView.setCellFactory(participantListView -> new SetPermissionController.ParticipantCell());
                listView.getSelectionModel().selectedItemProperty().addListener((observableValue, participant, newParticipant) -> {
                    if(newParticipant == null){
                        return;
                    }
                    //将普通成员升为管理员
                    if(newParticipant.getPermission() == 0){
                        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                        alert1.setHeaderText("该成员为普通成员，是否升为管理员？");
                        alert1.showAndWait();
                        Optional<ButtonType> result = alert1.showAndWait();
                        if(result.isPresent()){
                            ButtonType buttonType = result.get();
                            if(buttonType == ButtonType.OK){
                                newParticipant.setPermission(1);
                                try {
                                    var response1 = HttpService.getInstance().put("/participant/" + newParticipant.getId(), newParticipant, new TypeReference<ResponseDTO<Participant>>() {});
                                    if(response1 != null){
                                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                        alert2.setHeaderText("已将该成员升为管理员");
                                        alert2.show();
                                    }
                                }catch (NetworkException | IOException e){
                                    System.out.println("修改权限失败");
                                }
                            }
                        }

                    }
                    //将管理员降为普通成员
                    else if(newParticipant.getPermission() == 1){
                        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                        alert1.setHeaderText("该成员为管理员，是否降为普通成员？");
                        alert1.showAndWait();
                        Optional<ButtonType> result = alert1.showAndWait();
                        if(result.isPresent()){
                            ButtonType buttonType = result.get();
                            if(buttonType == ButtonType.OK){
                                newParticipant.setPermission(0);
                                try {
                                    var response1 = HttpService.getInstance().put("/participant/" + newParticipant.getId(), newParticipant, new TypeReference<ResponseDTO<Participant>>() {});
                                    if(response1 != null){
                                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                        alert2.setHeaderText("已将该管理员降为普通成员");
                                        alert2.show();
                                    }
                                }catch (NetworkException | IOException e){
                                    System.out.println("修改权限失败");
                                }
                            }
                        }
                    }else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("您是群主，不能修改自己的权限");
                    }

                });

            }
        }catch (NetworkException | IOException e){
            System.out.println("修改权限失败");
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
