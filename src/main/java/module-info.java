module top.rrricardo.postletter {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jfxtras.styles.jmetro;
    requires org.jetbrains.annotations;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires opencv;
    requires java.desktop;
    requires javafx.swing;

    opens top.rrricardo.postletter to javafx.fxml;
    exports top.rrricardo.postletter;
    exports top.rrricardo.postletter.controllers;
    opens top.rrricardo.postletter.controllers to javafx.fxml;
    opens top.rrricardo.postletter.models to com.fasterxml.jackson.databind;
}