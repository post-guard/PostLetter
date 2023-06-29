package top.rrricardo.postletter.models;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import top.rrricardo.postletter.exceptions.NetworkException;
import top.rrricardo.postletter.services.HttpService;
import top.rrricardo.postletter.utils.SceneManager;

import java.io.IOException;



public class FriendRequestCell extends ListCell<Friend> {

    private final AnchorPane anchorPane;
    private final Label requestLabel;
    private final Button acceptButton;

    private final Friend friend;

    public FriendRequestCell() {
        super();


        anchorPane = new AnchorPane();
        requestLabel = new Label();
        acceptButton = new Button("同意请求");

        anchorPane.getChildren().add(requestLabel);
        anchorPane.getChildren().add(acceptButton);

        friend = new Friend(Configuration.getInstance().getId(), -1);

        acceptButton.setOnAction(e->{

            if(friend.getFriendId() != -1) {
                try {
                    var response = HttpService.getInstance().post("/friend/",friend
                            , new TypeReference<ResponseDTO<Friend>>() {
                            });

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("已同意该好友申请");
                    alert.show();
                    SceneManager.replaceScene("friendRequest-view.fxml", 800, 600, "好友申请");

                } catch (IOException | NetworkException error) {
                    error.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateItem(Friend item, boolean empty) {

        if(item != null) {
            super.updateItem(item, empty);
            setEditable(false);

            try {
                var response = HttpService.getInstance().get("/user/" + item.getUserId()
                        , new TypeReference<ResponseDTO<User>>() {
                        });
                if (response != null) {
                    if (response.getData() != null) {

                        friend.setFriendId(item.getUserId());

                        requestLabel.setText("用户"+response.getData().getNickname()+"向您发起了好友请求");
                        AnchorPane.setLeftAnchor(requestLabel,5.0);
                        AnchorPane.setRightAnchor(acceptButton, 5.0);

                        setGraphic(anchorPane);
                    }
                }
            } catch (IOException | NetworkException e) {
                e.printStackTrace();
            }
        }

        else {
            setGraphic(null);
        }

    }
}
