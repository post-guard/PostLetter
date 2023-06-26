package top.rrricardo.postletter.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.ResponseDTO;
import top.rrricardo.postletter.models.Session;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.ControllerBase;

import java.io.IOException;
import java.util.List;

public class MessageController extends HomeController implements ControllerBase {


    @FXML
    private Button sendButton;
    @FXML
    private Label sessionLabelName;
    @FXML
    private Label sessionLabelLevel;
    @FXML
    private ListView<Session> sessionListView = new ListView<>();

    private final Callback<ListView<Session>, ListCell<Session>> call = TextFieldListCell.forListView(new StringConverter<>() {

        @Override
        public String toString(Session object) {
            return object.getName();
        }

        @Override
        public Session fromString(String string) {

            return new Session(string, "", 100);
        }
    });


    public void open() {
        try {
            var response = HttpService.getInstance().get("/session/", new TypeReference<ResponseDTO<List<Session>>>() {
            });
            System.out.println(response);
            if (response != null) {

                List<Session> sessions = response.getData();

                ObservableList<Session> items = FXCollections.observableArrayList();

                items.addAll(sessions);

                sessionListView.setItems(items);
                sessionListView.setCellFactory(call);
                sessionListView.getSelectionModel().selectedItemProperty().addListener(
                        (ObservableValue<? extends Session> ov, Session old_val,
                         Session new_val) -> {

                            sessionLabelName.setText(new_val.getName());
                            sessionLabelLevel.setText("Lv." + new_val.getLevel());
                        });
            }
        } catch (NetworkException | IOException e) {
            System.out.println("获取会话列表失败");
        }
    }

}
