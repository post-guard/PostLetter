module top.rrricardo.postletter {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens top.rrricardo.postletter to javafx.fxml;
    exports top.rrricardo.postletter;
}