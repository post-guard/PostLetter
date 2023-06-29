package top.rrricardo.postletter.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.models.*;
import top.rrricardo.postletter.services.HttpService;

import java.io.IOException;
import java.util.List;


public class FriendRequestController extends HomeController {
    @FXML
    public ListView<Friend> requestListView;

    public void open() {

        ObservableList<Friend> items = FXCollections.observableArrayList();

        try {
            var response = HttpService.getInstance().get("/friend/request/" + Configuration.getInstance().getId()
                    , new TypeReference<ResponseDTO<List<Friend>>>() {
                    });
            if (response != null) {
                if (response.getData() != null) {
                    items.addAll(response.getData());
                }
            }
        } catch (IOException | NetworkException e) {
            e.printStackTrace();
        }

        requestListView.setItems(items);
        requestListView.setCellFactory(l-> new FriendRequestCell());
    }
}