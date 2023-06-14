package top.rrricardo.postletter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TestController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button button;

    private int counter = 0;

    @FXML
    protected void onClicked() {
        button.setText("Clicked");

        counter++;
        welcomeLabel.setText("Counter = " + counter);
    }
}
