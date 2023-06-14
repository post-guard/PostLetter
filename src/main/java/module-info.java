module top.rrricardo.postletter {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jfxtras.styles.jmetro;
    requires org.jetbrains.annotations;
                            
    opens top.rrricardo.postletter to javafx.fxml;
    exports top.rrricardo.postletter;
    exports top.rrricardo.postletter.controllers;
    opens top.rrricardo.postletter.controllers to javafx.fxml;
}